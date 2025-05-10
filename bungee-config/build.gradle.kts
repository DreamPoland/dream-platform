dependencies {
    api(project(":core"))
    api(project(":bungee"))

    // -- configs--
    api(libs.okaeri.configs.yaml.bungee)
    api(libs.okaeri.configs.serdes.bungee)
    api(libs.okaeri.configs.serdes.commons)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    api(libs.dream.utilties)
}