dependencies {
    implementation(project(":core"))
    implementation(project(":cli"))

    // -- configs--
    implementation("eu.okaeri:okaeri-configs-yaml-snakeyaml:5.0.1")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:5.0.1")

    // -- injector --
    implementation("eu.okaeri:okaeri-injector:2.1.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.0")
}