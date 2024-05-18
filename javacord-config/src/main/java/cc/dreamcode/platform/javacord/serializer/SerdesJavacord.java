package cc.dreamcode.platform.javacord.serializer;

import cc.dreamcode.platform.javacord.serializer.embed.WrappedEmbedBuilderSerializer;
import cc.dreamcode.platform.javacord.serializer.embed.WrappedEmbedFieldSerializer;
import cc.dreamcode.platform.javacord.serializer.notice.NoticeSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;

public class SerdesJavacord implements OkaeriSerdesPack {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new WrappedEmbedBuilderSerializer());
        registry.register(new WrappedEmbedFieldSerializer());
        registry.register(new ColorSerializer());
        registry.register(new NoticeSerializer());
    }
}
