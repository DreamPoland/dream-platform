package cc.dreamcode.platform.discord4j.serdes.embed;

import discord4j.core.spec.EmbedCreateFields;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class EmbedAuthorSerializer implements ObjectSerializer<EmbedCreateFields.Author> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super EmbedCreateFields.Author> type) {
        return EmbedCreateFields.Author.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(EmbedCreateFields.@NonNull Author object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("name", object.name());

        if (object.url() != null) {
            data.add("url", object.url());
        }

        if (object.iconUrl() != null) {
            data.add("icon-url", object.iconUrl());
        }
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public EmbedCreateFields.Author deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        String name = data.get("name", String.class);

        String url = null;
        if (data.containsKey("url")) {
            url = data.get("url", String.class);
        }

        String iconUrl = null;
        if (data.containsKey("icon-url")) {
            iconUrl = data.get("icon-url", String.class);
        }

        return EmbedCreateFields.Author.of(name, url, iconUrl);
    }
}
