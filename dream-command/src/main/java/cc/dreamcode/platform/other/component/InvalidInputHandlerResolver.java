package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.handler.InvalidInputHandler;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class InvalidInputHandlerResolver implements ComponentClassResolver<InvalidInputHandler> {

    private final CommandProvider commandProvider;

    @Inject
    public InvalidInputHandlerResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<InvalidInputHandler> type) {
        return InvalidInputHandler.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-invalid-input-handler";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull InvalidInputHandler invalidInputHandler) {
        return new HashMap<>();
    }

    @Override
    public InvalidInputHandler resolve(@NonNull Injector injector, @NonNull Class<InvalidInputHandler> type) {

        final InvalidInputHandler invalidInputHandler = injector.createInstance(type);

        this.commandProvider.setInvalidInputHandler(invalidInputHandler);
        return invalidInputHandler;
    }
}
