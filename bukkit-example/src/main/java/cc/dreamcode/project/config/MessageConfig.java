package cc.dreamcode.project.config;

import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;

@Configuration(child = "message.yml")
public class MessageConfig extends OkaeriConfig {

    @CustomKey("command-usage")
    public BukkitNotice usage = BukkitNotice.chat("&7Command usage examples: &c{label}");
    @CustomKey("command-usage-help")
    public BukkitNotice usagePath = BukkitNotice.chat("&f{usage} &8- &7{description}");

    @CustomKey("command-usage-not-found")
    public BukkitNotice usageNotFound = BukkitNotice.chat("&cNo matching commands found.");
    @CustomKey("command-path-not-found")
    public BukkitNotice pathNotFound = BukkitNotice.chat("&cThis command is empty or you do not have access to it.");
    @CustomKey("command-no-permission")
    public BukkitNotice noPermission = BukkitNotice.chat("&cYou do not have permission.");
    @CustomKey("command-not-player")
    public BukkitNotice notPlayer = BukkitNotice.chat("&cThis command can only be executed by a player.");
    @CustomKey("command-not-console")
    public BukkitNotice notConsole = BukkitNotice.chat("&cThis command can only be executed from the console.");
    @CustomKey("command-invalid-format")
    public BukkitNotice invalidFormat = BukkitNotice.chat("&cInvalid argument format provided. ({input})");

    @CustomKey("player-not-found")
    public BukkitNotice playerNotFound = BukkitNotice.chat("&cPlayer not found.");
    @CustomKey("world-not-found")
    public BukkitNotice worldNotFound = BukkitNotice.chat("&cWorld not found.");

    @Comment("Message sent when points are added. Placeholders: {player}, {amount}")
    @CustomKey("points-added-notice")
    public BukkitNotice pointsAddedNotice = BukkitNotice.chat("&aAdded &e{amount} &acoins to &f{player}&a.");

    @Comment("Message when player is not found")
    @CustomKey("user-not-found-notice")
    public BukkitNotice userNotFoundNotice = BukkitNotice.chat("&cPlayer not found!");

    @Comment("Join message. Placeholders: {player}")
    @CustomKey("join-notice")
    public BukkitNotice joinNotice = BukkitNotice.chat("&7Player &f{player} &7joined the server.");

    @Comment("Message to annouce")
    @CustomKey("announce-notice")
    public BukkitNotice announceNotice = BukkitNotice.chat("&cTest message!");
}
