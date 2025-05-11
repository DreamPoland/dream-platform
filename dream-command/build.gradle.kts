repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- injector --
    api(libs.okaeri.injector)

    // -- dream-utilities --
    implementation(libs.dream.utilties)

    // -- dream-command --
    api(libs.dream.command.core)
}