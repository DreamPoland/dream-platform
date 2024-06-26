package cc.dreamcode.platform.persistence.component;

import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.platform.persistence.DreamPersistence;
import cc.dreamcode.platform.persistence.StorageConfig;
import cc.dreamcode.utilities.builder.MapBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zaxxer.hikari.HikariConfig;
import eu.okaeri.configs.json.gson.JsonGsonConfigurer;
import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.flat.FlatPersistence;
import eu.okaeri.persistence.jdbc.H2Persistence;
import eu.okaeri.persistence.jdbc.MariaDbPersistence;
import eu.okaeri.persistence.mongo.MongoPersistence;
import lombok.NonNull;

import java.util.Map;

public class DocumentPersistenceResolver implements ComponentClassResolver<DocumentPersistence> {

    private final DreamPlatform dreamPlatform;
    private final StorageConfig storageConfig;

    @Inject
    public DocumentPersistenceResolver(DreamPlatform dreamPlatform, StorageConfig storageConfig) {
        this.dreamPlatform = dreamPlatform;
        this.storageConfig = storageConfig;
    }

    @Override
    public boolean isAssignableFrom(@NonNull Class<DocumentPersistence> documentPersistenceClass) {
        return DocumentPersistence.class.isAssignableFrom(documentPersistenceClass);
    }

    @Override
    public String getComponentName() {
        return "persistence";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull DocumentPersistence documentPersistence) {
        return new MapBuilder<String, Object>()
                .put("type", this.storageConfig.storageType.getName())
                .put("prefix", this.storageConfig.prefix)
                .build();
    }

    @Override
    public DocumentPersistence resolve(@NonNull Injector injector, @NonNull Class<DocumentPersistence> documentPersistenceClass) {
        final PersistencePath persistencePath = PersistencePath.of(this.storageConfig.prefix);

        try { Class.forName("org.mariadb.jdbc.Driver"); } catch (ClassNotFoundException ignored) { }
        try { Class.forName("org.h2.Driver"); } catch (ClassNotFoundException ignored) { }

        if (!(this.dreamPlatform instanceof DreamPersistence)) {
            throw new PlatformException(this.dreamPlatform.getClass().getSimpleName() + " class must implement DreamPersistence.");
        }

        final DreamPersistence dreamPersistence = (DreamPersistence) this.dreamPlatform;
        switch (this.storageConfig.storageType) {
            case FLAT:
                return new DocumentPersistence(
                        new FlatPersistence(
                                this.dreamPlatform.getDataFolder(),
                                ".json"
                        ),
                        JsonGsonConfigurer::new,
                        new SerdesCommons(),
                        dreamPersistence.getPersistenceSerdesPack()
                );
            case MYSQL:
                HikariConfig mariadbHikari = new HikariConfig();
                mariadbHikari.setJdbcUrl(this.storageConfig.uri);

                return new DocumentPersistence(
                        new MariaDbPersistence(
                                persistencePath,
                                mariadbHikari
                        ),
                        JsonSimpleConfigurer::new,
                        new SerdesCommons(),
                        dreamPersistence.getPersistenceSerdesPack()
                );
            case H2:
                HikariConfig jdbcHikari = new HikariConfig();
                jdbcHikari.setJdbcUrl(this.storageConfig.uri);

                return new DocumentPersistence(
                        new H2Persistence(
                                persistencePath,
                                jdbcHikari
                        ),
                        JsonSimpleConfigurer::new,
                        new SerdesCommons(),
                        dreamPersistence.getPersistenceSerdesPack()
                );
            case MONGO:
                MongoClient mongoClient = MongoClients.create(this.storageConfig.uri);

                return new DocumentPersistence(
                        new MongoPersistence(
                                persistencePath,
                                mongoClient,
                                this.storageConfig.prefix
                        ),
                        JsonSimpleConfigurer::new,
                        new SerdesCommons(),
                        dreamPersistence.getPersistenceSerdesPack()
                );
            default:
                throw new PlatformException("Unknown data type: " + this.storageConfig.storageType);
        }
    }
}
