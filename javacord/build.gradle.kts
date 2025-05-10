dependencies {
    api(project(":core"))

    // -- javacord --
    api(libs.javacord)

    // -- logger --
    api(libs.logback.core)
    api(libs.logback.classic)
    api(libs.slf4j.api)
    api(libs.log4j.slf4j)

    // -- placeholders --
    api(libs.okaeri.placeholders)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    api(libs.dream.utilties)
}