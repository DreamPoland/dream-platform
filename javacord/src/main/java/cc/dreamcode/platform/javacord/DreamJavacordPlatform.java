package cc.dreamcode.platform.javacord;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.platform.javacord.component.CommandResolver;
import cc.dreamcode.platform.javacord.component.ListenerResolver;
import cc.dreamcode.platform.javacord.component.TimerTaskResolver;
import cc.dreamcode.platform.javacord.component.command.JavacordCommand;
import cc.dreamcode.platform.javacord.component.method.SchedulerMethodResolver;
import cc.dreamcode.utilities.TimeUtil;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.javacord.api.DiscordApi;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class DreamJavacordPlatform implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentService componentService;
    @Getter private DiscordApi discordApi;

    @Getter private final List<JavacordCommand> javacordCommandList = new ArrayList<>();
    @Getter private final File dataFolder = new File(".");

    void initialize() {
        long start = System.currentTimeMillis();

        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);

        this.dreamLogger = new DreamJavacordLogger(LoggerFactory.getLogger(this.getClass()));
        this.injector.registerInjectable(this.dreamLogger);

        this.componentService = new ComponentService(this.injector);
        this.injector.registerInjectable(this.componentService);

        this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                this.getDreamVersion().getVersion(),
                this.getDreamVersion().getAuthor()));

        this.dreamLogger.info(String.format("Loading %s resources...",
                this.getDreamVersion().getName()));

        this.discordApi = this.login(this.componentService);
        this.injector.registerInjectable(this.discordApi);

        this.dreamLogger.info(String.format("Logged in as %s (%s)",
                this.discordApi.getYourself().getName(),
                this.discordApi.getYourself().getIdAsString()));

        this.componentService.registerResolver(ListenerResolver.class);
        this.componentService.registerResolver(CommandResolver.class);
        this.componentService.registerResolver(TimerTaskResolver.class);
        this.componentService.registerMethodResolver(SchedulerMethodResolver.class);

        try {
            this.enable(this.componentService);
        } catch (Exception e) {
            this.getDreamLogger().error("An error was caught when plugin are starting...", e);

            this.shutdownHook(false);
            return;
        }

        this.discordApi.bulkOverwriteGlobalApplicationCommands(this.javacordCommandList
                .stream()
                .map(JavacordCommand::getSlashCommandBuilder)
                .collect(Collectors.toSet()))
                .join();

        Duration startupDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        this.dreamLogger.info("Loading complete! Done in " + TimeUtil.format(startupDuration) + "...");
    }

    public abstract @NonNull DiscordApi login(@NonNull ComponentService componentService);

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

        if (this.componentService.isDebug()) {
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

        if (this.componentService.isDebug()) {
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

    Thread shutdownHook(boolean disableMethod) {
        Thread shutdownHook = new Thread(() -> {
            this.getDreamLogger().info(String.format("Disabling %s...", this.getDreamVersion().getName()));

            if (disableMethod) {
                try {
                    this.disable();
                } catch (Exception e) {
                    throw new PlatformException("An error was caught when plugin are stopping...", e);
                }
            }

            if (this.getDiscordApi() != null) {
                this.getDreamLogger().info(String.format("Disconnecting from %s (%s)",
                        this.getDiscordApi().getYourself().getName(),
                        this.getDiscordApi().getYourself().getIdAsString()));

                this.getDiscordApi().disconnect().join();
            }

            this.getDreamLogger().info(String.format("Active version: v%s - Author: %s",
                    this.getDreamVersion().getVersion(),
                    this.getDreamVersion().getAuthor()));
        });

        shutdownHook.setName("dream-shutdown");
        return shutdownHook;
    }

    public static <T extends DreamJavacordPlatform> T run(@NonNull T platform, @NonNull String[] args) {

        platform.initialize();

        Runtime.getRuntime().addShutdownHook(platform.shutdownHook(true));

        return platform;
    }

}
