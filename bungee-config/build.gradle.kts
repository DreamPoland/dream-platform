dependencies {
    api(project(":bungee"))

    // -- configs--
    api(libs.okaeri.configs.yaml.bungee)
    implementation(libs.okaeri.configs.serdes.bungee)
    implementation(libs.okaeri.configs.serdes.commons)
}