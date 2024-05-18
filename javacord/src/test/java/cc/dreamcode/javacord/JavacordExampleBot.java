package cc.dreamcode.javacord;

import cc.dreamcode.javacord.command.TestCommand;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.component.ComponentService;
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
    public @NonNull DiscordApi login(@NonNull ComponentService componentService) {
        return new DiscordApiBuilder()
                .setToken("token")
                .addIntents(Intent.MESSAGE_CONTENT)
                .login().join();
    }

    @Override
    public void enable(@NonNull ComponentService componentService) {
        componentService.registerComponent(TestCommand.class);
    }

    @Override
    public void disable() {

    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("ExampleBot", "1.0-InDEV", "exampleAuthor");
    }
}
