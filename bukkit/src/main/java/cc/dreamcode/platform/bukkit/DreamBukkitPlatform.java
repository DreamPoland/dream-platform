package cc.dreamcode.platform.bukkit;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.component.ListenerResolver;
import cc.dreamcode.platform.bukkit.component.RunnableResolver;
import cc.dreamcode.platform.bukkit.component.method.SchedulerMethodResolver;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.exception.PlatformException;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DreamBukkitPlatform extends JavaPlugin implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentService componentService;
    @Getter private final AtomicBoolean pluginDisabled = new AtomicBoolean(false);

    @Override
    public void onLoad() {
        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);
        this.injector.registerInjectable(this.getServer());
        this.injector.registerInjectable(this.getServer().getScheduler());
        this.injector.registerInjectable(this.getServer().getPluginManager());

        this.dreamLogger = new DreamBukkitLogger(this.getLogger());
        this.injector.registerInjectable(this.dreamLogger);

        this.componentService = new ComponentService(this.injector);
        this.injector.registerInjectable(this.componentService);

        try {
            this.load(this.componentService);
        }
        catch (Exception e) {
            this.getPluginDisabled().set(true);
            throw new PlatformException("An error was caught when plugin are loading...", e);
        }
    }

    @Override
    public void onEnable() {
        if (this.getPluginDisabled().get()) {
            return;
        }

        this.componentService.registerResolver(ListenerResolver.class);
        this.componentService.registerResolver(RunnableResolver.class);
        this.componentService.registerMethodResolver(SchedulerMethodResolver.class);

        try {
            this.enable(this.componentService);
        }
        catch (Exception e) {
            this.getPluginDisabled().set(true);
            throw new PlatformException("An error was caught when plugin are starting...", e);
        }

        this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                getDescription().getVersion(),
                getDescription().getAuthors()));
    }

    @Override
    public void onDisable() {
        if (this.getPluginDisabled().get()) {
            return;
        }

        try {
            this.disable();
        }
        catch (Exception e) {
            throw new PlatformException("An error was caught when plugin are stopping...", e);
        }

        this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                getDescription().getVersion(),
                getDescription().getAuthors()));
    }

    public abstract void load(@NonNull ComponentService componentService);

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        final PluginDescriptionFile desc = this.getDescription();
        return DreamVersion.create(
                desc.getName(),
                desc.getVersion(),
                desc.getAuthors().isEmpty() ? "Unknown" : String.join(", ", desc.getAuthors())
        );
    }

    public boolean isPluginDisabled() {
        return this.pluginDisabled.get();
    }

    public void runSync(@NonNull Runnable runnable) {
        if (!this.isEnabled() || this.isPluginDisabled()) {
            return;
        }

        this.getServer().getScheduler().runTask(this, runnable);
    }

    public void runAsync(@NonNull Runnable runnable) {
        if (!this.isEnabled() || this.isPluginDisabled()) {
            return;
        }

        this.getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    public BukkitTask runLater(@NonNull Runnable runnable, long delayTicks) {
        if (!this.isEnabled() || this.isPluginDisabled()) {
            return null;
        }

        return this.getServer().getScheduler().runTaskLater(this, runnable, delayTicks);
    }

    public BukkitTask runLaterAsync(@NonNull Runnable runnable, long delayTicks) {
        if (!this.isEnabled() || this.isPluginDisabled()) {
            return null;
        }

        return this.getServer().getScheduler().runTaskLaterAsynchronously(this, runnable, delayTicks);
    }

    public BukkitTask runTimer(@NonNull Runnable runnable, long delayTicks, long periodTicks) {
        if (!this.isEnabled() || this.isPluginDisabled()) {
            return null;
        }

        return this.getServer().getScheduler().runTaskTimer(this, runnable, delayTicks, periodTicks);
    }

    public BukkitTask runTimerAsync(@NonNull Runnable runnable, long delayTicks, long periodTicks) {
        if (!this.isEnabled() || this.isPluginDisabled()) {
            return null;
        }

        return this.getServer().getScheduler().runTaskTimerAsynchronously(this, runnable, delayTicks, periodTicks);
    }
}
