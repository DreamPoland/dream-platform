# dream-platform
Simple project that will create an application platform for you.

```xml
<repository>
  <id>dreamcode-repository-releases</id>
  <url>https://repo.dreamcode.cc/releases</url>
</repository>
```

```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

### Core:
```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>core</artifactId>
  <version>1.11.11</version>
</dependency>
```

```groovy
implementation "cc.dreamcode.platform:core:1.11.11"
```

### Supported platforms:
- [Bukkit](https://github.com/DreamPoland/dream-platform/tree/master/bukkit)
- [Bungee](https://github.com/DreamPoland/dream-platform/tree/master/bungee)
- [Javacord](https://github.com/DreamPoland/dream-platform/tree/master/javacord)
- [CLI](https://github.com/DreamPoland/dream-platform/tree/master/cli)

### Commons:
- [Persistence support](https://github.com/DreamPoland/dream-platform/tree/master/persistence)
- [Kotlin support](https://github.com/DreamPoland/dream-platform/tree/master/core-kt)
- [Hook support](https://github.com/DreamPoland/dream-platform/tree/master/hook)
+ config/commands support for platform modules.

```xml
<dependency>
  <groupId>cc.dreamcode.platform</groupId>
  <artifactId>{platform/common}</artifactId>
  <version>1.11.11</version>
</dependency>
```
```groovy
implementation "cc.dreamcode.platform:{platform/common}:1.11.11"
```

For project content, open project modules and see the contents of the classes. (todo)
