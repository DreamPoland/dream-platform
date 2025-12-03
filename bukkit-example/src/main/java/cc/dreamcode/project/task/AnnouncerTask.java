package cc.dreamcode.project.task;

import cc.dreamcode.platform.bukkit.component.scheduler.Scheduler;
import cc.dreamcode.project.config.MessageConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@Scheduler(delay = 1200L, interval = 6000L) // Delay: 1 min, Interval: 5 min (in ticks)
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class AnnouncerTask implements Runnable {

    private final MessageConfig messageConfig;

    @Override
    public void run() {
        // Logic: Send announcement to all players
        Bukkit.getOnlinePlayers().forEach(player -> {
            this.messageConfig.announceNotice.send(player);
        });
    }
}