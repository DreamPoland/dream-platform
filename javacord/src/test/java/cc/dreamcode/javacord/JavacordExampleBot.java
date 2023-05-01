package cc.dreamcode.javacord;

import cc.dreamcode.javacord.command.TestCommand;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.component.ComponentManager;
import cc.dreamcode.platform.javacord.DreamJavacordPlatform;
import lombok.NonNull;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.intent.Intent;

public class JavacordExampleBot extends DreamJavacordPlatform {

    /**
     * Basic run main method.
     */
    public static void main(String[] args) {
        DreamJavacordPlatform.run(new JavacordExampleBot(), args);
    }

    @Override
    public @NonNull DiscordApi login(@NonNull ComponentManager componentManager) {
        return new DiscordApiBuilder()
                .setToken("token")
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
}
