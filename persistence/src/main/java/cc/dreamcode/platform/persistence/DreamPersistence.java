package cc.dreamcode.platform.persistence;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;

public interface DreamPersistence {

    default OkaeriSerdesPack getPersistenceSerdesPack() {
        return registry -> {};
    }
}
