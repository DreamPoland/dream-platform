package cc.dreamcode.project.command;

import cc.dreamcode.command.CommandBase;
import cc.dreamcode.command.annotation.Arg;
import cc.dreamcode.command.annotation.Command;
import cc.dreamcode.command.annotation.Completion;
import cc.dreamcode.command.annotation.Executor;
import cc.dreamcode.project.config.MessageConfig;
import cc.dreamcode.project.menu.ExampleMenuHolder;
import cc.dreamcode.project.profile.Profile;
import cc.dreamcode.project.profile.ProfileCache;
import cc.dreamcode.project.profile.ProfileService;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.annotation.Inject;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@Command(name = "points", aliases = {"p", "coins"})
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PointsCommand implements CommandBase {

    private final ProfileCache profileCache;
    private final ProfileService profileService;
    private final MessageConfig messageConfig;
    private final ExampleMenuHolder menuHolder;

    @Executor(path = "menu", description = "Opens menu.")
    void menu(Player player) {
        // opens prebuild menu
        this.menuHolder.open(player);
    }

    @Executor(path = "add", description = "Adding some points.")
    @Completion(arg = "playerName", value = "@players")
    void addPoints(CommandSender sender, @Arg String playerName, @Arg int amount) {

        Optional<Profile> profileOptional = this.profileCache.findProfileByName(playerName);

        if (!profileOptional.isPresent()) {
            // Sending simple notice without placeholders
            this.messageConfig.userNotFoundNotice.send(sender);
            return;
        }

        Profile profile = profileOptional.get();
        this.profileService.addPoints(profile, amount);

        // Sending notice with placeholders using MapBuilder
        this.messageConfig.pointsAddedNotice.send(sender, new MapBuilder<String, Object>()
                .put("player", playerName)
                .put("amount", amount)
                .build());
    }
}
