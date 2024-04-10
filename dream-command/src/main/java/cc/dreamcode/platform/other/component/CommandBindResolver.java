package cc.dreamcode.platform.other.component;

import cc.dreamcode.command.CommandProvider;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CommandBindResolver implements ComponentClassResolver<BindResolver<?>> {

    private final CommandProvider commandProvider;

    @Inject
    public CommandBindResolver(CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<BindResolver<?>> type) {
        return BindResolver.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "cmd-bind-resolver";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull BindResolver<?> bindResolver) {
        return new HashMap<>();
    }

    @Override
    public BindResolver<?> resolve(@NonNull Injector injector, @NonNull Class<BindResolver<?>> type) {

        final BindResolver<?> bindResolver = injector.createInstance(type);

        this.commandProvider.registerBind(bindResolver);
        return bindResolver;
    }
}
