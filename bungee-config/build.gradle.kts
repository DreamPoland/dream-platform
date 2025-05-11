dependencies {
    api(project(":core"))
    api(project(":bungee"))

    // -- configs--
    implementation(libs.okaeri.configs.yaml.bungee)
    implementation(libs.okaeri.configs.serdes.bungee)
    implementation(libs.okaeri.configs.serdes.commons)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    implementation(libs.dream.utilties)
}