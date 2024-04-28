package cc.dreamcode.platform.bukkit;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.exception.PlatformException;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DreamBukkitPlatform extends JavaPlugin implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentManager componentManager;
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

        this.componentManager = new ComponentManager(this.injector);
        this.injector.registerInjectable(this.componentManager);

        try {
            this.load(this.componentManager);
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

        try {
            this.enable(this.componentManager);
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

    public abstract void load(@NonNull ComponentManager componentManager);

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

    public void runAsync(@NonNull Runnable runnable) {
        if (!this.isEnabled() || this.getPluginDisabled().get()) {
            return;
        }

        this.getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }
}
