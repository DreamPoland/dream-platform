repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.reposilite.com/maven-central")
}

dependencies {
    // -- spigot api -- (base)
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // -- dream-platform --
    implementation(project(":bukkit"))
    implementation(project(":bukkit-config"))
    implementation(project(":dream-command"))
    implementation(project(":persistence"))

    // -- dream-utilties --
    implementation("cc.dreamcode:utilities-adventure:2.0.2")

    // -- dream-notice --
    implementation("cc.dreamcode.notice:bukkit:1.8.3")
    implementation("cc.dreamcode.notice:bukkit-serializer:1.8.3")

    // -- dream-command --
    implementation("cc.dreamcode.command:bukkit:2.4.0")

    // -- dream-menu --
    implementation("cc.dreamcode.menu:bukkit:1.5.1")
    implementation("cc.dreamcode.menu:bukkit-serializer:1.5.1")
}
