package cc.dreamcode.platform;

import cc.dreamcode.platform.component.ComponentExtension;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.exception.PlatformException;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.io.File;
import java.util.Optional;

public interface DreamPlatform {

    void enable(@NonNull ComponentService componentService);

    void disable();

    File getDataFolder();

    @NonNull DreamLogger getDreamLogger();

    @NonNull DreamVersion getDreamVersion();

    @NonNull ComponentService getComponentService();

    @NonNull Injector getInjector();

    default <T> T registerInjectable(Class<T> type) {
        final T t = this.createInstance(type);
        return this.registerInjectable(t);
    }

    default <T> T registerInjectable(@NonNull String name, Class<T> type) {
        final T t = this.createInstance(type);
        return this.registerInjectable(name, t);
    }

    default <T> T registerInjectable(@NonNull T t) {
        this.getInjector().registerInjectable(t);

        if (this.getComponentService().isDebug()) {
            this.getDreamLogger().info(
                    new DreamLogger.Builder()
                            .type("Added object instance")
                            .name(t.getClass().getSimpleName())
                            .build()
            );
        }

        return t;
    }

    default <T> T registerInjectable(@NonNull String name, @NonNull T t) {
        this.getInjector().registerInjectable(name, t);

        if (this.getComponentService().isDebug()) {
            this.getDreamLogger().info(
                    new DreamLogger.Builder()
                            .type("Added object instance")
                            .name(t.getClass().getSimpleName())
                            .meta("name", name)
                            .build()
            );
        }

        return t;
    }

    default void registerExtension(@NonNull ComponentExtension extension) {
        this.getComponentService().registerExtension(extension);
    }

    default void registerExtension(@NonNull Class<? extends ComponentExtension> extensionClass) {
        this.getComponentService().registerExtension(extensionClass);
    }

    default <T> T createInstance(@NonNull Class<T> type) {
        return this.getInjector().createInstance(type);
    }

    default <T> Optional<T> getInject(@NonNull String name, @NonNull Class<T> value) {
        return this.getInjector().get(name, value);
    }

    default <T> Optional<T> getInject(@NonNull Class<T> value) {
        return this.getInject("", value);
    }

    default <T> T getInjectOrNull(@NonNull Class<T> value) {
        return this.getInject(value).orElse(null);
    }

    default <T> T getInjectOrNull(@NonNull String name, @NonNull Class<T> value) {
        return this.getInject(name, value).orElse(null);
    }

    default <T> T getInjectOrThrow(@NonNull Class<T> value) {
        return this.getInject(value).orElseThrow(() ->
                new PlatformException("Cannot find injectable for class " + value.getName()));
    }

    default <T> T getInjectOrThrow(@NonNull String name, @NonNull Class<T> value) {
        return this.getInject(name, value).orElseThrow(() ->
                new PlatformException("Cannot find injectable for name '" + name + "' and class " + value.getName()));
    }
}
