package cc.dreamcode.platform.other.component;

import cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.other.component.method.CommandMethodResolver;
import lombok.NonNull;

public class DreamCommandExtension implements ComponentExtension {
    @Override
    public void register(@NonNull ComponentService componentService) {
        componentService.registerResolver(CommandBaseResolver.class);
        componentService.registerResolver(CommandExtensionResolver.class);

        componentService.registerResolver(InvalidInputHandlerResolver.class);
        componentService.registerResolver(InvalidPermissionHandlerResolver.class);
        componentService.registerResolver(InvalidSenderHandlerResolver.class);
        componentService.registerResolver(InvalidUsageHandlerResolver.class);

        componentService.registerResolver(ObjectTransformerResolver.class);
        componentService.registerResolver(ArrayTransformerResolver.class);

        componentService.registerResolver(CommandBindResolver.class);
        componentService.registerResolver(CommandResultResolver.class);
        componentService.registerResolver(CommandSuggestionResolver.class);
        componentService.registerResolver(CommandSuggestionFilterResolver.class);

        componentService.registerMethodResolver(CommandMethodResolver.class);
    }
}
