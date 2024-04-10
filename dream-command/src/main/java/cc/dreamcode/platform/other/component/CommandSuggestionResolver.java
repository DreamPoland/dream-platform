package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.platform.other.component.annotation.SuggestionKey;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class CommandSuggestionResolver implements ComponentClassResolver<SuggestionSupplier> {

    private final CommandProvider commandProvider;

    @Inject
    public CommandSuggestionResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<SuggestionSupplier> type) {
        return SuggestionSupplier.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "suggestion-supplier";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull SuggestionSupplier suggestionSupplier) {
        final SuggestionKey suggestionKey = suggestionSupplier.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionSupplier must have @SuggestionKey annotation.");
        }

        return MapBuilder.of("key", suggestionKey.value());
    }

    @Override
    public SuggestionSupplier resolve(@NonNull Injector injector, @NonNull Class<SuggestionSupplier> type) {

        final SuggestionSupplier suggestionSupplier = injector.createInstance(type);
        final SuggestionKey suggestionKey = suggestionSupplier.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionSupplier must have @SuggestionKey annotation.");
        }

        this.commandProvider.registerSuggestion(suggestionKey.value(), suggestionSupplier);
        return suggestionSupplier;
    }
}
