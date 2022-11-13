package cc.dreamcode.platform.bungee.component;

import cc.dreamcode.command.bungee.BungeeCommand;
import cc.dreamcode.command.bungee.BungeeCommandProvider;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class CommandComponentClassResolver extends ComponentClassResolver<Class<BungeeCommand>> {

    private @Inject BungeeCommandProvider bungeeCommandProvider;

    @Override
    public boolean isAssignableFrom(@NonNull Class<BungeeCommand> bungeeCommandClass) {
        return BungeeCommand.class.isAssignableFrom(bungeeCommandClass);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<BungeeCommand> bungeeCommandClass) {
        final BungeeCommand bungeeCommand = injector.createInstance(bungeeCommandClass);

        return new MapBuilder<String, Object>()
                .put("name", bungeeCommand.getName())
                .put("aliases", bungeeCommand.getAliases())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<BungeeCommand> bungeeCommandClass) {
        final BungeeCommand bungeeCommand = injector.createInstance(bungeeCommandClass);

        this.bungeeCommandProvider.addCommand(bungeeCommand);
        return bungeeCommand;
    }
}
