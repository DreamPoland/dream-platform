dependencies {
    api(project(":core"))
    api(project(":javacord"))

    // -- javacord --
    implementation(libs.javacord)

    // -- configs--
    implementation(libs.okaeri.configs.yaml.snakeyaml)
    implementation(libs.okaeri.configs.serdes.commons)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    implementation(libs.dream.utilties)
}