package cc.dreamcode.platform.discord4j.component.command;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import lombok.NonNull;

public interface DiscordCommand {

    ApplicationCommandRequest commandRequest();

    /**
     * When value is 0, command will be global.
     */
    default long guildId() {
        return 0L;
    }

    void content(@NonNull ChatInputInteractionEvent event);

}
