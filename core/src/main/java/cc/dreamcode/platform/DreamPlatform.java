package cc.dreamcode.platform;

import cc.dreamcode.platform.component.ComponentManager;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import lombok.NonNull;

import java.io.File;
import java.util.Optional;

public interface DreamPlatform {

    void enable(@NonNull ComponentManager componentManager);

    void disable();

    File getDataFolder();

    @NonNull DreamLogger getDreamLogger();

    @NonNull DreamVersion getDreamVersion();

    @NonNull OkaeriSerdesPack getConfigurationSerdesPack();

    @NonNull OkaeriSerdesPack getPersistenceSerdesPack();

    void registerInjectable(@NonNull String name, @NonNull Object object);

    void registerInjectable(@NonNull Object object);

    <T> T createInstance(@NonNull Class<T> type);

    <T> Optional<T> getInject(@NonNull String name, @NonNull Class<T> value);

    <T> Optional<T> getInject(@NonNull Class<T> value);
}
