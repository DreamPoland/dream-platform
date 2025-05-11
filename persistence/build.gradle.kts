dependencies {
    api(project(":core"))

    // -- configs--
    api(libs.okaeri.configs.core)
    implementation(libs.okaeri.configs.serdes.commons)

    // -- persistence data --
    api(libs.okaeri.persistence.flat)
    api(libs.okaeri.persistence.jdbc)
    api(libs.okaeri.persistence.mongo)

    // -- json configure --
    implementation(libs.okaeri.configs.json.gson)
    implementation(libs.okaeri.configs.json.simple)
}