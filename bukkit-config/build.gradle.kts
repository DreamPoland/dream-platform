repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))
    api(project(":bukkit"))

    // -- spigot api (base) --
    compileOnly(libs.spigot16.api)

    // -- configs--
    implementation(libs.okaeri.configs.yaml.bukkit)
    implementation(libs.okaeri.configs.serdes.bukkit)
    implementation(libs.okaeri.configs.serdes.commons)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    implementation(libs.dream.utilties)
}