package cc.dreamcode.platform.bungee;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bungee.component.ListenerResolver;
import cc.dreamcode.platform.bungee.component.RunnableResolver;
import cc.dreamcode.platform.bungee.component.method.SchedulerMethodResolver;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.exception.PlatformException;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DreamBungeePlatform extends Plugin implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentService componentService;
    @Getter private final AtomicBoolean pluginDisabled = new AtomicBoolean(false);

    @Override
    public void onLoad() {
        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);
        this.injector.registerInjectable(this.injector);
        this.injector.registerInjectable(this.getProxy());
        this.injector.registerInjectable(this.getProxy().getScheduler());
        this.injector.registerInjectable(this.getProxy().getPluginManager());

        this.dreamLogger = new DreamBungeeLogger(this.getLogger());
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
                getDescription().getAuthor()));
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
                getDescription().getAuthor()));
    }

    public abstract void load(@NonNull ComponentService componentService);

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        final PluginDescription desc = this.getDescription();
        return DreamVersion.create(
                desc.getName(),
                desc.getVersion(),
                desc.getAuthor() != null ? desc.getAuthor() : "Unknown"
        );
    }

    public boolean isPluginDisabled() {
        return this.pluginDisabled.get();
    }

    public void runAsync(@NonNull Runnable runnable) {
        if (this.isPluginDisabled()) {
            return;
        }

        this.getProxy().getScheduler().runAsync(this, runnable);
    }

    public ScheduledTask runLater(@NonNull Runnable runnable, long delay, @NonNull TimeUnit unit) {
        if (this.isPluginDisabled()) {
            return null;
        }

        return this.getProxy().getScheduler().schedule(this, runnable, delay, unit);
    }

    public ScheduledTask runTimer(@NonNull Runnable runnable, long delay, long period, @NonNull TimeUnit unit) {
        if (this.isPluginDisabled()) {
            return null;
        }

        return this.getProxy().getScheduler().schedule(this, runnable, delay, period, unit);
    }
}
