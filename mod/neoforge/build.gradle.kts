import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginExtension

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loader.dokka)
    alias(libs.plugins.loader.runtime)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.neogradle)
    alias(libs.plugins.minotaur)
    `maven-publish`
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "KotlinForForge" }
}

dependencies {
    implementation(libs.neoforge)
    implementation(libs.kotlinforforge)
}

subsystems { parchment { mappingsVersion = libs.versions.parchment.get() } }

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("neoforge.mods.toml") { expand(mutableMapOf("version" to project.version)) }
    }
}

tasks { jar { from(rootProject.file("LICENSE")) { rename { "${it}_${rootProject.name}" } } } }

modrinth {
    val changelogPlugin = rootProject.extensions.getByType<ChangelogPluginExtension>()
    token = System.getenv("MODRINTH_TOKEN")
    debugMode = System.getenv("MODRINTH_DEBUG") == "1"
    projectId = "netlib"
    versionNumber = "${project.version}+neoforge"
    versionType =
        with(project.version.toString()) {
            when {
                contains("alpha") -> "alpha"
                contains("beta") -> "beta"
                else -> "release"
            }
        }
    uploadFile.set(tasks.jar)
    additionalFiles.set(listOf(tasks.sourcesJar))
    gameVersions = listOf(libs.versions.minecraft.get())
    dependencies { required.project("kotlin-for-forge") }
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
                name = "netlib"
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
            artifact(tasks.jar)
            artifact(tasks.sourcesJar)
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
