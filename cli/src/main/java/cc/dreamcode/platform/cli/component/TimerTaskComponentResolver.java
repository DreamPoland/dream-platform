package cc.dreamcode.platform.cli.component;

import cc.dreamcode.platform.cli.component.scheduler.Scheduler;
import cc.dreamcode.platform.cli.exception.CliPlatformException;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import lombok.NonNull;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTaskComponentResolver extends ComponentClassResolver<Class<TimerTask>> {

    @Override
    public boolean isAssignableFrom(@NonNull Class<TimerTask> timerTaskClass) {
        return TimerTask.class.isAssignableFrom(timerTaskClass);
    }

    @Override
    public String getComponentName() {
        return "task";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<TimerTask> timerTaskClass) {
        Scheduler scheduler = timerTaskClass.getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new CliPlatformException("TimerTask are not have a Scheduler annotation.");
        }

        return new MapBuilder<String, Object>()
                .put("timer-name", scheduler.timerName())
                .put("start-time", scheduler.delay())
                .put("interval-time", scheduler.interval())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<TimerTask> timerTaskClass) {
        final TimerTask timerTask = injector.createInstance(timerTaskClass);

        Scheduler scheduler = timerTask.getClass().getAnnotation(Scheduler.class);
        if (scheduler == null) {
            throw new CliPlatformException("TimerTask are not have a Scheduler annotation.");
        }

        Timer timer = new Timer(scheduler.timerName());
        timer.scheduleAtFixedRate(timerTask, scheduler.delay(), scheduler.interval());

        return timerTask;
    }
}
