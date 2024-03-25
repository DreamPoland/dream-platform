package cc.dreamcode.platform.bungee.component;

import cc.dreamcode.platform.bungee.DreamBungeePlatform;
import cc.dreamcode.platform.bungee.component.scheduler.Scheduler;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RunnableComponentResolver implements ComponentClassResolver<Runnable> {

    private final DreamBungeePlatform dreamBukkitPlatform;

    @Inject
    public RunnableComponentResolver(DreamBungeePlatform dreamBukkitPlatform) {
        this.dreamBukkitPlatform = dreamBukkitPlatform;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<Runnable> type) {
        return Runnable.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "task";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Runnable runnable) {
        Scheduler scheduler = runnable.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new PlatformException("Runnable must have @Scheduler annotation.");
        }

        return new MapBuilder<String, Object>()
                .put("async", scheduler.async())
                .put("start-time", scheduler.delay())
                .put("interval-time", scheduler.interval())
                .build();
    }

    @Override
    public Runnable resolve(@NonNull Injector injector, @NonNull Class<Runnable> type) {

        final Runnable runnable = injector.createInstance(type);

        Scheduler scheduler = runnable.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new PlatformException("Runnable must have @Scheduler annotation.");
        }

        if (scheduler.async()) {
            this.dreamBukkitPlatform.getProxy().getScheduler().schedule(
                    this.dreamBukkitPlatform,
                    () -> this.dreamBukkitPlatform.runAsync(runnable),
                    scheduler.delay() * 50,
                    scheduler.interval() * 50,
                    TimeUnit.MILLISECONDS
            );
        }
        else {
            this.dreamBukkitPlatform.getProxy().getScheduler().schedule(
                    this.dreamBukkitPlatform,
                    runnable,
                    scheduler.delay() * 50,
                    scheduler.interval() * 50,
                    TimeUnit.MILLISECONDS
            );
        }

        return runnable;
    }
}
