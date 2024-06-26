repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":bukkit"))

    // -- spigot api (base) --
    compileOnly("org.spigotmc:spigot-api:1.14-R0.1-SNAPSHOT")

    // -- configs--
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.1")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.1")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:5.0.1")

    // -- injector --
    implementation("eu.okaeri:okaeri-injector:2.1.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.4.1")
    implementation("cc.dreamcode:utilities-bukkit:1.4.1")
}