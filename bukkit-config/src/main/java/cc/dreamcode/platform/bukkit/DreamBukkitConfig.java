package cc.dreamcode.platform.bukkit;

import cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import org.bukkit.inventory.meta.ItemMeta;

public interface DreamBukkitConfig {

    default OkaeriSerdesPack getConfigSerdesPack() {
        return registry -> {
            registry.register(new SerdesBukkit());
            registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
        };
    }
}
