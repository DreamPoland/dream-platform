package cc.dreamcode.platform.component;

import cc.dreamcode.platform.DreamLogger;
import eu.okaeri.injector.Injectable;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.util.Map;

@SuppressWarnings("rawtypes")
public abstract class ComponentClassResolver<T extends Class> {

    public abstract boolean isAssignableFrom(@NonNull T t);
    public abstract String getComponentName();
    public abstract Map<String, Object> getMetas(@NonNull Injector injector, @NonNull T t);
    public abstract Object resolve(@NonNull Injector injector, @NonNull T t);

    public Object process(@NonNull Injector injector, @NonNull T t, boolean debug) {
        long start = System.currentTimeMillis();

        final Object object = this.resolve(injector, t);
        injector.registerInjectable(object);

        long took = System.currentTimeMillis() - start;
        if (debug) {
            injector.getInjectable("", DreamLogger.class)
                    .map(Injectable::getObject)
                    .ifPresent(dreamLogger -> dreamLogger.info(
                            new DreamLogger.Builder()
                                    .type("Added " + this.getComponentName() + " component")
                                    .name(t.getSimpleName())
                                    .took(took)
                                    .meta(this.getMetas(injector, t))
                                    .build()
                    ));
        }

        return object;
    }

}
