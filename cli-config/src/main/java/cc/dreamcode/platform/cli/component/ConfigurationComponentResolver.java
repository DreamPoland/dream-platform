package cc.dreamcode.platform.cli.component;

import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.cli.DreamCliConfig;
import cc.dreamcode.platform.cli.component.configuration.Configuration;
import cc.dreamcode.platform.cli.exception.CliPlatformException;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
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

    @Override
    public boolean isAssignableFrom(@NonNull Class<OkaeriConfig> okaeriConfigClass) {
        if (OkaeriConfig.class.isAssignableFrom(okaeriConfigClass)) {
            if (okaeriConfigClass.getAnnotation(Configuration.class) == null) {
                throw new CliPlatformException(okaeriConfigClass.getSimpleName() + " does not contain " + Configuration.class.getSimpleName() + " annotation.");
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
            throw new CliPlatformException("Config component must have an " + Configuration.class.getSimpleName() + " annotation.");
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
            throw new CliPlatformException("Config component must have an " + Configuration.class.getSimpleName() + " annotation.");
        }

        if (!(this.dreamPlatform instanceof DreamCliConfig)) {
            throw new PlatformException(this.dreamPlatform.getClass().getSimpleName() + " class must implement DreamCliConfig.");
        }

        final DreamCliConfig dreamCliConfig = (DreamCliConfig) this.dreamPlatform;
        return ConfigManager.create(okaeriConfigClass, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesCommons(), dreamCliConfig.getConfigSerdesPack());
            it.withBindFile(new File(configuration.child()));
            it.saveDefaults();
            it.load(true);
        });
    }
}
