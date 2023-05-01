package cc.dreamcode.platform.discord4j.serdes;

import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

import java.time.Instant;

public class EmbedCreateSpecSerializer implements ObjectSerializer<EmbedCreateSpec> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super EmbedCreateSpec> type) {
        return EmbedCreateSpec.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(@NonNull EmbedCreateSpec object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        if (object.color().isAbsent()) {
            data.add("color", object.color().get());
        }

        if (object.title().isAbsent()) {
            data.add("title", object.title().get());
        }

        if (object.url().isAbsent()) {
            data.add("url", object.url().get());
        }

        if (object.author() != null) {
            data.add("author", object.author());
        }

        if (object.description().isAbsent()) {
            data.add("description", object.description().get());
        }

        if (object.thumbnail().isAbsent()) {
            data.add("thumbnail", object.thumbnail().get());
        }

        if (!object.fields().isEmpty()) {
            data.add("fields", object.fields());
        }

        if (object.image().isAbsent()) {
            data.add("image", object.image().get());
        }

        if (object.timestamp().isAbsent()) {
            data.add("timestamp", object.timestamp().get());
        }

        if (object.footer() != null) {
            data.add("footer", object.footer());
        }
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public EmbedCreateSpec deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        EmbedCreateSpec.Builder builder = EmbedCreateSpec.builder();

        if (data.containsKey("color")) {
            builder.color(data.get("color", Color.class));
        }

        if (data.containsKey("title")) {
            builder.title(data.get("title", String.class));
        }

        if (data.containsKey("url")) {
            builder.url(data.get("url", String.class));
        }

        if (data.containsKey("author")) {
            builder.author(data.get("author", EmbedCreateFields.Author.class));
        }

        if (data.containsKey("description")) {
            builder.description(data.get("description", String.class));
        }

        if (data.containsKey("thumbnail")) {
            builder.thumbnail(data.get("thumbnail", String.class));
        }

        if (data.containsKey("fields")) {
            builder.fields(data.getAsList("fields", EmbedCreateFields.Field.class));
        }

        if (data.containsKey("image")) {
            builder.image(data.get("image", String.class));
        }

        if (data.containsKey("timestamp")) {
            builder.timestamp(data.get("timestamp", Instant.class));
        }

        if (data.containsKey("footer")) {
            builder.footer(data.get("footer", EmbedCreateFields.Footer.class));
        }

        return builder.build();
    }
}
