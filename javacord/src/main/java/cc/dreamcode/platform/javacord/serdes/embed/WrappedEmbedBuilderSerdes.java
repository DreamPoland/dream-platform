package cc.dreamcode.platform.javacord.serdes.embed;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

import java.awt.*;
import java.time.Instant;

public class WrappedEmbedBuilderSerdes implements ObjectSerializer<WrappedEmbedBuilder> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super WrappedEmbedBuilder> type) {
        return WrappedEmbedBuilder.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(@NonNull WrappedEmbedBuilder object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        if (object.getTitle() != null) {
            data.add("embed-title", object.getTitle());
        }

        if (object.getDescription() != null) {
            data.add("embed-description", object.getDescription());
        }

        if (object.getUrl() != null) {
            data.add("embed-url", object.getUrl());
        }

        if (object.getTimestamp() != null) {
            data.add("embed-timestamp", object.getTimestamp());
        }

        if (object.getColor() != null) {
            data.add("embed-color", object.getColor());
        }

        if (object.getFooterText() != null) {
            data.add("embed-footer-text", object.getFooterText());

            if (object.getFooterIconUrl() != null) {
                data.add("embed-footer-icon", object.getFooterIconUrl());
            }
        }

        if (object.getImageUrl() != null) {
            data.add("embed-image", object.getImageUrl());
        }

        if (object.getAuthorName() != null) {
            data.add("embed-author-name", object.getAuthorName());
            data.add("embed-author-url", object.getAuthorUrl());
            data.add("embed-author-icon", object.getAuthorIconUrl());
        }

        if (object.getThumbnailUrl() != null) {
            data.add("embed-thumbnail", object.getThumbnailUrl());
        }

        if (!object.getFields().isEmpty()) {
            data.add("embed-fields", object.getFields());
        }
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public WrappedEmbedBuilder deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        WrappedEmbedBuilder wrappedEmbedBuilder = new WrappedEmbedBuilder();

        if (data.containsKey("embed-title")) {
            wrappedEmbedBuilder.setTitle(data.get("embed-title", String.class));
        }

        if (data.containsKey("embed-description")) {
            wrappedEmbedBuilder.setDescription(data.get("embed-description", String.class));
        }

        if (data.containsKey("embed-url")) {
            wrappedEmbedBuilder.setUrl(data.get("embed-url", String.class));
        }

        if (data.containsKey("embed-timestamp")) {
            wrappedEmbedBuilder.setTimestamp(data.get("embed-timestamp", Instant.class));
        }

        if (data.containsKey("embed-color")) {
            wrappedEmbedBuilder.setColor(data.get("embed-color", Color.class));
        }

        if (data.containsKey("embed-footer-text")) {
            if (data.containsKey("embed-footer-icon")) {
                wrappedEmbedBuilder.setFooter(
                        data.get("embed-footer-text", String.class),
                        data.get("embed-footer-icon", String.class)
                );
            }
            else {
                wrappedEmbedBuilder.setFooter(
                        data.get("embed-footer-text", String.class)
                );
            }
        }

        if (data.containsKey("embed-image")) {
            wrappedEmbedBuilder.setImage(data.get("embed-image", String.class));
        }

        if (data.containsKey("embed-author-name")) {
            wrappedEmbedBuilder.setAuthor(
                    data.get("embed-author-name", String.class),
                    data.get("embed-author-url", String.class),
                    data.get("embed-author-icon", String.class)
            );
        }

        if (data.containsKey("embed-thumbnail")) {
            wrappedEmbedBuilder.setThumbnail(data.get("embed-thumbnail", String.class));
        }

        if (data.containsKey("embed-fields")) {
            data.getAsList("embed-fields", WrappedEmbedField.class).forEach(wrappedEmbedField ->
                    wrappedEmbedBuilder.addField(wrappedEmbedField.getName(), wrappedEmbedField.getValue(), wrappedEmbedField.isInline()));
        }

        return wrappedEmbedBuilder;
    }
}
