package cc.dreamcode.platform.javacord.serdes;

import cc.dreamcode.platform.javacord.serdes.embed.WrappedEmbedBuilderSerializer;
import cc.dreamcode.platform.javacord.serdes.embed.WrappedEmbedFieldSerializer;
import cc.dreamcode.platform.javacord.serdes.notice.NoticeSerializer;
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
