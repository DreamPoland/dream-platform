package cc.dreamcode.platform.other.component;

import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.PlatformExtension;
import cc.dreamcode.platform.component.ComponentManager;
import lombok.NonNull;

public class DreamCommandExtension implements PlatformExtension {
    @Override
    public void register(@NonNull DreamPlatform dreamPlatform) {
        final ComponentManager componentManager = dreamPlatform.getComponentManager();

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
    }
}
