package cc.dreamcode.platform.discord4j.serdes;

import discord4j.common.util.Snowflake;
import eu.okaeri.configs.schema.GenericsPair;
import eu.okaeri.configs.serdes.BidirectionalTransformer;
import eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;

public class SnowflakeTransformer extends BidirectionalTransformer<String, Snowflake> {

    @Override
    public GenericsPair<String, Snowflake> getPair() {
        return this.genericsPair(String.class, Snowflake.class);
    }

    @Override
    public Snowflake leftToRight(@NonNull String data, @NonNull SerdesContext serdesContext) {
        return Snowflake.of(data);
    }

    @Override
    public String rightToLeft(@NonNull Snowflake data, @NonNull SerdesContext serdesContext) {
        return data.asString();
    }
}
