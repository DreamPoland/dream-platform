package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.result.ResultResolver;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CommandResultResolver implements ComponentClassResolver<ResultResolver> {

    private final CommandProvider commandProvider;

    @Inject
    public CommandResultResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<ResultResolver> type) {
        return ResultResolver.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-result-resolver";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull ResultResolver resultResolver) {
        return new HashMap<>();
    }

    @Override
    public ResultResolver resolve(@NonNull Injector injector, @NonNull Class<ResultResolver> type) {

        final ResultResolver resultResolver = injector.createInstance(type);

        this.commandProvider.registerResult(resultResolver);
        return resultResolver;
    }
}
