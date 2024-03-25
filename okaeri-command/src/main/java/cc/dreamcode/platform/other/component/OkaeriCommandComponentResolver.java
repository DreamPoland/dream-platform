package cc.dreamcode.platform.other.component;

import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.commands.OkaeriCommands;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class OkaeriCommandComponentResolver implements ComponentClassResolver<CommandService> {

    private final OkaeriCommands okaeriCommands;

    @Inject
    public OkaeriCommandComponentResolver(OkaeriCommands okaeriCommands) {
        this.okaeriCommands = okaeriCommands;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<CommandService> type) {
        return CommandService.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull CommandService commandService) {
        final Command command = commandService.getClass().getAnnotation(Command.class);
        if (command == null) {
            return new HashMap<>();
        }

        return new MapBuilder<String, Object>()
                .put("name", command.label())
                .put("aliases", StringUtil.join(command.aliases(), ", "))
                .build();
    }

    @Override
    public CommandService resolve(@NonNull Injector injector, @NonNull Class<CommandService> type) {

        final CommandService commandService = injector.createInstance(type);

        this.okaeriCommands.registerCommand(commandService);
        return commandService;
    }
}
