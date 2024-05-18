package cc.dreamcode.platform.component;

import cc.dreamcode.platform.DreamLogger;
import eu.okaeri.injector.Injectable;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

public interface ComponentMethodResolver<T extends Annotation> {

    boolean isAssignableFrom(@NonNull Class<T> type);

    String getComponentName();

    Map<String, Object> getMetas(@NonNull T t);

    void apply(@NonNull Injector injector, @NonNull T t, @NonNull Method method, @NonNull Object instance);

    default void register(@NonNull Injector injector, @NonNull T t, @NonNull Method method, @NonNull Object instance, boolean debug) {
        long start = System.currentTimeMillis();
        this.apply(injector, t, method, instance);

        long took = System.currentTimeMillis() - start;
        if (debug) {
            injector.getInjectable("", DreamLogger.class)
                    .map(Injectable::getObject)
                    .ifPresent(dreamLogger -> dreamLogger.info(
                            new DreamLogger.Builder()
                                    .type("Added " + this.getComponentName() + " method component")
                                    .name(t.getClass().getSimpleName())
                                    .took(took)
                                    .meta(this.getMetas(t))
                                    .build()
                    ));
        }
    }

}
