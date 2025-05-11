repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://repo.codemc.io/repository/maven-public")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- bungee-cord (base) --
    compileOnly(libs.bungeecord.api)
}