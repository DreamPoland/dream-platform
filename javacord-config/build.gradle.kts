dependencies {
    api(project(":core"))
    api(project(":javacord"))

    // -- javacord --
    api(libs.javacord)

    // -- configs--
    api(libs.okaeri.configs.yaml.snakeyaml)
    api(libs.okaeri.configs.serdes.commons)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    api(libs.dream.utilties)
}