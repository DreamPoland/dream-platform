package cc.dreamcode.platform.component;

import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

public interface ComponentMethodResolver<T extends Annotation> {

    boolean isAssignableFrom(@NonNull Class<T> type);

    String getComponentName();

    Map<String, Object> getMetas(@NonNull T t);

    void register(@NonNull Injector injector, @NonNull T t, @NonNull Method method, @NonNull Object instance);

}
