package cc.dreamcode.platform.cli;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.cli.exception.CliPlatformException;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
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

    public static void run(@NonNull DreamCliPlatform dreamCliPlatform) {
        dreamCliPlatform.injector = OkaeriInjector.create();
        dreamCliPlatform.injector.registerInjectable(dreamCliPlatform);

        dreamCliPlatform.dreamLogger = new DreamCliLogger(LoggerFactory.getLogger(dreamCliPlatform.getClass()));
        dreamCliPlatform.injector.registerInjectable(dreamCliPlatform.dreamLogger);

        dreamCliPlatform.componentManager = new ComponentManager(dreamCliPlatform.injector);
        dreamCliPlatform.injector.registerInjectable(dreamCliPlatform.componentManager);

        DreamVersion dreamVersion = dreamCliPlatform.getDreamVersion();

        dreamCliPlatform.dreamLogger.info(String.format("Loading %s resources...",
                dreamVersion.getName()));

        try {
            dreamCliPlatform.enable(dreamCliPlatform.componentManager);
        }
        catch (Exception e) {
            throw new CliPlatformException("An error was caught when platform are starting...", e);
        }

        dreamCliPlatform.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                dreamVersion.getVersion(),
                dreamVersion.getAuthor()));

        Thread shutdownHook = new Thread(() -> {
            dreamCliPlatform.dreamLogger.info(String.format("Disabling %s...",
                    dreamVersion.getName()));

            try {
                dreamCliPlatform.disable();
            }
            catch (Exception e) {
                throw new CliPlatformException("An error was caught when plugin are stopping...", e);
            }

            dreamCliPlatform.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                    dreamVersion.getVersion(),
                    dreamVersion.getAuthor()));
        });

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public abstract OkaeriSerdesPack getPluginSerdesPack();

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
