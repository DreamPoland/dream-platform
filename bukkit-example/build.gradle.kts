repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.reposilite.com/maven-central")
}

dependencies {
    // -- spigot api -- (base)
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // -- dream-platform --
    implementation("cc.dreamcode.platform:bukkit:1.13.8")
    implementation("cc.dreamcode.platform:bukkit-config:1.13.8")
    implementation("cc.dreamcode.platform:dream-command:1.13.8")
    implementation("cc.dreamcode.platform:persistence:1.13.8")

    // -- dream-utilties --
    implementation("cc.dreamcode:utilities-adventure:1.5.8")

    // -- dream-notice --
    implementation("cc.dreamcode.notice:bukkit:1.7.4")
    implementation("cc.dreamcode.notice:bukkit-serializer:1.7.4")

    // -- dream-command --
    implementation("cc.dreamcode.command:bukkit:2.2.3")

    // -- dream-menu --
    implementation("cc.dreamcode.menu:bukkit:1.4.5")
    implementation("cc.dreamcode.menu:bukkit-serializer:1.4.5")
}