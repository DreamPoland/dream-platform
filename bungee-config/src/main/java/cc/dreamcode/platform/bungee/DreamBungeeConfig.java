package cc.dreamcode.platform.bungee;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBungee;

public interface DreamBungeeConfig {

    default OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> registry.register(new SerdesBungee());
    }
}
