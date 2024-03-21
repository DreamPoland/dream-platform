package cc.dreamcode.platform;

import cc.dreamcode.platform.component.ComponentManager;
import lombok.NonNull;

import java.io.File;
import java.util.Optional;

public interface DreamPlatform {

    void enable(@NonNull ComponentManager componentManager);

    void disable();

    File getDataFolder();

    @NonNull DreamLogger getDreamLogger();

    @NonNull DreamVersion getDreamVersion();

    <T> T registerInjectable(@NonNull String name, @NonNull T t);

    <T> T registerInjectable(@NonNull String name, Class<T> type);

    <T> T registerInjectable(@NonNull T t);

    <T> T registerInjectable(Class<T> type);

    <T> T createInstance(@NonNull Class<T> type);

    <T> Optional<T> getInject(@NonNull String name, @NonNull Class<T> value);

    <T> Optional<T> getInject(@NonNull Class<T> value);
}
