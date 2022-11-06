package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.command.bukkit.BukkitCommandHandler;
import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builders.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class CommandComponentClassResolver extends ComponentClassResolver<Class<BukkitCommandHandler>> {

    private @Inject BukkitCommandProvider bukkitCommandProvider;

    @Override
    public boolean isAssignableFrom(@NonNull Class<BukkitCommandHandler> bukkitCommandHandlerClass) {
        return BukkitCommandHandler.class.isAssignableFrom(bukkitCommandHandlerClass);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<BukkitCommandHandler> bukkitCommandHandlerClass) {
        final BukkitCommandHandler bukkitCommandHandler = injector.createInstance(bukkitCommandHandlerClass);

        return new MapBuilder<String, Object>()
                .put("name", bukkitCommandHandler.getName())
                .put("aliases", bukkitCommandHandler.getAliases())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<BukkitCommandHandler> bukkitCommandHandlerClass) {
        final BukkitCommandHandler bukkitCommandHandler = injector.createInstance(bukkitCommandHandlerClass);

        this.bukkitCommandProvider.addCommand(bukkitCommandHandler);
        return bukkitCommandHandler;
    }
}
