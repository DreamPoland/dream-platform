dependencies {
    implementation(project(":core"))

    // -- javacord --
    implementation("org.javacord:javacord:3.8.0")
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.17.0")

    // -- logger --
    implementation("ch.qos.logback:logback-core:1.4.6")
    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("org.slf4j:slf4j-api:2.0.6")

    // -- placeholders --
    implementation("eu.okaeri:okaeri-placeholders-core:5.0.1")

    // -- injector --
    implementation("eu.okaeri:okaeri-injector:2.1.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.0")
}