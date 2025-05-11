dependencies {
    api(project(":cli"))

    // -- configs--
    api(libs.okaeri.configs.yaml.snakeyaml)
    implementation(libs.okaeri.configs.serdes.commons)
}