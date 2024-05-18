package cc.dreamcode.platform.bukkit.hook;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.bukkit.hook.annotation.Hook;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PluginHookManager {

    private final DreamPlatform dreamPlatform;
    private final DreamLogger dreamLogger;

    private final List<PluginHook> pluginHooks = new ArrayList<>();

    @Inject
    public PluginHookManager(DreamPlatform dreamPlatform, DreamLogger dreamLogger) {
        this.dreamPlatform = dreamPlatform;
        this.dreamLogger = dreamLogger;
    }

    @SuppressWarnings("unchecked")
    public <T extends PluginHook> Optional<T> get(@NonNull Class<T> pluginHookClass) {
        return (Optional<T>) this.pluginHooks
                .stream()
                .filter(hook -> hook.getClass().isAssignableFrom(pluginHookClass))
                .findAny();
    }

    public void registerHook(@NonNull Class<? extends PluginHook> pluginHookClass) {
        final long time = System.currentTimeMillis();

        if (!pluginHookClass.isAnnotationPresent(Hook.class)) {
            throw new RuntimeException("Hook annotation not found");
        }

        final Hook hook = pluginHookClass.getAnnotation(Hook.class);
        if (!Bukkit.getServer().getPluginManager().isPluginEnabled(hook.name())) {
            this.dreamLogger.warning(hook.name() + " not found! Some things may be unavailable.");
            return;
        }

        final PluginHook pluginHook = this.dreamPlatform.createInstance(pluginHookClass);

        pluginHook.onInit();
        this.pluginHooks.add(pluginHook);

        this.dreamLogger.info(
                new DreamLogger.Builder()
                        .type("Added hook")
                        .name(hook.name())
                        .took(System.currentTimeMillis() - time)
                        .build()
        );
    }

}
