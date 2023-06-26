package cc.dreamcode.platform.discord4j;

import cc.dreamcode.platform.discord4j.serdes.SerdesDiscord4J;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;

public interface DreamDiscord4jConfig {

    OkaeriSerdesPack getConfigSerdesPack();

    default OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesDiscord4J());
            registry.register(this.getConfigSerdesPack());
        };
    }
}
