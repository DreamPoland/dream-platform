package cc.dreamcode.platform.cli.test;

import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.cli.DreamCliPlatform;
import cc.dreamcode.platform.component.ComponentManager;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import lombok.NonNull;

public class TestApplication extends DreamCliPlatform {

    public static void main(String[] args) {
        DreamCliPlatform.run(new TestApplication());
    }

    @Override
    public void enable(@NonNull ComponentManager componentManager) {
        this.getDreamLogger().info("test");
    }

    @Override
    public void disable() {
        this.getDreamLogger().info("end");
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("cli-test", "1.0", "Ravis96");
    }

    @Override
    public @NonNull OkaeriSerdesPack getConfigurationSerdesPack() {
        return registry -> {

        };
    }

    @Override
    public @NonNull OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {

        };
    }
}
