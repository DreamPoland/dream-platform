dependencies {
    api(project(":core"))

    // -- logging --
    api(libs.logback.core)
    api(libs.logback.classic)
    api(libs.slf4j.api)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    api(libs.dream.utilties)
}