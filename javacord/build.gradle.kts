dependencies {
    api(project(":core"))

    // -- javacord --
    api(libs.javacord)

    // -- logger --
    implementation(libs.logback.core)
    implementation(libs.logback.classic)
    implementation(libs.slf4j.api)
    implementation(libs.log4j.slf4j)
}