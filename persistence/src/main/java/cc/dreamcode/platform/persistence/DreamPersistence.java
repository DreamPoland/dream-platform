package cc.dreamcode.platform.persistence;

import eu.okaeri.configs.serdes.OkaeriSerdes;

public interface DreamPersistence {

    default OkaeriSerdes getPersistenceSerdesPack() {
        return registry -> {};
    }
}
