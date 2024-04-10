package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ObjectTransformerResolver implements ComponentClassResolver<ObjectTransformer<?>> {

    private final CommandProvider commandProvider;

    @Inject
    public ObjectTransformerResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<ObjectTransformer<?>> type) {
        return ObjectTransformer.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-object-transformer";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull ObjectTransformer<?> objectTransformer) {
        return new HashMap<>();
    }

    @Override
    public ObjectTransformer<?> resolve(@NonNull Injector injector, @NonNull Class<ObjectTransformer<?>> type) {

        final ObjectTransformer<?> objectTransformer = injector.createInstance(type);

        this.commandProvider.registerTransformer(objectTransformer);
        return objectTransformer;
    }
}
