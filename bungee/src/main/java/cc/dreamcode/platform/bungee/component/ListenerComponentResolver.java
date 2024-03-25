package cc.dreamcode.platform.bungee.component;

import cc.dreamcode.platform.bungee.DreamBungeePlatform;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
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

public class ListenerComponentResolver implements ComponentClassResolver<Listener> {

    private final DreamBungeePlatform dreamBungeePlatform;

    @Inject
    public ListenerComponentResolver(DreamBungeePlatform dreamBungeePlatform) {
        this.dreamBungeePlatform = dreamBungeePlatform;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<Listener> type) {
        return Listener.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "listener (events)";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Listener listener) {
        return new MapBuilder<String, Object>()
                .put("events", Arrays.stream(listener.getClass().getDeclaredMethods())
                        .filter(method -> method.getAnnotation(EventHandler.class) != null)
                        .map(Method::getName)
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public Listener resolve(@NonNull Injector injector, @NonNull Class<Listener> type) {

        final Listener listener = injector.createInstance(type);

        final PluginManager pluginManager = this.dreamBungeePlatform.getProxy().getPluginManager();
        pluginManager.registerListener(this.dreamBungeePlatform, listener);

        return listener;
    }
}
