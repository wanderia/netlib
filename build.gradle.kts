import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.changelog.ChangelogPluginConstants.SEM_VER_REGEX
import org.jetbrains.changelog.date

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.loom) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.dokka.javadoc) apply false
    alias(libs.plugins.minotaur)
    alias(libs.plugins.kotlinx.kover)

    alias(libs.plugins.loader.common) apply false
    alias(libs.plugins.loader.dokka) apply false
    alias(libs.plugins.loader.runtime) apply false
}

group = "dev.wanderia"
version = "1.3.0"

repositories { mavenCentral() }

dependencies {
    kover(projects.mod.common)
    kover(projects.mod.fabric)
    kover(projects.mod.neoforge)
}

changelog {
    version = project.version.toString()
    path = rootProject.file("CHANGELOG.md").canonicalPath
    header = provider { "[${project.version}] - ${date()}" }
    headerParserRegex = SEM_VER_REGEX
    itemPrefix = "-"
    keepUnreleasedSection = true
    unreleasedTerm = "[Unreleased]"
    groups = listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security")
    lineSeparator = "\n"
    combinePreReleases = true
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    debugMode = System.getenv("MODRINTH_DEBUG") == "1"
    syncBodyFrom = rootProject.file("README.md").readText()
}

subprojects {
    if (!project.name.endsWith("mod") && project.name != "docs") {
        apply(plugin = "com.diffplug.spotless")
        extensions.configure<SpotlessExtension> {
            kotlin {
                ktfmt().kotlinlangStyle()
                licenseHeaderFile("${rootProject.projectDir}/HEADER")
            }
            kotlinGradle { ktfmt().kotlinlangStyle() }
            java {
                googleJavaFormat()
                licenseHeaderFile("${rootProject.projectDir}/HEADER")
            }
        }
    }

    group = rootProject.group
    version = rootProject.version
}

tasks {
    val copySubJars by registering(Copy::class) {
        dependsOn(
            ":mod:common:build",
            ":mod:fabric:build",
            ":mod:neoforge:build",
            ":docs:dokkaHtmlJar"
        )
        from(rootProject.subprojects.map { it.layout.buildDirectory.file("libs/").get().asFile })
        into(rootProject.layout.buildDirectory.dir("libs"))
    }
}
