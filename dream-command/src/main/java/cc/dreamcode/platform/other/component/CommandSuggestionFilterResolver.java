package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.suggestion.filter.SuggestionFilter;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.platform.other.component.annotation.SuggestionKey;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class CommandSuggestionFilterResolver implements ComponentClassResolver<SuggestionFilter> {

    private final CommandProvider commandProvider;

    @Inject
    public CommandSuggestionFilterResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<SuggestionFilter> type) {
        return SuggestionFilter.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "suggestion-filter";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull SuggestionFilter suggestionFilter) {
        final SuggestionKey suggestionKey = suggestionFilter.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionFilter must have @SuggestionKey annotation.");
        }

        return MapBuilder.of("key", suggestionKey.value());
    }

    @Override
    public SuggestionFilter resolve(@NonNull Injector injector, @NonNull Class<SuggestionFilter> type) {

        final SuggestionFilter suggestionFilter = injector.createInstance(type);
        final SuggestionKey suggestionKey = suggestionFilter.getClass().getAnnotation(SuggestionKey.class);
        if (suggestionKey == null) {
            throw new PlatformException("SuggestionFilter must have @SuggestionKey annotation.");
        }

        this.commandProvider.registerSuggestionFilter(suggestionKey.value(), suggestionFilter);
        return suggestionFilter;
    }
}
