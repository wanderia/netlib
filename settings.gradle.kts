rootProject.name = "netlib"

pluginManagement {
    includeBuild("buildLogic")
    repositories {
        maven("https://maven.fabricmc.net/") { name = "FabricMC" }
        maven("https://maven.neoforged.net/releases") { name = "NeoForge" }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    versionCatalogs { create("libs") { from(files("libs.versions.toml")) } }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(
    ":docs",

    ":mod:common",
    ":mod:fabric",
    ":mod:neoforge"
)
