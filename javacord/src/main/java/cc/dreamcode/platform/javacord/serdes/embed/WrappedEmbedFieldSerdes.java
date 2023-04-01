package cc.dreamcode.platform.javacord.serdes.embed;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class WrappedEmbedFieldSerdes implements ObjectSerializer<WrappedEmbedField> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super WrappedEmbedField> type) {
        return WrappedEmbedField.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(@NonNull WrappedEmbedField object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("field-name", object.getName());
        data.add("field-value", object.getValue());
        data.add("field-inline", object.isInline());
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public WrappedEmbedField deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new WrappedEmbedField(
                data.get("field-name", String.class),
                data.get("field-value", String.class),
                data.get("field-inline", Boolean.class)
        );
    }
}
