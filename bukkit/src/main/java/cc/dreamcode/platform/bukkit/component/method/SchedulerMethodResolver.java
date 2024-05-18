package cc.dreamcode.platform.bukkit.component.method;

import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import cc.dreamcode.platform.component.ComponentMethodResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class SchedulerMethodResolver implements ComponentMethodResolver<Scheduler> {

    private final DreamBukkitPlatform dreamBukkitPlatform;

    @Inject
    public SchedulerMethodResolver(DreamBukkitPlatform dreamBukkitPlatform) {
        this.dreamBukkitPlatform = dreamBukkitPlatform;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<Scheduler> type) {
        return Scheduler.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "scheduler-method";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Scheduler scheduler) {
        return new MapBuilder<String, Object>()
                .put("async", scheduler.async())
                .put("start-time", scheduler.delay())
                .put("interval-time", scheduler.interval())
                .build();
    }

    @Override
    public void apply(@NonNull Injector injector, @NonNull Scheduler scheduler, @NonNull Method method, @NonNull Object instance) {
        final Runnable runnable = () -> {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Cannot invoke scheduler method", e);
            }
        };

        if (scheduler.async()) {
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimerAsynchronously(this.dreamBukkitPlatform,
                    runnable, scheduler.delay(), scheduler.interval());
        }
        else {
            this.dreamBukkitPlatform.getServer().getScheduler().runTaskTimer(this.dreamBukkitPlatform,
                    runnable, scheduler.delay(), scheduler.interval());
        }
    }
}
