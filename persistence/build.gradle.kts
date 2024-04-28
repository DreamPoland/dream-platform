dependencies {
    implementation(project(":core"))

    // -- configs--
    implementation("eu.okaeri:okaeri-configs-core:5.0.1")
    implementation("eu.okaeri:okaeri-configs-serdes-commons:5.0.1")

    // -- json configure --
    implementation("eu.okaeri:okaeri-configs-json-gson:5.0.1")
    implementation("eu.okaeri:okaeri-configs-json-simple:5.0.1")

    // -- persistence data --
    implementation("eu.okaeri:okaeri-persistence-flat:2.0.3")
    implementation("eu.okaeri:okaeri-persistence-jdbc:2.0.3")
    implementation("eu.okaeri:okaeri-persistence-mongo:2.0.3")

    // -- injector --
    implementation("eu.okaeri:okaeri-injector:2.1.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.3")
}