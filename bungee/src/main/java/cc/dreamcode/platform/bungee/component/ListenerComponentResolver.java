package cc.dreamcode.platform.bungee.component;

import cc.dreamcode.platform.bungee.DreamBungeePlatform;
import cc.dreamcode.platform.component.ComponentClassResolver;
import com.google.common.collect.ImmutableMap;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.event.EventHandler;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ListenerComponentResolver extends ComponentClassResolver<Class<? extends Listener>> {

    private @Inject DreamBungeePlatform dreamBungeePlatform;

    @Override
    public boolean isAssignableFrom(@NonNull Class<? extends Listener> listenerClass) {
        return Listener.class.isAssignableFrom(listenerClass);
    }

    @Override
    public String getComponentName() {
        return "listener (event)";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<? extends Listener> listenerClass) {
        return new ImmutableMap.Builder<String, Object>()
                .put("events", Arrays.stream(listenerClass.getDeclaredMethods())
                        .filter(method -> method.getAnnotation(EventHandler.class) != null)
                        .map(Method::getName)
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<? extends Listener> listenerClass) {
        final Listener listener = injector.createInstance(listenerClass);

        final PluginManager pm = this.dreamBungeePlatform.getProxy().getPluginManager();
        pm.registerListener(this.dreamBungeePlatform, listener);

        return listener;
    }
}
