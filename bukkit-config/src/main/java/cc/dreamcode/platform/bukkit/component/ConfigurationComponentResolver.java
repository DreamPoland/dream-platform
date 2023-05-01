package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationComponentResolver extends ComponentClassResolver<Class<OkaeriConfig>> {

    private @Inject DreamPlatform dreamPlatform;
    private @Inject("configuration-serdes") OkaeriSerdesPack configurationSerdesPack;

    @Override
    public boolean isAssignableFrom(@NonNull Class<OkaeriConfig> okaeriConfigClass) {
        if (OkaeriConfig.class.isAssignableFrom(okaeriConfigClass)) {
            if (okaeriConfigClass.getAnnotation(Configuration.class) == null) {
                throw new PlatformException(okaeriConfigClass.getSimpleName() + " does not contain " + Configuration.class.getSimpleName() + " annotation.");
            }
            return true;
        }
        return false;
    }

    @Override
    public String getComponentName() {
        return "configuration";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull Injector injector, @NonNull Class<OkaeriConfig> okaeriConfigClass) {
        final Configuration configuration = okaeriConfigClass.getAnnotation(Configuration.class);
        if (configuration == null) {
            throw new PlatformException("Config component must have an " + Configuration.class.getSimpleName() + " annotation.");
        }

        return new MapBuilder<String, Object>()
                .put("path", configuration.child())
                .put("subconfigs", Arrays.stream(okaeriConfigClass.getDeclaredFields())
                        .map(Field::getName)
                        .filter(name -> name.contains("Config"))
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public Object resolve(@NonNull Injector injector, @NonNull Class<OkaeriConfig> okaeriConfigClass) {
        final Configuration configuration = okaeriConfigClass.getAnnotation(Configuration.class);
        if (configuration == null) {
            throw new PlatformException("Config component must have an " + Configuration.class.getSimpleName() + " annotation.");
        }

        if (!(this.dreamPlatform instanceof DreamBukkitConfig)) {
            throw new PlatformException(this.dreamPlatform.getClass().getSimpleName() + " class must implement DreamBukkitConfig.");
        }

        final DreamBukkitConfig dreamBukkitConfig = (DreamBukkitConfig) this.dreamPlatform;
        return ConfigManager.create(okaeriConfigClass, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit(), new SerdesCommons(), dreamBukkitConfig.getConfigSerdesPack());
            it.withBindFile(new File(this.dreamPlatform.getDataFolder(), configuration.child()));
            it.saveDefaults();
            it.load(true);
        });
    }
}
