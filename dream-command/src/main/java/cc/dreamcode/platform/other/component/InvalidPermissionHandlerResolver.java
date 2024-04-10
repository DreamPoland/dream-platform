package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.handler.InvalidPermissionHandler;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class InvalidPermissionHandlerResolver implements ComponentClassResolver<InvalidPermissionHandler> {

    private final CommandProvider commandProvider;

    @Inject
    public InvalidPermissionHandlerResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<InvalidPermissionHandler> type) {
        return InvalidPermissionHandler.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-invalid-permission-handler";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull InvalidPermissionHandler invalidPermissionHandler) {
        return new HashMap<>();
    }

    @Override
    public InvalidPermissionHandler resolve(@NonNull Injector injector, @NonNull Class<InvalidPermissionHandler> type) {

        final InvalidPermissionHandler invalidPermissionHandler = injector.createInstance(type);

        this.commandProvider.setInvalidPermissionHandler(invalidPermissionHandler);
        return invalidPermissionHandler;
    }
}
