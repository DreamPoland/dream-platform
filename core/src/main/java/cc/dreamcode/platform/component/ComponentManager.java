package cc.dreamcode.platform.component;

import eu.okaeri.injector.Injector;
import lombok.Getter;
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

    @Getter @Setter private boolean debug = true;

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
     * @param componentClass class to register and bind
     * @param consumer       apply changes after register.
     */
    @SuppressWarnings("ALL")
    @SneakyThrows
    public <T> void registerComponent(@NonNull Class<T> componentClass, Consumer<T> consumer) {
        final ComponentClassResolver defaultObjectResolver = this.injector.createInstance(RawObjectResolver.class);
        final AtomicReference<ComponentClassResolver> reference = new AtomicReference<>(defaultObjectResolver);

        for (Class<? extends ComponentClassResolver> componentResolvers : this.classResolvers) {
            final ComponentClassResolver componentClassResolver = this.injector.createInstance(componentResolvers);
            if (componentClassResolver.isAssignableFrom(componentClass)) {
                reference.set(componentClassResolver);
            }
        }

        if (consumer != null) {
            consumer.accept((T) reference.get().register(this.injector, componentClass, this.debug));
        }
        else {
            reference.get().register(this.injector, componentClass, this.debug);
        }
    }

    /**
     * This method can register all content of this plugin.
     * When class is undefined, object will be bound for injection only.
     * Class with constructor can only be register with RegisterObject method.
     *
     * @param componentClass class to register and bind
     */
    public void registerComponent(@NonNull Class<?> componentClass) {
        this.registerComponent(componentClass, null);
    }

    public void registerExtension(@NonNull ComponentExtension componentExtension) {
        componentExtension.register(this);
    }

    public void registerExtension(@NonNull Class<? extends ComponentExtension> extensionClass) {
        ComponentExtension componentExtension = this.injector.createInstance(extensionClass);
        this.registerExtension(componentExtension);
    }
}
