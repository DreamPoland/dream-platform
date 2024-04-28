package cc.dreamcode.platform.bukkit.serializer.nbt;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class NbtDataSerializer implements ObjectSerializer<NbtData> {
    @Override
    public boolean supports(@NonNull Class<? super NbtData> type) {
        return NbtData.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull NbtData object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("nbt-namespace", object.getNamespace());
        data.add("nbt-key", object.getKey());
        data.add("nbt-value", object.getValue());
    }

    @Override
    public NbtData deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new NbtData(
                data.get("nbt-namespace", String.class),
                data.get("nbt-key", String.class),
                data.get("nbt-value", String.class)
        );
    }
}
