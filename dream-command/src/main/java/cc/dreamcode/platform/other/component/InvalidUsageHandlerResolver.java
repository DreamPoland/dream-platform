package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.handler.InvalidUsageHandler;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class InvalidUsageHandlerResolver implements ComponentClassResolver<InvalidUsageHandler> {

    private final CommandProvider commandProvider;

    @Inject
    public InvalidUsageHandlerResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<InvalidUsageHandler> type) {
        return InvalidUsageHandler.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-invalid-usage-handler";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull InvalidUsageHandler invalidUsageHandler) {
        return new HashMap<>();
    }

    @Override
    public InvalidUsageHandler resolve(@NonNull Injector injector, @NonNull Class<InvalidUsageHandler> type) {

        final InvalidUsageHandler invalidUsageHandler = injector.createInstance(type);

        this.commandProvider.setInvalidUsageHandler(invalidUsageHandler);
        return invalidUsageHandler;
    }
}
