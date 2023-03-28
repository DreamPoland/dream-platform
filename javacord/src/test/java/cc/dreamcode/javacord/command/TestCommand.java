package cc.dreamcode.javacord.command;

import cc.dreamcode.platform.javacord.component.command.JavacordCommand;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

public class TestCommand extends JavacordCommand {

    private @Inject DiscordApi discordApi;

    protected TestCommand() {
        super("ping", "Ping pong!");
    }

    @Override
    public @NonNull SlashCommandCreateListener respond() {
        return event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            InteractionImmediateResponseBuilder responder = interaction.createImmediateResponder();

            responder.setFlags(MessageFlag.EPHEMERAL);
            responder.setContent("Pong!");

            responder.respond();
        };
    }
}
