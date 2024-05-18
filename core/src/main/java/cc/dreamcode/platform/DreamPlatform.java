package cc.dreamcode.platform;

import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.component.ComponentExtension;
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

    <T> T registerInjectable(@NonNull String name, @NonNull T t);

    <T> T registerInjectable(@NonNull String name, Class<T> type);

    <T> T registerInjectable(@NonNull T t);

    <T> T registerInjectable(Class<T> type);

    default void registerExtension(@NonNull ComponentExtension extension) {
        this.getComponentService().registerExtension(extension);
    }

    default void registerExtension(@NonNull Class<? extends ComponentExtension> extensionClass) {
        this.getComponentService().registerExtension(extensionClass);
    }

    <T> T createInstance(@NonNull Class<T> type);

    <T> Optional<T> getInject(@NonNull String name, @NonNull Class<T> value);

    <T> Optional<T> getInject(@NonNull Class<T> value);
}
