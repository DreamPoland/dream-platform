package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.annotation.Scheduler;
import cc.dreamcode.platform.bukkit.exception.BukkitPluginException;
import cc.dreamcode.platform.component.ComponentClassResolver;
import com.google.common.collect.ImmutableMap;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.Map;

public class RunnableComponentClassResolver extends ComponentClassResolver<Class<? extends Runnable>> {

    private @Inject DreamBukkitPlatform dreamBukkitPlatform;

    @Override
    public boolean isAssignableFrom(@NonNull Class<? extends Runnable> runnableClass) {
        return Runnable.class.isAssignableFrom(runnableClass);
    }

    @Override
    public String getComponentName() {
        return "runnable";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<? extends Runnable> runnableClass) {
        Scheduler scheduler = runnableClass.getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new BukkitPluginException("Runnable are not have a Scheduler annotation.");
        }

        return new ImmutableMap.Builder<String, Object>()
                .put("async", scheduler.async())
                .put("start-time", scheduler.delay())
                .put("interval-time", scheduler.interval())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<? extends Runnable> runnableClass) {
        final Runnable runnable = injector.createInstance(runnableClass);

        Scheduler scheduler = runnable.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new BukkitPluginException("Runnable are not have a Scheduler annotation.");
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
