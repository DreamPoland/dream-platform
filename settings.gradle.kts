rootProject.name = "dream-platform"

// -- core --
include(":core")
include(":core-kt")

// -- addons --
include(":persistence")

// -- minecraft --
include(":bukkit")
include(":bukkit-hook")
include(":bukkit-config")

include(":bungee")
include(":bungee-config")

// -- discord --
include(":javacord")
include(":javacord-config")

// -- other --
include(":cli")
include(":cli-config")

// -- libraries --
include(":dream-command")
include(":okaeri-command")
