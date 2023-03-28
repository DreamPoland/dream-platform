package cc.dreamcode.javacord;

import cc.dreamcode.javacord.command.TestCommand;
import cc.dreamcode.javacord.config.TokenConfig;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.javacord.DreamJavacordPlatform;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import lombok.NonNull;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

import java.util.concurrent.atomic.AtomicReference;

public class JavacordExampleBot extends DreamJavacordPlatform {

    /**
     * Basic run main method.
     */
    public static void main(String[] args) {
        DreamJavacordPlatform.run(new JavacordExampleBot(), args);
    }

    @Override
    public @NonNull DiscordApi login(@NonNull ComponentManager componentManager) {
        final AtomicReference<String> token = new AtomicReference<>();

        componentManager.registerComponent(TokenConfig.class, tokenConfig ->
                token.set(tokenConfig.token));

        return new DiscordApiBuilder()
                .setToken(token.get())
                .addIntents(Intent.MESSAGE_CONTENT)
                .login().join();
    }

    @Override
    public void enable(@NonNull ComponentManager componentManager) {
        componentManager.registerComponent(TestCommand.class);
    }

    @Override
    public void disable() {

    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("ExampleBot", "1.0-InDEV", "exampleAuthor");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigurationSerdesPack() {
        return registry -> {

        };
    }
}
