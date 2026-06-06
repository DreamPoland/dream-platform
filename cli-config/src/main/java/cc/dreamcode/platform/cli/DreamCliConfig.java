package cc.dreamcode.platform.cli;

import eu.okaeri.configs.serdes.OkaeriSerdes;

public interface DreamCliConfig {

    default OkaeriSerdes getConfigSerdesPack() {
        return registry -> {};
    }
}
