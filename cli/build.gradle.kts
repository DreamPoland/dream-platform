dependencies {
    api(project(":core"))

    // -- logging --
    implementation(libs.logback.core)
    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    implementation(libs.dream.utilties)
}