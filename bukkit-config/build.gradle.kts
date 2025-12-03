repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":bukkit"))

    // -- spigot api (base) --
    compileOnly(libs.spigot16.api)

    // -- configs--
    api(libs.okaeri.configs.yaml.bukkit)
    api(libs.okaeri.configs.serdes.bukkit)
    implementation(libs.okaeri.configs.serdes.commons)
}