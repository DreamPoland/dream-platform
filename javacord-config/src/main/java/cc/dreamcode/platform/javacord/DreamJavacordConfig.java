package cc.dreamcode.platform.javacord;

import cc.dreamcode.platform.javacord.serdes.SerdesJavacord;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;

public interface DreamJavacordConfig {
    OkaeriSerdesPack getConfigSerdesPack();

    default OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesJavacord());
            registry.register(this.getConfigSerdesPack());
        };
    }
}
