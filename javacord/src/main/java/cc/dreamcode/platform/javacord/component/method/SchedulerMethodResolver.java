package cc.dreamcode.platform.javacord.component.method;

import cc.dreamcode.platform.component.ComponentMethodResolver;
import cc.dreamcode.platform.javacord.component.scheduler.Scheduler;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SchedulerMethodResolver implements ComponentMethodResolver<Scheduler> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<Scheduler> type) {
        return Scheduler.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "scheduler";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Scheduler scheduler) {
        return new MapBuilder<String, Object>()
                .put("start-time", scheduler.delay())
                .put("interval-time", scheduler.interval())
                .build();
    }

    @Override
    public void apply(@NonNull Injector injector, @NonNull Scheduler scheduler, @NonNull Method method, @NonNull Object instance) {

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Cannot invoke scheduler method", e);
                }
            }
        };

        Timer timer = new Timer(scheduler.timerName());
        timer.scheduleAtFixedRate(timerTask, scheduler.delay(), scheduler.interval());
    }
}
