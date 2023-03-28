package cc.dreamcode.platform.javacord.component;

import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.javacord.DreamJavacordPlatform;
import cc.dreamcode.platform.javacord.component.command.JavacordCommand;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.javacord.api.DiscordApi;

import java.util.Map;

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
        this.discordApi.addSlashCommandCreateListener(javacordCommand.respond());
        this.discordApi.addAutocompleteCreateListener(javacordCommand.autocomplete());

        return javacordCommand;
    }
}
