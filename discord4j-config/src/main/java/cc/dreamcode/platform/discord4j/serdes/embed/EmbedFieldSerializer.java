package cc.dreamcode.platform.discord4j.serdes.embed;

import discord4j.core.spec.EmbedCreateFields;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class EmbedFieldSerializer implements ObjectSerializer<EmbedCreateFields.Field> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super EmbedCreateFields.Field> type) {
        return EmbedCreateFields.Field.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(EmbedCreateFields.@NonNull Field object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("name", object.name());
        data.add("value", object.value());
        data.add("inline", object.inline());
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public EmbedCreateFields.Field deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        String name = data.get("name", String.class);
        String value = data.get("value", String.class);
        boolean inline = data.get("inline", Boolean.class);

        return EmbedCreateFields.Field.of(name, value, inline);
    }
}
