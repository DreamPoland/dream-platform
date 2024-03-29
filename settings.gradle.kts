rootProject.name = "dream-platform"

// -- core --
include(":core")
include(":core-kt")

// -- addons --
include(":persistence")
include(":hook")

// -- minecraft --
include(":bukkit")
include(":bukkit-hook")
include(":bukkit-config")
include(":bukkit-command")

include(":bungee")
include(":bungee-config")
include(":bungee-command")

// -- discord --
include(":javacord")
include(":javacord-config")

// -- other --
include(":cli")
include(":cli-config")

// -- libraries --
include(":okaeri-command")
