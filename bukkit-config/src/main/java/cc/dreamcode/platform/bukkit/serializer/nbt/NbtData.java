package cc.dreamcode.platform.bukkit.serializer.nbt;

import lombok.Data;

@Data
public class NbtData {
    
    private final String namespace;
    private final String key;
    private final String value;
}
