package cc.dreamcode.platform.bukkit.hook;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.bukkit.hook.annotation.Hook;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PluginHookService {

    private final DreamPlatform dreamPlatform;
    private final DreamLogger dreamLogger;

    private final List<PluginHook> pluginHooks = new ArrayList<>();

    @Inject
    public PluginHookService(DreamPlatform dreamPlatform, DreamLogger dreamLogger) {
        this.dreamPlatform = dreamPlatform;
        this.dreamLogger = dreamLogger;
    }

    public List<PluginHook> getPluginHooks() {
        return Collections.unmodifiableList(this.pluginHooks);
    }

    public <T extends PluginHook> Optional<T> get(@NonNull Class<T> pluginHookClass) {
        return this.pluginHooks
                .stream()
                .filter(pluginHookClass::isInstance)
                .map(pluginHookClass::cast)
                .findAny();
    }

    public boolean isHookLoaded(@NonNull Class<? extends PluginHook> pluginHookClass) {
        return this.get(pluginHookClass).isPresent();
    }

    public boolean isHookLoaded(@NonNull String pluginName) {
        return this.pluginHooks
                .stream()
                .anyMatch(hook -> {
                    final Hook annotation = hook.getClass().getAnnotation(Hook.class);
                    return annotation != null && annotation.name().equalsIgnoreCase(pluginName);
                });
    }

    public void registerHook(@NonNull Class<? extends PluginHook> pluginHookClass) {
        final long time = System.currentTimeMillis();

        if (!pluginHookClass.isAnnotationPresent(Hook.class)) {
            throw new RuntimeException("Hook annotation not found on class " + pluginHookClass.getName());
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
