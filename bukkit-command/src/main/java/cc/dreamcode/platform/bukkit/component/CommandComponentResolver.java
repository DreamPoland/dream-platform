package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class CommandComponentResolver implements ComponentClassResolver<BukkitCommand> {

    private final BukkitCommandProvider bukkitCommandProvider;

    @Inject
    public CommandComponentResolver(BukkitCommandProvider bukkitCommandProvider) {
        this.bukkitCommandProvider = bukkitCommandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<BukkitCommand> type) {
        return BukkitCommand.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull BukkitCommand bukkitCommand) {
        return new MapBuilder<String, Object>()
                .put("name", bukkitCommand.getName())
                .put("aliases", bukkitCommand.getAliases())
                .build();
    }

    @Override
    public BukkitCommand resolve(@NonNull Injector injector, @NonNull Class<BukkitCommand> type) {

        final BukkitCommand bukkitCommand = injector.createInstance(type);

        this.bukkitCommandProvider.addCommand(bukkitCommand);
        return bukkitCommand;
    }
}
