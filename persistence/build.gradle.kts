dependencies {
    api(project(":core"))

    // -- configs--
    implementation(libs.okaeri.configs.core)
    implementation(libs.okaeri.configs.serdes.commons)

    // -- json configure --
    implementation(libs.okaeri.configs.json.gson)
    implementation(libs.okaeri.configs.json.simple)

    // -- persistence data --
    implementation(libs.okaeri.persistence.flat)
    implementation(libs.okaeri.persistence.jdbc)
    implementation(libs.okaeri.persistence.mongo)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    implementation(libs.dream.utilties)
}