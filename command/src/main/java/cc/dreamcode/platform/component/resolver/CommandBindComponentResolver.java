package cc.dreamcode.platform.component.resolver;

import cc.dreamcode.command.DreamCommandImpl;
import cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class CommandBindComponentResolver extends ComponentClassResolver<Class<BindResolver<?>>> {

    private @Inject DreamCommandImpl dreamCommand;

    @Override
    public boolean isAssignableFrom(@NonNull Class<BindResolver<?>> bindResolverClass) {
        return BindResolver.class.isAssignableFrom(bindResolverClass);
    }

    @Override
    public String getComponentName() {
        return "command-bind";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<BindResolver<?>> bindResolverClass) {
        final BindResolver<?> bindResolver = injector.createInstance(bindResolverClass);
        return new MapBuilder<String, Object>()
                .put("class-type", bindResolver.getClassType().getSimpleName())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<BindResolver<?>> bindResolverClass) {
        final BindResolver<?> bindResolver = injector.createInstance(bindResolverClass);

        this.dreamCommand.getBinds().registerBind(bindResolver);
        return bindResolver;
    }
}
