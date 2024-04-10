package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class RunnableResolver implements ComponentClassResolver<Runnable> {

    private final DreamBukkitPlatform dreamBukkitPlatform;

    @Inject
    public RunnableResolver(DreamBukkitPlatform dreamBukkitPlatform) {
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
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimerAsynchronously(this.dreamBukkitPlatform,
                    runnable, scheduler.delay(), scheduler.interval());
        }
        else {
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimer(this.dreamBukkitPlatform,
                    runnable, scheduler.delay(), scheduler.interval());
        }

        return runnable;
    }
}
