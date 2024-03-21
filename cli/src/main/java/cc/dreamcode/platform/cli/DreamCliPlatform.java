package cc.dreamcode.platform.cli;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.exception.PlatformException;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class DreamCliPlatform implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentManager componentManager;

    private void initialize() {
        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);

        this.dreamLogger = new DreamCliLogger(LoggerFactory.getLogger(this.getClass()));
        this.injector.registerInjectable(this.dreamLogger);

        this.componentManager = new ComponentManager(this.injector);
        this.injector.registerInjectable(this.componentManager);

        DreamVersion dreamVersion = this.getDreamVersion();

        this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                dreamVersion.getVersion(),
                dreamVersion.getAuthor()));
        this.dreamLogger.info(String.format("Loading %s resources...",
                dreamVersion.getName()));

        try {
            this.enable(this.componentManager);
        }
        catch (Exception e) {
            throw new PlatformException("An error was caught when platform are starting...", e);
        }

        Thread shutdownHook = new Thread(() -> {
            this.dreamLogger.info(String.format("Disabling %s...",
                    dreamVersion.getName()));

            try {
                this.disable();
            }
            catch (Exception e) {
                throw new PlatformException("An error was caught when plugin are stopping...", e);
            }

            this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                    dreamVersion.getVersion(),
                    dreamVersion.getAuthor()));
        });

        shutdownHook.setName("Shutdown-Worker");
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    @Override
    public <T> T registerInjectable(Class<T> type) {

        final T t = this.createInstance(type);
        return this.registerInjectable(t);
    }

    @Override
    public <T> T registerInjectable(@NonNull String name, Class<T> type) {

        final T t = this.createInstance(type);
        return this.registerInjectable(name, t);
    }

    @Override
    public <T> T registerInjectable(@NonNull T t) {
        this.injector.registerInjectable(t);

        if (this.componentManager.isDebug()) {
            this.dreamLogger.info(
                    new DreamLogger.Builder()
                            .type("Added object instance")
                            .name(t.getClass().getSimpleName())
                            .build()
            );
        }

        return t;
    }

    @Override
    public <T> T registerInjectable(@NonNull String name, @NonNull T t) {
        this.injector.registerInjectable(name, t);

        if (this.componentManager.isDebug()) {
            this.dreamLogger.info(
                    new DreamLogger.Builder()
                            .type("Added object instance")
                            .name(t.getClass().getSimpleName())
                            .meta("name", name)
                            .build()
            );
        }

        return t;
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

    public static void run(@NonNull DreamCliPlatform dreamCliPlatform) {
        dreamCliPlatform.initialize();
    }
}
