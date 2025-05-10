dependencies {
    api(project(":core"))
    api(project(":cli"))

    // -- configs--
    api(libs.okaeri.configs.yaml.snakeyaml)
    api(libs.okaeri.configs.serdes.commons)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    api(libs.dream.utilties)
}