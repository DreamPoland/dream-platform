package cc.dreamcode.project.controller;

import cc.dreamcode.project.config.MessageConfig;
import cc.dreamcode.project.profile.Profile;
import cc.dreamcode.project.profile.ProfileService;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayerController implements Listener {

    private final ProfileService profileService;
    private final MessageConfig messageConfig;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        final Profile profile = this.profileService.findOrCreate(event.getPlayer());

        // Send welcome message using Dream-Notice
        this.messageConfig.joinNotice.send(event.getPlayer(), new MapBuilder<String, Object>()
                .put("player", profile.getName())
                .build());
    }
}