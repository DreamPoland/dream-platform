package cc.dreamcode.platform.component;

import cc.dreamcode.platform.DreamLogger;
import eu.okaeri.injector.Injectable;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.util.Map;

public interface ComponentClassResolver<T> {

    boolean isAssignableFrom(@NonNull Class<T> type);

    String getComponentName();

    Map<String, Object> getMetas(@NonNull T t);

    T resolve(@NonNull Injector injector, @NonNull Class<T> type);

    default T register(@NonNull Injector injector, @NonNull Class<T> type, boolean debug) {
        long start = System.currentTimeMillis();

        final T instance = this.resolve(injector, type);
        injector.registerInjectable(instance);

        long took = System.currentTimeMillis() - start;
        if (debug) {
            injector.getInjectable("", DreamLogger.class)
                    .map(Injectable::getObject)
                    .ifPresent(dreamLogger -> dreamLogger.info(
                            new DreamLogger.Builder()
                                    .type("Added " + this.getComponentName() + " component")
                                    .name(type.getSimpleName())
                                    .took(took)
                                    .meta(this.getMetas(instance))
                                    .build()
                    ));
        }

        return instance;
    }

}
