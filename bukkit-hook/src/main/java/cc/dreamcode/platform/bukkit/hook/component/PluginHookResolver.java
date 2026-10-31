package cc.dreamcode.platform.bukkit.hook.component;

import cc.dreamcode.platform.bukkit.hook.PluginHook;
import cc.dreamcode.platform.bukkit.hook.PluginHookService;
import cc.dreamcode.platform.bukkit.hook.annotation.Hook;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Collections;
import java.util.Map;

public class PluginHookResolver implements ComponentClassResolver<PluginHook> {

    private final PluginHookService pluginHookService;

    @Inject
    public PluginHookResolver(PluginHookService pluginHookService) {
        this.pluginHookService = pluginHookService;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<PluginHook> type) {
        return PluginHook.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "hook";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull PluginHook pluginHook) {
        final Hook hook = pluginHook.getClass().getAnnotation(Hook.class);
        if (hook == null) {
            return Collections.emptyMap();
        }

        return new MapBuilder<String, Object>()
                .put("plugin", hook.name())
                .build();
    }

    @Override
    public PluginHook resolve(@NonNull Injector injector, @NonNull Class<PluginHook> type) {
        this.pluginHookService.registerHook(type);
        return this.pluginHookService.get(type).orElse(null);
    }
}
