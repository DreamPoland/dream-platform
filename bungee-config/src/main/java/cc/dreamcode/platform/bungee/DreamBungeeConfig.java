package cc.dreamcode.platform.bungee;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBungee;

public interface DreamBungeeConfig {

    OkaeriSerdesPack getConfigSerdesPack();

    default OkaeriSerdesPack getSerdesPack() {
        return registry -> {
            registry.register(new SerdesBungee());
            registry.register(this.getConfigSerdesPack());
        };
    }
}
