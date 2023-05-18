package cc.dreamcode.platform.component;

import cc.dreamcode.platform.component.resolver.ObjectComponentClassResolver;
import eu.okaeri.injector.Injector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public final class ComponentManager {

    private final Injector injector;
    private final List<Class<? extends ComponentClassResolver>> classResolvers = new ArrayList<>();

    @Setter private boolean debug = false;

    /**
     * This method implement new class resolver, before component will be registered.
     * @param classResolver resolver class.
     */
    public void registerResolver(@NonNull Class<? extends ComponentClassResolver> classResolver) {
        this.classResolvers.add(classResolver);
    }

    /**
     * This method can register all content of this plugin.
     * When class is undefined, object will be bound for injection only.
     * Class with constructor can only be register with RegisterObject method.
     *
     * @param componentClass class to register & bind
     * @param consumer       apply changes after register.
     */
    @SuppressWarnings("ALL")
    @SneakyThrows
    public <T> void registerComponent(@NonNull Class<T> componentClass, Consumer<T> consumer) {
        final ComponentClassResolver defaultObjectResolver = ObjectComponentClassResolver.class.getConstructor().newInstance();
        final AtomicReference<ComponentClassResolver> reference = new AtomicReference<>(defaultObjectResolver);

        for (Class<? extends ComponentClassResolver> componentResolvers : this.classResolvers) {
            final ComponentClassResolver componentClassResolver = componentResolvers.newInstance();
            if (componentClassResolver.isAssignableFrom(componentClass)) {
                reference.set(componentClassResolver);
            }
        }

        this.injector.injectFields(reference.get());
        if (consumer != null) {
            consumer.accept((T) reference.get().process(this.injector, componentClass, this.debug));
        }
        else {
            reference.get().process(this.injector, componentClass, this.debug);
        }
    }

    /**
     * This method can register all content of this plugin.
     * When class is undefined, object will be bound for injection only.
     * Class with constructor can only be register with RegisterObject method.
     *
     * @param componentClass class to register & bind
     */
    public void registerComponent(@NonNull Class<?> componentClass) {
        this.registerComponent(componentClass, null);
    }

}
