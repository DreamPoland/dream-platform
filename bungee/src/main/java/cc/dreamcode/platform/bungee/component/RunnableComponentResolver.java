package cc.dreamcode.platform.bungee.component;

import cc.dreamcode.platform.bungee.DreamBungeePlatform;
import cc.dreamcode.platform.bungee.component.scheduler.Scheduler;
import cc.dreamcode.platform.bungee.exception.BungeePluginException;
import cc.dreamcode.platform.component.ComponentClassResolver;
import com.google.common.collect.ImmutableMap;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RunnableComponentResolver extends ComponentClassResolver<Class<? extends Runnable>> {

    private final DreamBungeePlatform dreamBukkitPlatform;

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
            throw new BungeePluginException("Runnable are not have a Scheduler annotation.");
        }

        return new ImmutableMap.Builder<String, Object>()
                .put("start-time", scheduler.delay())
                .put("interval-time", scheduler.interval())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<? extends Runnable> runnableClass) {
        final Runnable runnable = injector.createInstance(runnableClass);

        Scheduler scheduler = runnable.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new BungeePluginException("Runnable are not have a Scheduler annotation.");
        }

        this.dreamBukkitPlatform.getProxy().getScheduler().schedule(this.dreamBukkitPlatform,
                runnable, scheduler.delay() * 50, scheduler.interval() * 50, TimeUnit.MILLISECONDS);

        return runnable;
    }

}
