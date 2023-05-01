package cc.dreamcode.platform.discord4j.component;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.discord4j.component.listener.EventHandler;
import cc.dreamcode.platform.discord4j.component.listener.Listener;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.utilities.builder.MapBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ListenerComponentResolver extends ComponentClassResolver<Class<Listener>> {

    private @Inject DreamLogger dreamLogger;
    private @Inject GatewayDiscordClient discordClient;

    @Override
    public boolean isAssignableFrom(@NonNull Class<Listener> listenerClass) {
        return Listener.class.isAssignableFrom(listenerClass);
    }

    @Override
    public String getComponentName() {
        return "listener (event)";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<Listener> listenerClass) {
        return new MapBuilder<String, Object>()
                .put("events", Arrays.stream(listenerClass.getDeclaredMethods())
                        .filter(method -> method.getAnnotation(EventHandler.class) != null)
                        .map(Method::getName)
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<Listener> listenerClass) {
        final Listener listener = injector.createInstance(listenerClass);

        Arrays.stream(listenerClass.getDeclaredMethods())
                .filter(method -> method.getAnnotation(EventHandler.class) != null)
                .filter(method -> {
                    if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        this.dreamLogger.warning("Method structure is invalid. (" + method.toGenericString() + ")");
                        return false;
                    }

                    method.setAccessible(true);
                    return true;
                })
                .findFirst()
                .ifPresent(method -> {
                    final Class<? extends Event> eventClass = method.getParameterTypes()[0].asSubclass(Event.class);

                    this.discordClient.on(eventClass).subscribe(event -> {
                        try {
                            method.invoke(listener, event);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new PlatformException(e);
                        }
                    });
                });

        return listener;
    }
}
