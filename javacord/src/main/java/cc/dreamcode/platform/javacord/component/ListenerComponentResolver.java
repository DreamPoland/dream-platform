package cc.dreamcode.platform.javacord.component;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.javacord.api.DiscordApi;
import org.javacord.api.event.Event;
import org.javacord.api.listener.GloballyAttachableListener;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ListenerComponentResolver extends ComponentClassResolver<Class<GloballyAttachableListener>> {

    private @Inject DreamLogger dreamLogger;
    private @Inject DiscordApi discordApi;

    @Override
    public boolean isAssignableFrom(@NonNull Class<GloballyAttachableListener> listenerClass) {
        return GloballyAttachableListener.class.isAssignableFrom(listenerClass);
    }

    @Override
    public String getComponentName() {
        return "listener (event)";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<GloballyAttachableListener> listenerClass) {
        return new MapBuilder<String, Object>()
                .put("type", listenerClass.getInterfaces()[0].getSimpleName())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<GloballyAttachableListener> listenerClass) {
        final GloballyAttachableListener listener = injector.createInstance(listenerClass);

        this.discordApi.addListener(listener);

        return listener;
    }
}
