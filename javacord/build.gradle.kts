dependencies {
    api(project(":core"))

    // -- javacord --
    implementation(libs.javacord)

    // -- logger --
    implementation(libs.logback.core)
    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)
    implementation(libs.log4j.slf4j)

    // -- placeholders --
    implementation(libs.okaeri.placeholders)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    implementation(libs.dream.utilties)
}