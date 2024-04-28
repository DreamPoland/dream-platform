package cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import cc.dreamcode.platform.bukkit.serializer.ItemStackSerializer;
import cc.dreamcode.platform.bukkit.serializer.nbt.NbtDataSerializer;
import cc.dreamcode.platform.component.ComponentClassResolver;
import cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.utilities.builder.MapBuilder;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.serdes.commons.SerdesCommons;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.injector.Injector;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigurationResolver implements ComponentClassResolver<OkaeriConfig> {

    private final DreamPlatform dreamPlatform;

    @Inject
    public ConfigurationResolver(DreamPlatform dreamPlatform) {
        this.dreamPlatform = dreamPlatform;
    }

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
                        .filter(field -> OkaeriConfig.class.isAssignableFrom(field.getType()))
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

        if (!DreamBukkitConfig.class.isAssignableFrom(this.dreamPlatform.getClass())) {
            throw new PlatformException(this.dreamPlatform.getClass().getSimpleName() + " must have DreamBukkitConfig implementation.");
        }

        final DreamBukkitConfig dreamBukkitConfig = (DreamBukkitConfig) this.dreamPlatform;
        return ConfigManager.create(type, (it) -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit(), new SerdesCommons(), dreamBukkitConfig.getConfigSerdesPack());
            it.withSerdesPack(registry -> {
                registry.register(new NbtDataSerializer());
                registry.registerExclusive(ItemStack.class, new ItemStackSerializer());
                registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
            });
            it.withBindFile(new File(this.dreamPlatform.getDataFolder(), configuration.child()));
            it.saveDefaults();
            it.load(true);
        });
    }
}
