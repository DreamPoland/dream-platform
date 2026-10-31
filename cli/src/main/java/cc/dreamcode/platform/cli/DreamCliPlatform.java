package cc.dreamcode.platform.cli;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.cli.component.TimerTaskResolver;
import cc.dreamcode.platform.cli.component.method.SchedulerMethodResolver;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.exception.PlatformException;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class DreamCliPlatform implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentService componentService;
    @Getter private final File dataFolder = new File(".");

    private void initialize() {
        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);

        this.dreamLogger = new DreamCliLogger(LoggerFactory.getLogger(this.getClass()));
        this.injector.registerInjectable(this.dreamLogger);

        this.componentService = new ComponentService(this.injector);
        this.injector.registerInjectable(this.componentService);

        DreamVersion dreamVersion = this.getDreamVersion();

        this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                dreamVersion.getVersion(),
                dreamVersion.getAuthor()));
        this.dreamLogger.info(String.format("Loading %s resources...",
                dreamVersion.getName()));

        this.componentService.registerResolver(TimerTaskResolver.class);
        this.componentService.registerMethodResolver(SchedulerMethodResolver.class);

        try {
            this.enable(this.componentService);
        }
        catch (Exception e) {
            throw new PlatformException("An error was caught when platform are starting...", e);
        }

        Thread shutdownHook = this.getShutdownHook(dreamVersion);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    private Thread getShutdownHook(DreamVersion dreamVersion) {
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
        return shutdownHook;
    }

    public static void run(@NonNull DreamCliPlatform dreamCliPlatform) {
        dreamCliPlatform.initialize();
    }
}
