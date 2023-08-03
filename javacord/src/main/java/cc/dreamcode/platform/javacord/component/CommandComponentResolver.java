package cc.dreamcode.platform.javacord.component;

import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.javacord.DreamJavacordPlatform;
import cc.dreamcode.platform.javacord.component.command.JavacordCommand;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.util.Map;

@RequiredArgsConstructor
public class CommandComponentResolver extends ComponentClassResolver<Class<JavacordCommand>> {

    private @Inject DreamJavacordPlatform dreamJavacordPlatform;
    private @Inject DiscordApi discordApi;

    @Override
    public boolean isAssignableFrom(@NonNull Class<JavacordCommand> javacordCommandClass) {
        return JavacordCommand.class.isAssignableFrom(javacordCommandClass);
    }

    @Override
    public String getComponentName() {
        return "command";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<JavacordCommand> javacordCommandClass) {
        final JavacordCommand javacordCommand = injector.createInstance(javacordCommandClass);

        return new MapBuilder<String, Object>()
                .put("name", javacordCommand.getName())
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<JavacordCommand> javacordCommandClass) {
        final JavacordCommand javacordCommand = injector.createInstance(javacordCommandClass);

        this.dreamJavacordPlatform.getJavacordCommandList().add(javacordCommand);

        this.discordApi.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction interaction = event.getSlashCommandInteraction();
            if (interaction.getCommandName().equalsIgnoreCase(javacordCommand.getName())) {
                javacordCommand.respond().onSlashCommandCreate(event);
            }
        });

        this.discordApi.addAutocompleteCreateListener(event -> {
            SlashCommandInteraction interaction = event.getAutocompleteInteraction();
            if (interaction.getCommandName().equalsIgnoreCase(javacordCommand.getName())) {
                javacordCommand.autocomplete().onAutocompleteCreate(event);
            }
        });

        return javacordCommand;
    }
}
