package cc.dreamcode.platform.persistence;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.NameModifier;
import eu.okaeri.configs.annotation.NameStrategy;
import eu.okaeri.configs.annotation.Names;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Names(strategy = NameStrategy.HYPHEN_CASE, modifier = NameModifier.TO_LOWER_CASE)
public class StorageConfig extends OkaeriConfig {

    @Comment({"W jakiej formie maja byc zapisywane dane o graczu?",
            "Dostepne zapisy: (FLAT, MYSQL, MONGO, H2)"})
    public StorageType storageType = StorageType.FLAT;

    @Comment({"Jaki prefix ustawic dla danych?",
            "Dla FLAT prefix nie jest uzywany."})
    public String prefix = "dreamtemplate";

    @Comment("FLAT   : nie jest wymagane podawanie dodatkowych informacji")
    @Comment("MONGO  : mongodb://{host}:{port}/?maxPoolSize=20&w=majority")
    @Comment("MYSQL  : jdbc:mysql://{host}:{port}/{database}?user={username}&password={password}")
    @Comment("H2     : jdbc:h2:file:./plugins/{data_folder}/storage;mode=mysql")
    public String uri = "";

    public StorageConfig(@NonNull String prefix) {
        this.prefix = prefix;
    }

}
