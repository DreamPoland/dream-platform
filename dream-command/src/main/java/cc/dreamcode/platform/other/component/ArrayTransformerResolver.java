package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class ArrayTransformerResolver implements ComponentClassResolver<ArrayTransformer<?>> {

    private final CommandProvider commandProvider;

    @Inject
    public ArrayTransformerResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<ArrayTransformer<?>> type) {
        return ArrayTransformer.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-array-transformer";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull ArrayTransformer<?> arrayTransformer) {
        return new HashMap<>();
    }

    @Override
    public ArrayTransformer<?> resolve(@NonNull Injector injector, @NonNull Class<ArrayTransformer<?>> type) {

        final ArrayTransformer<?> arrayTransformer = injector.createInstance(type);

        this.commandProvider.registerTransformer(arrayTransformer);
        return arrayTransformer;
    }
}
