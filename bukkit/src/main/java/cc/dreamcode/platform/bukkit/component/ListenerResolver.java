package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ListenerResolver implements ComponentClassResolver<Listener> {

    private final DreamBukkitPlatform dreamBukkitPlatform;

    @Inject
    public ListenerResolver(DreamBukkitPlatform dreamBukkitPlatform) {
        this.dreamBukkitPlatform = dreamBukkitPlatform;
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
                .put("handlers", Arrays.stream(listener.getClass().getDeclaredMethods())
                        .filter(method -> method.getAnnotation(EventHandler.class) != null)
                        .map(Method::getName)
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public Listener resolve(@NonNull Injector injector, @NonNull Class<Listener> type) {

        final Listener listener = injector.createInstance(type);

        final PluginManager pluginManager = this.dreamBukkitPlatform.getServer().getPluginManager();
        pluginManager.registerEvents(listener, this.dreamBukkitPlatform);

        return listener;
    }
}
