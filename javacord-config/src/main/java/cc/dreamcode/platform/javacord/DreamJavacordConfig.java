package cc.dreamcode.platform.javacord;

import cc.dreamcode.platform.javacord.serdes.SerdesJavacord;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;

public interface DreamJavacordConfig {

    default OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> registry.register(new SerdesJavacord());
    }
}
