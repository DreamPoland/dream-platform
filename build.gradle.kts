plugins {
    id("java-library")
    id("maven-publish")
}

allprojects {
    group = "cc.dreamcode.platform"
    version = "2.0.0"

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
        maven("https://repo.dreamcode.cc/releases")
        maven("https://storehouse.okaeri.eu/repository/maven-public")
    }
}

subprojects {
    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    tasks.withType<Javadoc> {
        options.encoding = "UTF-8"
        isFailOnError = false
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        failOnNoDiscoveredTests = false
    }

    dependencies {
        compileOnly(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.lombok)
        testCompileOnly(rootProject.libs.lombok)
        testAnnotationProcessor(rootProject.libs.lombok)

        testImplementation(rootProject.libs.junit.jupiter)
        testRuntimeOnly(rootProject.libs.junit.platform.launcher)
    }

    if (project.name != "bukkit-example") {
        publishing {
            repositories {
                maven {
                    if (version.toString().endsWith("-SNAPSHOT")) {
                        name = "snapshots"
                        url = uri("https://repo.dreamcode.cc/snapshots")
                    } else {
                        name = "releases"
                        url = uri("https://repo.dreamcode.cc/releases")
                    }

                    credentials {
                        username = System.getenv("MAVEN_USERNAME")
                        password = System.getenv("MAVEN_TOKEN")
                    }
                }
            }

            publications {
                create<MavenPublication>("library") {
                    from(components.getByName("java"))

                    pom.withXml {
                        val repositories = asNode().appendNode("repositories")
                        project.repositories
                            .filterIsInstance<MavenArtifactRepository>()
                            .filter { it.url.toString().startsWith("https") }
                            .forEach { repo ->
                                val repository = repositories.appendNode("repository")
                                repository.appendNode("id", repo.url.toString().replace("https://", "").replace("/", "-").replace(".", "-").trim())
                                repository.appendNode("url", repo.url.toString().trim())
                            }
                    }
                }
            }
        }
    }
}
