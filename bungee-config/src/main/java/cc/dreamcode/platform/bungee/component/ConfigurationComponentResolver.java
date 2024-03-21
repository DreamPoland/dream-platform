package cc.dreamcode.platform.bungee.component;

import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.bungee.DreamBungeeConfig;
import cc.dreamcode.platform.bungee.component.configuration.Configuration;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBungee;
import eu.okaeri.configs.yaml.bungee.YamlBungeeConfigurer;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ConfigurationComponentResolver implements ComponentClassResolver<OkaeriConfig> {

    private final DreamPlatform dreamPlatform;

    @Override
    public boolean isAssignableFrom(@NonNull Class<OkaeriConfig> type) {
        return OkaeriConfig.class.isAssignableFrom(type);
    }

    @Override
    public String getComponentName() {
        return "configuration";
    }

    @Override
    public Map<String, Object> getMetas(@NonNull OkaeriConfig okaeriConfig) {
        final Configuration configuration = okaeriConfig.getClass().getAnnotation(Configuration.class);
        if (configuration == null) {
            throw new PlatformException("OkaeriConfig must have @Configuration annotation.");
        }

        return new MapBuilder<String, Object>()
                .put("path", configuration.child())
                .put("sub-configs", Arrays.stream(okaeriConfig.getClass().getDeclaredFields())
                        .filter(field -> field.getType().isAssignableFrom(OkaeriConfig.class))
                        .map(Field::getName)
                        .collect(Collectors.joining(", ")))
                .build();
    }

    @Override
    public OkaeriConfig resolve(@NonNull Injector injector, @NonNull Class<OkaeriConfig> type) {

        final Configuration configuration = type.getAnnotation(Configuration.class);
        if (configuration == null) {
            throw new PlatformException("OkaeriConfig must have @Configuration annotation.");
        }

        if (!this.dreamPlatform.getClass().isAssignableFrom(DreamBungeeConfig.class)) {
            throw new PlatformException(this.dreamPlatform.getClass().getSimpleName() + " must have DreamBungeeConfig implementation.");
        }

        final DreamBungeeConfig dreamBungeeConfig = (DreamBungeeConfig) this.dreamPlatform;
        return ConfigManager.create(type, (it) -> {
            it.withConfigurer(new YamlBungeeConfigurer(), new SerdesBungee(), new SerdesCommons(), dreamBungeeConfig.getConfigSerdesPack());
            it.withBindFile(new File(this.dreamPlatform.getDataFolder(), configuration.child()));
            it.saveDefaults();
            it.load(true);
        });
    }
}
