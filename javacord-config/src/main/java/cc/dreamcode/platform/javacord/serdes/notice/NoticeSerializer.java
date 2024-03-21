package cc.dreamcode.platform.javacord.serdes.notice;

import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.platform.javacord.embed.WrappedEmbedBuilder;
import cc.dreamcode.platform.javacord.notice.Notice;
import cc.dreamcode.platform.javacord.notice.NoticeType;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class NoticeSerializer implements ObjectSerializer<Notice> {
    /**
     * @param type the type checked for compatibility
     * @return {@code true} if serializer is able to process the {@code type}
     */
    @Override
    public boolean supports(@NonNull Class<? super Notice> type) {
        return Notice.class.isAssignableFrom(type);
    }

    /**
     * @param object   the object to be serialized
     * @param data     the serialization data
     * @param generics the generic information about the {@code object}
     */
    @Override
    public void serialize(@NonNull Notice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getNoticeType());
        data.add("value", object.getValue());
    }

    /**
     * @param data     the source deserialization data
     * @param generics the target generic type for the {@code data}
     * @return the deserialized object
     */
    @Override
    public Notice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        final NoticeType noticeType = data.get("type", NoticeType.class);

        if (noticeType.equals(NoticeType.MESSAGE)) {
            return new Notice(
                    noticeType,
                    data.get("value", String.class)
            );
        }

        if (noticeType.equals(NoticeType.EMBED)) {
            return new Notice(
                    noticeType,
                    data.get("value", WrappedEmbedBuilder.class)
            );
        }

        throw new PlatformException("Cannot resolve unknown notice-type: " + noticeType);
    }
}
