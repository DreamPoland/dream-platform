pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "dream-platform"

include(":core")
include(":core-kt")
include(":persistence")
include(":bukkit")
include(":bukkit-example")
include(":bukkit-hook")
include(":bukkit-config")
include(":bungee")
include(":bungee-config")
include(":javacord")
include(":javacord-config")
include(":cli")
include(":cli-config")
include(":dream-command")
