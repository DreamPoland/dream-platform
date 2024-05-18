package cc.dreamcode.platform.other.component;

import cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.other.component.method.CommandMethodResolver;
import lombok.NonNull;

public class DreamCommandExtension implements ComponentExtension {
    @Override
    public void register(@NonNull ComponentManager componentManager) {
        componentManager.registerResolver(CommandBaseResolver.class);
        componentManager.registerResolver(CommandExtensionResolver.class);

        componentManager.registerResolver(InvalidInputHandlerResolver.class);
        componentManager.registerResolver(InvalidPermissionHandlerResolver.class);
        componentManager.registerResolver(InvalidSenderHandlerResolver.class);
        componentManager.registerResolver(InvalidUsageHandlerResolver.class);

        componentManager.registerResolver(ObjectTransformerResolver.class);
        componentManager.registerResolver(ArrayTransformerResolver.class);

        componentManager.registerResolver(CommandBindResolver.class);
        componentManager.registerResolver(CommandSuggestionResolver.class);
        componentManager.registerResolver(CommandSuggestionFilterResolver.class);

        componentManager.registerMethodResolver(CommandMethodResolver.class);
    }
}
