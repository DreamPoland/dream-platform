package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.handler.InvalidSenderHandler;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class InvalidSenderHandlerResolver implements ComponentClassResolver<InvalidSenderHandler> {

    private final CommandProvider commandProvider;

    @Inject
    public InvalidSenderHandlerResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<InvalidSenderHandler> type) {
        return InvalidSenderHandler.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-invalid-sender-handler";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull InvalidSenderHandler invalidSenderHandler) {
        return new HashMap<>();
    }

    @Override
    public InvalidSenderHandler resolve(@NonNull Injector injector, @NonNull Class<InvalidSenderHandler> type) {

        final InvalidSenderHandler invalidSenderHandler = injector.createInstance(type);

        this.commandProvider.setInvalidSenderHandler(invalidSenderHandler);
        return invalidSenderHandler;
    }
}
