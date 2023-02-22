package cc.dreamcode.platform;

import cc.dreamcode.platform.component.ComponentManager;
import lombok.NonNull;

import java.util.Optional;

public interface DreamPlatform {

    void enable(@NonNull ComponentManager componentManager);

    void disable();

    DreamLogger getDreamLogger();

    @NonNull DreamVersion getDreamVersion();

    void registerInjectable(@NonNull String name, @NonNull Object object);

    void registerInjectable(@NonNull Object object);

    <T> T createInstance(@NonNull Class<T> type);

    <T> Optional<T> getInject(@NonNull String name, @NonNull Class<T> value);

    <T> Optional<T> getInject(@NonNull Class<T> value);
}
