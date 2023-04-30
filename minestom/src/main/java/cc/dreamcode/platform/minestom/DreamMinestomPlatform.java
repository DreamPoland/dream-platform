package cc.dreamcode.platform.minestom;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.minestom.exception.MinestomExtensionException;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.NonNull;
import net.minestom.server.extensions.Extension;

public abstract class DreamMinestomPlatform extends Extension implements DreamPlatform {

    @Getter
    private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentManager componentManager;
    @Getter private final AtomicBoolean pluginDisabled = new AtomicBoolean(false);

    @Override
    public void preInitialize() {
        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);

        this.dreamLogger = new DreamMinestomLogger(this.getLogger());
        this.injector.registerInjectable(this.dreamLogger);

        this.componentManager = new ComponentManager(this.injector);
        this.injector.registerInjectable(this.componentManager);

        try {
            this.load(this.componentManager);
        }
        catch (Exception e) {
            this.getPluginDisabled().set(true);
            throw new MinestomExtensionException("An error was caught when plugin are loading...", e);
        }
    }

    public abstract void load(@NonNull ComponentManager componentManager);
    public abstract @NonNull OkaeriSerdesPack getMinestomConfigurationSerdesPack();
    public @NonNull OkaeriSerdesPack getMinestomPersistenceSerdesPack() {
        return registry -> {};
    }

    @Override
    public File getDataFolder() {
        return this.getDataDirectory().toFile();
    }

    @Override
    public @NonNull DreamLogger getDreamLogger() {
        return this.dreamLogger;
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigurationSerdesPack() {
        return registry -> {
            registry.register(this.getMinestomConfigurationSerdesPack());
        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(this.getMinestomPersistenceSerdesPack());
        };
    }

    @Override
    public void registerInjectable(@NonNull String name, @NonNull Object object) {
        this.injector.registerInjectable(name, object);

        this.dreamLogger.info(
                new DreamLogger.Builder()
                        .type("Added injectable object")
                        .name(object.getClass().getSimpleName())
                        .meta("name", name)
                        .build()
        );
    }

    @Override
    public void registerInjectable(@NonNull Object object) {
        this.injector.registerInjectable(object);

        this.dreamLogger.info(
                new DreamLogger.Builder()
                        .type("Added injectable object")
                        .name(object.getClass().getSimpleName())
                        .build()
        );
    }

    @Override
    public <T> T createInstance(@NonNull Class<T> type) {
        return this.injector.createInstance(type);
    }

    @Override
    public <T> Optional<T> getInject(@NonNull String name, @NonNull Class<T> value) {
        return this.injector.get(name, value);
    }

    @Override
    public <T> Optional<T> getInject(@NonNull Class<T> value) {
        return this.getInject("", value);
    }
}
