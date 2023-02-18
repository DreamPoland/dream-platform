package cc.dreamcode.platform.bungee;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.bungee.exception.BungeePluginException;
import cc.dreamcode.platform.component.ComponentManager;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.OkaeriInjector;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DreamBungeePlatform extends Plugin implements DreamPlatform {

    @Getter private Injector injector;
    @Getter private DreamLogger dreamLogger;
    @Getter private ComponentManager componentManager;
    @Getter private final AtomicBoolean pluginDisabled = new AtomicBoolean(false);

    @Override
    public void onLoad() {
        this.injector = OkaeriInjector.create();
        this.injector.registerInjectable(this);

        this.dreamLogger = new DreamBungeeLogger(this.getLogger());
        this.injector.registerInjectable(this.dreamLogger);

        this.componentManager = new ComponentManager(this.injector);
        this.injector.registerInjectable(this.componentManager);

        try {
            this.load(this.componentManager);
        }
        catch (Exception e) {
            this.getPluginDisabled().set(true);
            throw new BungeePluginException("An error was caught when plugin are loading...", e, this);
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
            throw new BungeePluginException("An error was caught when plugin are starting...", e, this);
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
            throw new BungeePluginException("An error was caught when plugin are stopping...", e);
        }

        this.dreamLogger.info(String.format("Active version: v%s - Author: %s",
                getDescription().getVersion(),
                getDescription().getAuthor()));
    }

    public abstract void load(@NonNull ComponentManager componentManager);
    public abstract @NonNull OkaeriSerdesPack getConfigurationSerdesPack();
    public abstract @NonNull OkaeriSerdesPack getPersistenceSerdesPack();

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

    public void runAsync(@NonNull Runnable runnable) {
        this.getProxy().getScheduler().runAsync(this, runnable);
    }
}
