import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
}

repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- kotlin --
    api(libs.kotlin.stdlib.jdk8)

    // -- configs--
    api(libs.okaeri.configs.core)

    // -- injector
    api(libs.okaeri.injector)

    // -- dream-utilities --
    api(libs.dream.utilties)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        languageVersion = "1.8"
    }
}