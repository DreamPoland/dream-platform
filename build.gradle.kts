plugins {
    `java-library`
    `maven-publish`
}

allprojects {
    group = "cc.dreamcode.platform"
    version = "1.12.5"

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        /* Libraries */
        mavenCentral()
        maven("https://repo.dreamcode.cc/releases")
        maven("https://storehouse.okaeri.eu/repository/maven-public")
    }
}

subprojects {
    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
    }

    dependencies {
        /* General */
        val lombok = "1.18.34"
        compileOnly("org.projectlombok:lombok:$lombok")
        annotationProcessor("org.projectlombok:lombok:$lombok")
        testCompileOnly("org.projectlombok:lombok:$lombok")
        testAnnotationProcessor("org.projectlombok:lombok:$lombok")
    }

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
                    project.repositories.findAll(closureOf<Any> {
                        if (this is MavenArtifactRepository && this.url.toString().startsWith("https")) {
                            val repository = repositories.appendNode("repository")
                            repository.appendNode("id", this.url.toString().replace("https://", "").replace("/", "-").replace(".", "-").trim())
                            repository.appendNode("url", this.url.toString().trim())
                        }
                    })
                }
            }
        }
    }
}