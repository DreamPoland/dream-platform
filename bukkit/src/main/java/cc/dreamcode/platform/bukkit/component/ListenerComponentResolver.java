package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.component.ComponentClassResolver;
import com.google.common.collect.ImmutableMap;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ListenerComponentResolver extends ComponentClassResolver<Class<? extends Listener>> {

    private @Inject DreamBukkitPlatform dreamBukkitPlatform;

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

        final PluginManager pm = this.dreamBukkitPlatform.getServer().getPluginManager();
        pm.registerEvents(listener, this.dreamBukkitPlatform);

        return listener;
    }
}
