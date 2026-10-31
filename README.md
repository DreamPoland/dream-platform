# Dream-Platform
[![Build](https://github.com/DreamPoland/dream-platform/actions/workflows/gradle.yml/badge.svg)](https://github.com/DreamPoland/dream-platform/actions/workflows/gradle.yml)

A modern, multi-module application framework for Minecraft plugins (Bukkit, Bungee), Discord bots (Javacord), and CLI applications, powered by Okaeri Injector and Okaeri Configs.

> **Requirements:** Java 21 or higher.

---

### Repository:

```xml
<repository>
  <id>dreamcode-repository-releases</id>
  <url>https://repo.dreamcode.cc/releases</url>
</repository>
```

```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

### Core Dependency:

```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>core</artifactId>
  <version>2.0.0</version>
</dependency>
```

```groovy
implementation "cc.dreamcode.platform:core:2.0.0"
```

---

### Supported Modules & Platforms:

#### Platforms:
- **[Bukkit](bukkit)**: Support for Minecraft Spigot/Paper server plugins.
- **[Bungee](bungee)**: Support for BungeeCord/Waterfall proxy plugins.
- **[Javacord](javacord)**: Support for Discord bot applications.
- **[CLI](cli)**: Support for standalone command-line Java applications.

#### Addons & Commons:
- **[bukkit-config](bukkit-config)** / **[bungee-config](bungee-config)** / **[javacord-config](javacord-config)** / **[cli-config](cli-config)**: Okaeri configuration serializers and integration.
- **[bukkit-hook](bukkit-hook)**: Integrations with third-party Bukkit plugins.
- **[persistence](persistence)**: Storage layer (FLAT, MySQL, Mongo, H2) using Okaeri Persistence.
- **[core-kt](core-kt)**: Kotlin language extensions.
- **[dream-command](dream-command)**: Command framework integration.
- **[bukkit-example](bukkit-example)**: Reference implementation of a complete Bukkit plugin.

```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>{module-name}</artifactId>
  <version>2.0.0</version>
</dependency>
```

```groovy
implementation "cc.dreamcode.platform:{module-name}:2.0.0"
```
