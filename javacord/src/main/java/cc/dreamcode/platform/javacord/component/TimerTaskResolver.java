package cc.dreamcode.platform.javacord.component;

import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.platform.javacord.component.scheduler.Scheduler;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskResolver implements ComponentClassResolver<TimerTask> {
    @Override
    public boolean isAssignableFrom(@NonNull Class<TimerTask> timerTaskClass) {
        return TimerTask.class.isAssignableFrom(timerTaskClass);
    }

    @Override
    public String getComponentName() {
        return "task";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull TimerTask timerTask) {
        Scheduler scheduler = timerTask.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new PlatformException("TimerTask are not have a Scheduler annotation.");
        }

        return new MapBuilder<String, Object>()
                .put("timer-name", scheduler.timerName())
                .put("start-time", scheduler.delay())
                .put("interval-time", scheduler.interval())
                .build();
    }

    @Override
    public TimerTask resolve(@NonNull Injector injector, @NonNull Class<TimerTask> timerTaskClass) {

        final TimerTask timerTask = injector.createInstance(timerTaskClass);

        Scheduler scheduler = timerTask.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new PlatformException("TimerTask must have @Scheduler annotation.");
        }

        Timer timer = new Timer(scheduler.timerName());
        timer.scheduleAtFixedRate(timerTask, scheduler.delay(), scheduler.interval());

        return timerTask;
    }
}
