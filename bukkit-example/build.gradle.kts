repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.reposilite.com/maven-central")
}

dependencies {
    // -- spigot api -- (base)
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // -- dream-platform --
    implementation("cc.dreamcode.platform:bukkit:1.14.0")
    implementation("cc.dreamcode.platform:bukkit-config:1.14.0")
    implementation("cc.dreamcode.platform:dream-command:1.14.0")
    implementation("cc.dreamcode.platform:persistence:1.14.0")

    // -- dream-utilties --
    implementation("cc.dreamcode:utilities-adventure:1.6.1")

    // -- dream-notice --
    implementation("cc.dreamcode.notice:bukkit:1.8.3")
    implementation("cc.dreamcode.notice:bukkit-serializer:1.8.3")

    // -- dream-command --
    implementation("cc.dreamcode.command:bukkit:2.3.0")

    // -- dream-menu --
    implementation("cc.dreamcode.menu:bukkit:1.5.1")
    implementation("cc.dreamcode.menu:bukkit-serializer:1.5.1")
}