dependencies {
    api(project(":core"))

    // -- configs--
    api(libs.okaeri.configs.core)
    api(libs.okaeri.configs.serdes.commons)

    // -- json configure --
    api(libs.okaeri.configs.json.gson)
    api(libs.okaeri.configs.json.simple)

    // -- persistence data --
    api(libs.okaeri.persistence.flat)
    api(libs.okaeri.persistence.jdbc)
    api(libs.okaeri.persistence.mongo)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    api(libs.dream.utilties)
}