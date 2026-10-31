package cc.dreamcode.platform.component;

import eu.okaeri.injector.Injector;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

@SuppressWarnings("rawtypes")
public final class ComponentService {

    @Getter private final Injector injector;

    private final List<ComponentClassResolver> classResolvers = new ArrayList<>();
    private final List<ComponentMethodResolver> methodResolvers = new ArrayList<>();

    private final ComponentClassResolver defaultClassResolver;

    @Getter @Setter private boolean debug = true;

    public ComponentService(Injector injector) {
        this.injector = injector;

        this.defaultClassResolver = injector.createInstance(RawObjectResolver.class);
    }

    public List<ComponentClassResolver> getClassResolvers() {
        return Collections.unmodifiableList(this.classResolvers);
    }

    public List<ComponentMethodResolver> getMethodResolvers() {
        return Collections.unmodifiableList(this.methodResolvers);
    }

    /**
     * Implement component-class-resolver to future class components.
     * @param classResolver resolver class.
     */
    public void registerResolver(@NonNull Class<? extends ComponentClassResolver> classResolver) {
        this.classResolvers.removeIf(componentClassResolver -> classResolver.isAssignableFrom(componentClassResolver.getClass()));

        final ComponentClassResolver componentClassResolver = this.injector.createInstance(classResolver);
        this.classResolvers.add(componentClassResolver);
    }

    /**
     * Implement component-method-resolver to future class components.
     * @param methodResolver resolver class.
     */
    public void registerMethodResolver(@NonNull Class<? extends ComponentMethodResolver> methodResolver) {
        this.methodResolvers.removeIf(componentMethodResolver -> methodResolver.isAssignableFrom(componentMethodResolver.getClass()));

        final ComponentMethodResolver componentMethodResolver = this.injector.createInstance(methodResolver);
        this.methodResolvers.add(componentMethodResolver);
    }

    /**
     * This method resolves class and register it into injector.
     * When class is undefined, object will be bound for injection only.
     * Class with constructor can only be register with RegisterObject method.
     *
     * @param componentClass class to register and bind
     * @param consumer       apply changes after register.
     */
    @SuppressWarnings("ALL")
    @SneakyThrows
    public <T> void registerComponent(@NonNull Class<T> componentClass, Consumer<T> consumer) {
        final AtomicReference<ComponentClassResolver> reference = new AtomicReference<>(this.defaultClassResolver);
        for (ComponentClassResolver classResolver : this.classResolvers) {
            if (classResolver.isAssignableFrom(componentClass)) {
                reference.set(classResolver);
            }
        }

        final T t = (T) reference.get().register(this.injector, componentClass, this.debug);
        if (consumer != null) {
            consumer.accept(t);
        }

        this.processMethodResolvers(componentClass, t);
    }

    /**
     * Register an existing instantiated object as a component and bind it into the injector,
     * then process any annotated methods.
     *
     * @param componentObject instance to register and bind
     * @param consumer        apply changes after register.
     */
    @SuppressWarnings("ALL")
    public <T> void registerComponentObject(@NonNull T componentObject, Consumer<T> consumer) {
        this.injector.registerInjectable(componentObject);

        if (consumer != null) {
            consumer.accept(componentObject);
        }

        this.processMethodResolvers((Class<T>) componentObject.getClass(), componentObject);
    }

    /**
     * Register an existing instantiated object as a component.
     *
     * @param componentObject instance to register and bind
     */
    public <T> void registerComponentObject(@NonNull T componentObject) {
        this.registerComponentObject(componentObject, null);
    }

    /**
     * Process method resolvers for a component class and its instance.
     *
     * @param componentClass class to scan methods from
     * @param instance       instance of the class
     */
    @SuppressWarnings("ALL")
    private <T> void processMethodResolvers(@NonNull Class<T> componentClass, @NonNull T instance) {
        for (Method declaredMethod : componentClass.getDeclaredMethods()) {
            for (Annotation annotation : declaredMethod.getAnnotations()) {
                final Class<? extends Annotation> annotationType = annotation.annotationType();
                this.methodResolvers.stream()
                        .filter(resolver -> resolver.isAssignableFrom(annotationType))
                        .forEach(resolver -> resolver.register(this.injector, annotation, declaredMethod, instance, this.debug));
            }
        }
    }

    /**
     * This method resolves class and register it into injector.
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
