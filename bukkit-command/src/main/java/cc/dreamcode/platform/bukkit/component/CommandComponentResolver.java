package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.command.bukkit.BukkitCommand;
import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CommandComponentResolver extends ComponentClassResolver<Class<BukkitCommand>> {

    private final BukkitCommandProvider bukkitCommandProvider;

    @Override
    public boolean isAssignableFrom(@NonNull Class<BukkitCommand> BukkitCommandClass) {
        return BukkitCommand.class.isAssignableFrom(BukkitCommandClass);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<BukkitCommand> bukkitCommandClass) {
        final BukkitCommand bukkitCommand = injector.createInstance(bukkitCommandClass);

        return new MapBuilder<String, Object>()
                .put("name", bukkitCommand.getName())
                .put("aliases", bukkitCommand.getAliases())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<BukkitCommand> bukkitCommandClass) {
        final BukkitCommand bukkitCommand = injector.createInstance(bukkitCommandClass);

        this.bukkitCommandProvider.addCommand(bukkitCommand);
        return bukkitCommand;
    }
}
