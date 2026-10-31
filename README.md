# Dream-Platform

[![Build](https://github.com/DreamPoland/dream-platform/actions/workflows/gradle.yml/badge.svg)](https://github.com/DreamPoland/dream-platform/actions/workflows/gradle.yml)
[![Version](https://img.shields.io/badge/version-2.0.1-blue.svg)](https://repo.dreamcode.cc/#/releases/cc/dreamcode/platform)
[![Java](https://img.shields.io/badge/java-21%2B-orange.svg)](https://www.oracle.com/java/)

Modern, multi-module application framework for Minecraft plugins (Bukkit, BungeeCord), Discord bots (Javacord), and standalone CLI applications, powered by **Okaeri Injector** and **Okaeri Configs**.

---

## Features

- **Dependency Injection & Component Lifecycle**: Automatic injection, component resolvers, method resolvers, and lifecycle management powered by `eu.okaeri:okaeri-injector`.
- **Multi-Platform Support**:
  - **Bukkit / Spigot / Paper**: Full lifecycle support (`onLoad`, `onEnable`, `onDisable`), main-thread & async schedulers.
  - **BungeeCord / Waterfall**: Native proxy plugin integration with Bungee scheduler.
  - **Javacord**: Discord bot platform with automatic slash commands registration and lifecycle hooks.
  - **CLI**: Standalone Java console applications with shutdown hooks and timer schedulers.
- **Declarative Configuration**: Seamless integration with **Okaeri Configs** (YAML/JSON) with automatic file binding and custom serdes packs.
- **Persistence Layer**: Out-of-the-box storage integration via **Okaeri Persistence** supporting `FLAT`, `MySQL`, `PostgreSQL`, `MongoDB`, and `H2`.
- **Annotation-Driven Schedulers**: Schedule methods asynchronously or synchronously using `@Scheduler(delay = ..., interval = ...)`.
- **Plugin Hook Architecture**: Dynamic third-party Bukkit plugin hook detection and initialization (`PluginHookService` & `PluginHookResolver`).
- **Idiomatic Kotlin Support**: First-class Kotlin extensions (`core-kt`) with `inline reified` generics.

---

## Requirements

- **Java 21** or newer.
- **Minecraft**: Bukkit/Spigot/Paper 1.8.8 - 1.21+ or BungeeCord 1.20+.

---

## Installation

### Maven

Add the DreamCode repository:
```xml
<repositories>
  <repository>
    <id>dreamcode-repository-releases</id>
    <url>https://repo.dreamcode.cc/releases</url>
  </repository>
</repositories>
```

Add dependencies for your target platform:

**Bukkit / Paper:**
```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>bukkit</artifactId>
  <version>2.0.1</version>
</dependency>
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>bukkit-config</artifactId>
  <version>2.0.1</version>
</dependency>
```

**BungeeCord:**
```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>bungee</artifactId>
  <version>2.0.1</version>
</dependency>
```

**Discord Bot (Javacord):**
```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>javacord</artifactId>
  <version>2.0.1</version>
</dependency>
```

**Standalone CLI:**
```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>cli</artifactId>
  <version>2.0.1</version>
</dependency>
```

**Persistence Support:**
```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>persistence</artifactId>
  <version>2.0.1</version>
</dependency>
```

---

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
    maven("https://repo.dreamcode.cc/releases")
    maven("https://storehouse.okaeri.eu/repository/maven-public")
}

dependencies {
    // Core Platform
    implementation("cc.dreamcode.platform:core:2.0.1")

    // Platform Modules (choose your target)
    implementation("cc.dreamcode.platform:bukkit:2.0.1")
    implementation("cc.dreamcode.platform:bukkit-config:2.0.1")
    implementation("cc.dreamcode.platform:bukkit-hook:2.0.1")

    // Persistence Layer (Flat, MySQL, PostgreSQL, Mongo, H2)
    implementation("cc.dreamcode.platform:persistence:2.0.1")

    // Kotlin Extensions
    implementation("cc.dreamcode.platform:core-kt:2.0.1")
}
```

---

## Usage

### 1. Bukkit / Spigot / Paper Plugin

Extend `DreamBukkitPlatform` and implement `load`, `enable`, and `disable`:

```java
public final class ExamplePlugin extends DreamBukkitPlatform implements DreamBukkitConfig {

    @Override
    public void load(@NonNull ComponentService componentService) {
        // Run before enable (onLoad)
    }

    @Override
    public void enable(@NonNull ComponentService componentService) {
        // 1. Configure debug mode
        componentService.setDebug(false);

        // 2. Register configuration resolver and load configs
        componentService.registerResolver(ConfigurationResolver.class);
        componentService.registerComponent(PluginConfig.class, pluginConfig -> {
            // Callback when config is loaded
            this.registerInjectable(pluginConfig.storageConfig);
        });

        // 3. Register components (Services, Controllers, Listeners)
        componentService.registerComponent(PlayerController.class);
        componentService.registerComponent(UserService.class);
    }

    @Override
    public void disable() {
        // Shutdown logic (onDisable)
    }

    @Override
    public OkaeriSerdes getConfigSerdesPack() {
        return it -> {
            // Register custom Okaeri serializers here
        };
    }
}
```

---

### 2. Schedulers & Background Tasks

You can schedule methods declaratively using the `@Scheduler` annotation or programmatically via platform methods:

#### Declarative `@Scheduler`:
```java
public class BroadcastTask {

    @Scheduler(delay = 20L, interval = 1200L, async = true)
    public void announce() {
        // Executed asynchronously every 60 seconds (1200 ticks)
    }
}
```

#### Programmatic Execution:
```java
// Run task on main server thread
this.runSync(() -> player.teleport(location));

// Run task asynchronously
this.runAsync(() -> database.save(data));

// Run delayed or repeating tasks
this.runLater(() -> player.sendMessage("Welcome!"), 20L);
this.runTimer(() -> syncData(), 20L, 200L);
```

---

### 3. Declarative Configuration (`OkaeriConfig`)

Define configuration classes using `@Configuration`:

```java
@Configuration(child = "config.yml")
public class PluginConfig extends OkaeriConfig {

    @Comment("Plugin prefix displayed in chat")
    public String prefix = "&8[&bDreamPlatform&8] &7";

    @Comment("Storage configuration")
    public StorageConfig storageConfig = new StorageConfig("example_prefix");
}
```

---

### 4. Persistence Layer (MySQL, PostgreSQL, Mongo, H2, Flat)

Enable persistence by implementing `DreamPersistence`:

```java
public final class ExamplePlugin extends DreamBukkitPlatform implements DreamPersistence {

    @Override
    public void enable(@NonNull ComponentService componentService) {
        // Register persistence resolvers
        componentService.registerResolver(DocumentPersistenceResolver.class);
        componentService.registerComponent(DocumentPersistence.class);
        componentService.registerResolver(DocumentRepositoryResolver.class);

        // Register repositories
        componentService.registerComponent(UserRepository.class);
    }
}
```

Define repository interfaces:
```java
public interface UserRepository extends DocumentRepository<UUID, UserProfile> {
}
```

---

### 5. Idiomatic Kotlin Support (`core-kt`)

When using Kotlin, leverage `inline reified` extensions for clean, concise code:

```kotlin
class ExampleKotlinPlugin : DreamBukkitPlatform() {

    override fun enable(componentService: ComponentService) {
        // Register components using reified generics
        componentService.registerComponent<PluginConfig> { config ->
            println("Config loaded!")
        }
        componentService.registerComponent<PlayerController>()

        // Inject dependencies safely
        val userService = getInjectOrThrow<UserService>()
        val instance = createInstance<CustomManager>()
    }
}
```

---

## Modules Overview

| Module | Description |
|---|---|
| [`core`](core) | Base platform interfaces, `DreamLogger`, and `ComponentService` dependency injection. |
| [`core-kt`](core-kt) | Kotlin reified extension functions for `DreamPlatform` and `ComponentService`. |
| [`bukkit`](bukkit) | Bukkit/Spigot/Paper platform lifecycle, events, and task schedulers. |
| [`bukkit-config`](bukkit-config) | Okaeri configuration resolvers and serializers for Bukkit items & colors. |
| [`bukkit-hook`](bukkit-hook) | Third-party plugin detection (`PluginHookService` & `PluginHookResolver`). |
| [`bungee`](bungee) | BungeeCord/Waterfall platform lifecycle and task schedulers. |
| [`bungee-config`](bungee-config) | Okaeri configuration resolvers for BungeeCord. |
| [`javacord`](javacord) | Discord bot platform integration using Javacord with slash commands. |
| [`javacord-config`](javacord-config) | Okaeri configuration resolvers and serializers for Discord embeds. |
| [`cli`](cli) | Standalone command-line Java application platform with shutdown workers. |
| [`cli-config`](cli-config) | Okaeri configuration resolvers for CLI applications. |
| [`persistence`](persistence) | Document persistence layer supporting Flat, MySQL, PostgreSQL, Mongo, and H2. |
| [`dream-command`](dream-command) | Integration resolvers for the DreamCode command framework. |
| [`bukkit-example`](bukkit-example) | Reference implementation demonstrating a complete Bukkit plugin architecture. |

---

## License

This project is licensed under the [MIT License](LICENSE).
