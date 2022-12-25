package cc.dreamcode.platform.discord4j.serdes;

import cc.dreamcode.platform.discord4j.serdes.embed.EmbedAuthorSerializer;
import cc.dreamcode.platform.discord4j.serdes.embed.EmbedFieldSerializer;
import cc.dreamcode.platform.discord4j.serdes.embed.EmbedFooterSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;

public class SerdesDiscord4J implements OkaeriSerdesPack {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new ColorSerializer());
        registry.register(new SnowflakeTransformer());
        registry.register(new EmbedAuthorSerializer());
        registry.register(new EmbedFieldSerializer());
        registry.register(new EmbedFooterSerializer());
        registry.register(new EmbedCreateSpecSerializer());
    }
}
