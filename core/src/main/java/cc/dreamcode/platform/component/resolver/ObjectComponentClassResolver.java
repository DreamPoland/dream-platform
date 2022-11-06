package cc.dreamcode.platform.component.resolver;

import cc.dreamcode.platform.component.ComponentClassResolver;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ObjectComponentClassResolver extends ComponentClassResolver {
    @Override
    public boolean isAssignableFrom(@NonNull Class aClass) {
        return true;
    }

    @Override
    public String getComponentName() {
        return "object";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class aClass) {
        return new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class aClass) {
        return injector.createInstance(aClass);
    }
}
