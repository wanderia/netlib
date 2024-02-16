plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loom)

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.yumi.licenser)
}

group = "dev.wanderia"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.kotlin)
    modImplementation(libs.fabric.loader)
    modImplementation(fabricApi.module("fabric-networking-api-v1", libs.versions.fabricApi.get()))
    implementation(libs.kotlinx.serializationCore)
    testImplementation(libs.kotlin.test)
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

license {
    rule(file("HEADER"))
    include("**/*.kt")
    include("**/*.java")
}
