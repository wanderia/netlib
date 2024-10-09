import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginExtension

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loader.dokka)
    alias(libs.plugins.loader.runtime)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.loom)
    alias(libs.plugins.minotaur)
    `maven-publish`
}

repositories { maven("https://maven.parchmentmc.org") { name = "Parchment" } }

dependencies {
    minecraft(libs.minecraft)
    mappings(
        loom.layered {
            officialMojangMappings()
            parchment(variantOf(libs.parchment.fabric) { artifactType("zip") })
        }
    )

    modImplementation(libs.fabric.kotlin)
    modImplementation(libs.fabric.loader)
    modImplementation(fabricApi.module("fabric-networking-api-v1", libs.versions.fabricApi.get()))
}

loom {
    runtimeOnlyLog4j = true

    mods { create("wanderia-netlib") { sourceSet(sourceSets.main.get()) } }
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }

    jar { from(rootProject.file("LICENSE")) { rename { "${it}_${rootProject.name}" } } }
}

kotlin { explicitApi() }

modrinth {
    val changelogPlugin = rootProject.extensions.getByType<ChangelogPluginExtension>()
    token = System.getenv("MODRINTH_TOKEN")
    debugMode = System.getenv("MODRINTH_DEBUG") == "1"
    projectId = "netlib"
    versionNumber = "${project.version}+fabric"
    versionType =
        with(project.version.toString()) {
            when {
                contains("alpha") -> "alpha"
                contains("beta") -> "beta"
                else -> "release"
            }
        }
    uploadFile.set(tasks.remapJar)
    additionalFiles.set(listOf(tasks.remapSourcesJar))
    gameVersions = listOf(libs.versions.minecraft.get())
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
    }
    changelog =
        changelogPlugin.renderItem(
            changelogPlugin.getUnreleased().withEmptySections(false).withHeader(false),
            Changelog.OutputType.MARKDOWN,
        )
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = base.archivesName.get()
            pom {
                name = "netlib-fabric"
                description = "netlib $version - Kotlinx Serialization for custom payloads."
                url = "https://github.com/wanderia/netlib"
                licenses {
                    license {
                        name = "Mozilla Public License v2.0"
                        url = "https://www.mozilla.org/en-US/MPL/2.0/"
                    }
                }
                developers {
                    developer {
                        name = "Pyrrha Wills"
                        id = "JustPyrrha"
                        email = "contact@pyrrha.gay"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/wanderia/netlib.git"
                    developerConnection = "scm:git:ssh://github.com:wanderia/netlib.git"
                    url = "https://github.com/wanderia/netlib/tree/main"
                }
            }
            artifact(tasks.remapJar)
            artifact(tasks.remapSourcesJar)
        }
    }

    repositories {
        val repositoryUrl =
            with(project.version.toString()) {
                when {
                    contains("alpha") || contains("beta") -> "s3://maven.wanderia.dev/snapshots"
                    else -> "s3://maven.wanderia.dev/releases"
                }
            }

        maven(repositoryUrl) {
            credentials(AwsCredentials::class) {
                accessKey = System.getenv("AWS_ACCESS_KEY_ID")
                secretKey = System.getenv("AWS_SECRET_ACCESS_KEY")
            }
        }
    }
}
