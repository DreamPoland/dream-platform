package cc.dreamcode.platform.javacord.embed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javacord.api.entity.message.embed.EmbedField;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WrappedEmbedField implements EmbedField {

    private String name = "null?";
    private String value = "null?";
    private boolean inline = false;

}
