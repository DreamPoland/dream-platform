package cc.dreamcode.platform.bungee.component;

import cc.dreamcode.platform.bungee.DreamBungeePlatform;
import cc.dreamcode.platform.bungee.persistence.StorageConfig;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.utilities.builder.MapBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.zaxxer.hikari.HikariConfig;
import eu.okaeri.configs.json.gson.JsonGsonConfigurer;
import eu.okaeri.configs.json.simple.JsonSimpleConfigurer;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBungee;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.persistence.PersistencePath;
import eu.okaeri.persistence.document.DocumentPersistence;
import eu.okaeri.persistence.flat.FlatPersistence;
import eu.okaeri.persistence.jdbc.H2Persistence;
import eu.okaeri.persistence.jdbc.MariaDbPersistence;
import eu.okaeri.persistence.mongo.MongoPersistence;
import eu.okaeri.persistence.redis.RedisPersistence;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.NonNull;

import java.util.Map;

public class DocumentPersistenceComponentResolver extends ComponentClassResolver<Class<DocumentPersistence>> {

    private @Inject DreamBungeePlatform dreamBungeePlatform;
    private @Inject StorageConfig storageConfig;

    @Override
    public boolean isAssignableFrom(@NonNull Class<DocumentPersistence> documentPersistenceClass) {
        return DocumentPersistence.class.isAssignableFrom(documentPersistenceClass);
    }

    @Override
    public String getComponentName() {
        return "persistence";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<DocumentPersistence> documentPersistenceClass) {
        return new MapBuilder<String, Object>()
                .put("type", this.storageConfig.storageType.getName())
                .put("prefix", this.storageConfig.prefix)
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<DocumentPersistence> documentPersistenceClass) {
        final PersistencePath persistencePath = PersistencePath.of(this.storageConfig.prefix);

        try { Class.forName("org.mariadb.jdbc.Driver"); } catch (ClassNotFoundException ignored) { }
        try { Class.forName("org.h2.Driver"); } catch (ClassNotFoundException ignored) { }

        switch (this.storageConfig.storageType) {
            case FLAT:
                return new DocumentPersistence(
                        new FlatPersistence(
                                this.dreamBungeePlatform.getDataFolder(),
                                ".json"
                        ),
                        JsonGsonConfigurer::new,
                        new SerdesBungee(),
                        new SerdesCommons(),
                        this.dreamBungeePlatform.getPluginSerdesPack()
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
                        new SerdesBungee(),
                        new SerdesCommons(),
                        this.dreamBungeePlatform.getPluginSerdesPack()
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
                        new SerdesBungee(),
                        new SerdesCommons(),
                        this.dreamBungeePlatform.getPluginSerdesPack()
                );
            case REDIS:
                RedisURI redisURI = RedisURI.create(this.storageConfig.uri);
                RedisClient redisClient = RedisClient.create(redisURI);

                return new DocumentPersistence(
                        new RedisPersistence(
                                persistencePath,
                                redisClient
                        ),
                        JsonSimpleConfigurer::new,
                        new SerdesBungee(),
                        new SerdesCommons(),
                        this.dreamBungeePlatform.getPluginSerdesPack()
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
                        new SerdesBungee(),
                        new SerdesCommons(),
                        this.dreamBungeePlatform.getPluginSerdesPack()
                );
            default:
                throw new RuntimeException("Unknown data type: " + this.storageConfig.storageType);
        }
    }
}
