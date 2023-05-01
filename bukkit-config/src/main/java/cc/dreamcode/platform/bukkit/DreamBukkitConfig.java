package cc.dreamcode.platform.bukkit;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;

public interface DreamBukkitConfig {

    default OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> registry.register(new SerdesBukkit());
    }
}
