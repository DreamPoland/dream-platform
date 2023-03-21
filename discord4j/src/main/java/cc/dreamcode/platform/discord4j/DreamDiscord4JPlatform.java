package cc.dreamcode.platform.discord4j;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.discord4j.exception.Discord4JPlatformException;
import cc.dreamcode.platform.discord4j.serdes.SerdesDiscord4J;
import discord4j.core.GatewayDiscordClient;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.Timer;

public abstract class DreamDiscord4JPlatform implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentManager componentManager;

    public static void run(@NonNull DreamDiscord4JPlatform dreamDiscord4jPlatform) {
        dreamDiscord4jPlatform.injector = OkaeriInjector.create();
        dreamDiscord4jPlatform.injector.registerInjectable(dreamDiscord4jPlatform);

        dreamDiscord4jPlatform.dreamLogger = new DreamDiscord4JLogger(LoggerFactory.getLogger(dreamDiscord4jPlatform.getClass()));
        dreamDiscord4jPlatform.injector.registerInjectable(dreamDiscord4jPlatform.dreamLogger);

        dreamDiscord4jPlatform.componentManager = new ComponentManager(dreamDiscord4jPlatform.injector);
        dreamDiscord4jPlatform.injector.registerInjectable(dreamDiscord4jPlatform.componentManager);

        DreamVersion dreamVersion = dreamDiscord4jPlatform.getDreamVersion();

        dreamDiscord4jPlatform.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                dreamVersion.getVersion(),
                dreamVersion.getAuthor()));
        dreamDiscord4jPlatform.dreamLogger.info(String.format("Loading %s resources...",
                dreamVersion.getName()));

        try {
            final GatewayDiscordClient gateway = dreamDiscord4jPlatform.load(dreamDiscord4jPlatform.componentManager).block();
            if (gateway == null) {
                throw new Discord4JPlatformException("Discord gateway cannot be null.");
            }
            dreamDiscord4jPlatform.registerInjectable(gateway);

            dreamDiscord4jPlatform.enable(dreamDiscord4jPlatform.componentManager);

            gateway.onDisconnect().block();
        }
        catch (Exception e) {
            throw new Discord4JPlatformException("An error was caught when platform are starting...", e);
        }

        dreamDiscord4jPlatform.dreamLogger.info(String.format("Disabling %s...",
                dreamVersion.getName()));

        try {
            dreamDiscord4jPlatform.disable();
        }
        catch (Exception e) {
            throw new Discord4JPlatformException("An error was caught when plugin are stopping...", e);
        }

        dreamDiscord4jPlatform.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                dreamVersion.getVersion(),
                dreamVersion.getAuthor()));
    }

    public abstract Mono<GatewayDiscordClient> load(@NonNull ComponentManager componentManager);

    public abstract @NonNull OkaeriSerdesPack getDiscord4JConfigurationSerdesPack();
    public @NonNull OkaeriSerdesPack getDiscord4JPersistenceSerdesPack() {
        return registry -> {};
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigurationSerdesPack() {
        return registry -> {
            registry.register(new SerdesDiscord4J());
            registry.register(this.getDiscord4JConfigurationSerdesPack());
        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {
            registry.register(new SerdesDiscord4J());
            registry.register(this.getDiscord4JPersistenceSerdesPack());
        };
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
