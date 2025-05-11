dependencies {
    api(project(":javacord"))

    // -- javacord --
    compileOnly(libs.javacord)

    // -- configs--
    api(libs.okaeri.configs.yaml.snakeyaml)
    implementation(libs.okaeri.configs.serdes.commons)
}