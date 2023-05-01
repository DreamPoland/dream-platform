package cc.dreamcode.platform.cli;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;

public interface DreamCliConfig {

    default OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {};
    }
}
