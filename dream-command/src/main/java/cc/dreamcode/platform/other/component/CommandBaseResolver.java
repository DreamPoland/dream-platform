package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class CommandBaseResolver implements ComponentClassResolver<CommandBase> {

    private final CommandProvider commandProvider;

    @Inject
    public CommandBaseResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<CommandBase> type) {
        return CommandBase.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull CommandBase commandBase) {
        final Command command = commandBase.getClass().getAnnotation(Command.class);
        if (command == null) {
            throw new RuntimeException("Cannot find @Command annotation in class " + commandBase.getClass().getSimpleName());
        }

        return new MapBuilder<String, Object>()
                .put("name", command.name())
                .put("aliases", StringUtil.join(command.aliases(), ", "))
                .build();
    }

    @Override
    public CommandBase resolve(@NonNull Injector injector, @NonNull Class<CommandBase> type) {

        final CommandBase commandBase = injector.createInstance(type);

        this.commandProvider.register(commandBase);
        return commandBase;
    }
}
