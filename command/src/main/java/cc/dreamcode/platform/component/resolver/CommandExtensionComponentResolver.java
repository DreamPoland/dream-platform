package cc.dreamcode.platform.component.resolver;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.extension.ExtensionResolver;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class CommandExtensionComponentResolver extends ComponentClassResolver<Class<ExtensionResolver<?>>> {

    private @Inject DreamCommandImpl dreamCommand;

    @Override
    public boolean isAssignableFrom(@NonNull Class<ExtensionResolver<?>> extensionResolverClass) {
        return ExtensionResolver.class.isAssignableFrom(extensionResolverClass);
    }

    @Override
    public String getComponentName() {
        return "command-extension";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<ExtensionResolver<?>> extensionResolverClass) {
        final ExtensionResolver<?> extensionResolver = injector.createInstance(extensionResolverClass);
        return new MapBuilder<String, Object>()
                .put("class-type", extensionResolver.getClassType().getSimpleName())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<ExtensionResolver<?>> extensionResolverClass) {
        final ExtensionResolver<?> extensionResolver = injector.createInstance(extensionResolverClass);

        this.dreamCommand.getExtensions().registerExtension(extensionResolver);
        return extensionResolver;
    }
}
