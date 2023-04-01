package cc.dreamcode.platform.javacord.serdes;

import cc.dreamcode.platform.javacord.serdes.embed.WrappedEmbedBuilderSerdes;
import cc.dreamcode.platform.javacord.serdes.embed.WrappedEmbedFieldSerdes;
import cc.dreamcode.platform.javacord.serdes.notice.NoticeSerdes;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;

public class SerdesJavacord implements OkaeriSerdesPack {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new WrappedEmbedBuilderSerdes());
        registry.register(new WrappedEmbedFieldSerdes());
        registry.register(new ColorSerializer());
        registry.register(new NoticeSerdes());
    }
}
