import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.22"
}

repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    // -- kotlin --
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.22")

    implementation(project(":core"))

    // -- configs--
    implementation("eu.okaeri:okaeri-configs-core:5.0.1")

    // -- injector --
    implementation("eu.okaeri:okaeri-injector:2.1.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        languageVersion = "1.8"
    }
}