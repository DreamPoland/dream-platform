package cc.dreamcode.platform.discord4j.component;

import cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.discord4j.component.command.DiscordCommand;
import cc.dreamcode.utilities.builder.MapBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputAutoCompleteEvent;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandOptionChoiceData;
import discord4j.discordjson.json.ApplicationCommandOptionData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CommandComponentResolver extends ComponentClassResolver<Class<DiscordCommand>> {

    private @Inject GatewayDiscordClient discordClient;
    private @Inject DreamLogger dreamLogger;

    @Override
    public boolean isAssignableFrom(@NonNull Class<DiscordCommand> discordCommandClass) {
        return DiscordCommand.class.isAssignableFrom(discordCommandClass);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<DiscordCommand> discordCommandClass) {
        final DiscordCommand discordCommand = injector.createInstance(discordCommandClass);
        final ApplicationCommandRequest request = discordCommand.commandRequest();

        return new MapBuilder<String, Object>()
                .put("name", request.name())
                .put("description", request.description().toOptional().orElse("?"))
                .put("options", request.options().toOptional()
                        .map(list -> list.stream().map(ApplicationCommandOptionData::name).collect(Collectors.joining(", ")))
                        .orElse("-"))
                .put("guild", discordCommand.guildId() == 0L ? "global" : discordCommand.guildId())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<DiscordCommand> discordCommandClass) {
        final DiscordCommand discordCommand = injector.createInstance(discordCommandClass);

        Optional<Long> applicationId = this.discordClient.getRestClient().getApplicationId().blockOptional();
        if (!applicationId.isPresent()) {
            throw new NullPointerException("ApplicationId from rest-client is not found.");
        }

        if (discordCommand.guildId() == 0L) {
            this.discordClient.getRestClient().getApplicationService()
                    .createGlobalApplicationCommand(applicationId.get(), discordCommand.commandRequest())
                    .subscribe();
        }
        else {
            this.discordClient.getRestClient().getApplicationService()
                    .createGuildApplicationCommand(applicationId.get(), discordCommand.guildId(), discordCommand.commandRequest())
                    .subscribe();
        }

        this.discordClient.on(ChatInputInteractionEvent.class).subscribe(event -> {
            if (event.getCommandName().equals(discordCommand.commandRequest().name())) {
                discordCommand.content(event);
            }
        });

        this.discordClient.on(ChatInputAutoCompleteEvent.class).subscribe(event -> {
            if (event.getCommandName().equals(discordCommand.commandRequest().name())) {
                List<ApplicationCommandOptionChoiceData> suggestion = discordCommand.autoComplete(event);
                if (suggestion == null || suggestion.isEmpty()) {
                    return;
                }

                event.respondWithSuggestions(suggestion);
            }
        });

        return discordCommand;
    }
}
