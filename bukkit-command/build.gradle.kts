repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))

    // -- spigot api (base) --
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // -- injector --
    implementation("eu.okaeri:okaeri-injector:2.1.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.0")

    // -- dream-command --
    implementation("cc.dreamcode.command:core:1.4.23")
    implementation("cc.dreamcode.command:bukkit:1.4.23")
}