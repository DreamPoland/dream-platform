package cc.dreamcode.platform.javacord;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.javacord.component.ConfigurationComponentResolver;
import cc.dreamcode.platform.javacord.component.ListenerComponentResolver;
import cc.dreamcode.platform.javacord.component.TimerTaskComponentResolver;
import cc.dreamcode.platform.javacord.exception.JavacordPlatformException;
import cc.dreamcode.utilities.TimeUtil;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.javacord.api.DiscordApi;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public abstract class DreamJavacordPlatform implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentManager componentManager;
    @Getter private DiscordApi discordApi;

    @Getter private final File dataFolder = new File(".");

    void initialize() {
        long start = System.currentTimeMillis();

        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);

        this.dreamLogger = new DreamJavacordLogger(LoggerFactory.getLogger(this.getClass()));
        this.injector.registerInjectable(this.dreamLogger);

        this.componentManager = new ComponentManager(this.injector);
        this.injector.registerInjectable(this.componentManager);

        this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                this.getDreamVersion().getVersion(),
                this.getDreamVersion().getAuthor()));

        this.dreamLogger.info(String.format("Loading %s resources...",
                this.getDreamVersion().getName()));

        componentManager.registerResolver(ConfigurationComponentResolver.class);
        componentManager.registerResolver(TimerTaskComponentResolver.class);

        this.discordApi = this.login(this.componentManager);
        this.injector.registerInjectable(this.discordApi);

        this.dreamLogger.info(String.format("Logged in as %s (%s)",
                this.discordApi.getYourself().getNicknameMentionTag(),
                this.discordApi.getYourself().getIdAsString()));

        componentManager.registerResolver(ListenerComponentResolver.class);

        this.enable(this.componentManager);

        Duration startupDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        this.dreamLogger.info("Loading complete! Done in " + TimeUtil.convertDurationMills(startupDuration) + "...");
    }

    public abstract @NonNull DiscordApi login(@NonNull ComponentManager componentManager);

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

    public static <T extends DreamJavacordPlatform> T run(@NonNull T platform, @NonNull String[] args) {

        platform.initialize();

        Thread shutdownHook = new Thread(() -> {
            platform.getDreamLogger().info(String.format("Disabling %s...", platform.getDreamVersion().getName()));

            try {
                platform.disable();
            }
            catch (Exception e) {
                throw new JavacordPlatformException("An error was caught when plugin are stopping...", e);
            }

            if (platform.getDiscordApi() != null) {
                platform.getDreamLogger().info(String.format("Disconnecting from %s (%s)",
                        platform.getDiscordApi().getYourself().getNicknameMentionTag(),
                        platform.getDiscordApi().getYourself().getIdAsString()));

                platform.getDiscordApi().disconnect().join();
            }

            platform.getDreamLogger().info(String.format("Active version: v%s - Author: %s",
                    platform.getDreamVersion().getVersion(),
                    platform.getDreamVersion().getAuthor()));
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        return platform;
    }

}
