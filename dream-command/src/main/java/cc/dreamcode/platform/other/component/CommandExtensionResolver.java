package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandExtension;
import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CommandExtensionResolver implements ComponentClassResolver<CommandExtension> {

    private final CommandProvider commandProvider;

    @Inject
    public CommandExtensionResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<CommandExtension> type) {
        return CommandExtension.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "command-extension";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull CommandExtension commandExtension) {
        return new HashMap<>();
    }

    @Override
    public CommandExtension resolve(@NonNull Injector injector, @NonNull Class<CommandExtension> type) {

        final CommandExtension commandExtension = injector.createInstance(type);

        this.commandProvider.registerExtension(commandExtension);
        return commandExtension;
    }
}
