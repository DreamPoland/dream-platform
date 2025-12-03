package cc.dreamcode.project;

import cc.dreamcode.command.bukkit.BukkitCommandProvider;
import cc.dreamcode.menu.bukkit.BukkitMenuProvider;
import cc.dreamcode.platform.DreamVersion;
import cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.platform.bukkit.DreamBukkitPlatform;
import cc.dreamcode.platform.bukkit.component.ConfigurationResolver;
import cc.dreamcode.platform.component.ComponentService;
import cc.dreamcode.platform.other.component.DreamCommandExtension;
import cc.dreamcode.platform.persistence.component.DocumentPersistenceResolver;
import cc.dreamcode.platform.persistence.component.DocumentRepositoryResolver;
import cc.dreamcode.project.command.PointsCommand;
import cc.dreamcode.project.command.handler.InvalidInputHandlerImpl;
import cc.dreamcode.project.command.handler.InvalidPermissionHandlerImpl;
import cc.dreamcode.project.command.handler.InvalidSenderHandlerImpl;
import cc.dreamcode.project.command.handler.InvalidUsageHandlerImpl;
import cc.dreamcode.project.command.result.BukkitNoticeResolver;
import cc.dreamcode.project.config.MessageConfig;
import cc.dreamcode.project.config.PluginConfig;
import cc.dreamcode.project.controller.PlayerController;
import cc.dreamcode.project.menu.ExampleMenu;
import cc.dreamcode.project.menu.ExampleMenuHolder;
import cc.dreamcode.project.menu.MenuItemSerializer;
import cc.dreamcode.project.profile.ProfileCache;
import cc.dreamcode.project.profile.ProfileRepository;
import cc.dreamcode.project.profile.ProfileService;
import cc.dreamcode.project.task.AnnouncerTask;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.persistence.document.DocumentPersistence;
import lombok.NonNull;

public final class DreamProjectPlugin extends DreamBukkitPlatform implements DreamBukkitConfig {

    @Override
    public void load(@NonNull ComponentService componentService) {
        // default load logic
    }

    @Override
    public void enable(@NonNull ComponentService componentService) {
        // 1. Setup Debug & Core Providers
        componentService.setDebug(false);

        this.registerInjectable(BukkitMenuProvider.create(this));
        this.registerInjectable(BukkitCommandProvider.create(this));

        // 2. Register Extensions
        componentService.registerExtension(DreamCommandExtension.class);

        // 3. Register Config Resolvers & Messages
        componentService.registerResolver(ConfigurationResolver.class);
        componentService.registerComponent(MessageConfig.class);

        // 4. Register Command Handlers & Notices
        componentService.registerComponent(BukkitNoticeResolver.class);
        componentService.registerComponent(InvalidInputHandlerImpl.class);
        componentService.registerComponent(InvalidPermissionHandlerImpl.class);
        componentService.registerComponent(InvalidSenderHandlerImpl.class);
        componentService.registerComponent(InvalidUsageHandlerImpl.class);

        // 5. Register Main Config & Initialize Persistence
        // The lambda executes immediately after PluginConfig is loaded.
        componentService.registerComponent(PluginConfig.class, pluginConfig -> {

            // Register the StorageConfig (from PluginConfig) as an injectable
            this.registerInjectable(pluginConfig.storageConfig);

            // Register Persistence Resolvers
            componentService.registerResolver(DocumentPersistenceResolver.class);
            componentService.registerComponent(DocumentPersistence.class);
            componentService.registerResolver(DocumentRepositoryResolver.class);
        });

        // 6. Register Data Components (Repositories & Caches)
        // These rely on DocumentPersistence being ready
        componentService.registerComponent(ProfileRepository.class);
        componentService.registerComponent(ProfileCache.class);

        // 7. Register Logic Components (Services)
        // ProfileService @PostConstruct will fire here, loading data from Repo -> Cache
        componentService.registerComponent(ProfileService.class);

        // 8. Register Menu Components
        componentService.registerComponent(ExampleMenu.class);
        componentService.registerComponent(ExampleMenuHolder.class);

        // 9. Register Commands & Listeners
        // These rely on ProfileService being ready
        componentService.registerComponent(PlayerController.class);
        componentService.registerComponent(PointsCommand.class);

        // 10. Register Tasks
        // Free fron injectable fields
        componentService.registerComponent(AnnouncerTask.class);
    }

    @Override
    public void disable() {
        // default disable logic
    }

    @Override
    public @NonNull DreamVersion getDreamVersion() {
        return DreamVersion.create("plugin", "1.0", "author");
    }

    // Override this method to register custom serializers
    @Override
    public OkaeriSerdesPack getConfigSerdesPack() {
        return it -> {
            it.register(new MenuItemSerializer());
        };
    }
}