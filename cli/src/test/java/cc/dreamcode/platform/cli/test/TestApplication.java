package cc.dreamcode.platform.cli.test;

import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.cli.DreamCliPlatform;
import cc.dreamcode.platform.component.ComponentService;
import lombok.NonNull;

import java.io.File;

public class TestApplication extends DreamCliPlatform {

    public static void main(String[] args) {
        DreamCliPlatform.run(new TestApplication());
    }

    @Override
    public void enable(@NonNull ComponentService componentService) {
        this.getDreamLogger().info("test");
    }

    @Override
    public void disable() {
        this.getDreamLogger().info("end");
    }

    @Override
    public File getDataFolder() {
        return new File("."); // unknown
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("cli-test", "1.0", "Ravis96");
    }
}
