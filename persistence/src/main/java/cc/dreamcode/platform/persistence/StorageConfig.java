package cc.dreamcode.platform.persistence;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.CustomKey;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class StorageConfig extends OkaeriConfig {

    @Comment({"W jakiej formie maja byc zapisywane dane o graczu?",
            "Dostepne zapisy: (FLAT, MYSQL, MONGO, H2)"})
    @CustomKey("storage-type")
    public StorageType storageType = StorageType.FLAT;

    @Comment({"Jaki prefix ustawic dla danych?",
            "Nie uzupelniaj prefixu dla zapisu typu FLAT."})
    @CustomKey("prefix")
    public String prefix = "dreamtemplate";

    @Comment("FLAT   : not applicable")
    @Comment("MONGO  : mongodb://{host}:{port}/{database}")
    @Comment("MYSQL  : jdbc:mysql://{host}:{port}/{database}?user={username}&password={password}")
    @Comment("H2     : jdbc:h2:file:./plugins/{data_folder}/storage;mode=mysql")
    @Comment("uri")
    public String uri = "";

    public StorageConfig(@NonNull String prefix) {
        this.prefix = prefix;
    }

}
