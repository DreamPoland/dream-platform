package cc.dreamcode.platform.component;

import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ObjectComponentClassResolver implements ComponentClassResolver {
    @Override
    public boolean isAssignableFrom(@NonNull Class type) {
        return true;
    }

    @Override
    public String getComponentName() {
        return "raw-component";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Object o) {
        return new HashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object resolve(@NonNull Injector injector, @NonNull Class type) {
        return injector.createInstance(type);
    }
}
