import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginConstants.SEM_VER_REGEX
import org.jetbrains.changelog.date
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loom)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
    alias(libs.plugins.changelog)
    alias(libs.plugins.dokka)
    alias(libs.plugins.minotaur)
    alias(libs.plugins.kotlinx.kover)

    `maven-publish`
}

buildscript {
    dependencies {
        classpath(libs.dokka.pluginBase)
        classpath(libs.dokka.pluginVersioning)
    }
}

group = "dev.wanderia"

version = "1.1.0"

repositories { mavenCentral() }

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.officialMojangMappings())
    modImplementation(libs.fabric.kotlin)
    modImplementation(libs.fabric.loader)
    modImplementation(fabricApi.module("fabric-networking-api-v1", libs.versions.fabricApi.get()))
    implementation(libs.kotlinx.serializationCore)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.junit.params)
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") { expand(mutableMapOf("version" to project.version)) }
    }

    dokkaHtml {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customAssets = listOf(file("artSrc/logo-icon.svg"))
            footerMessage = "(c) 2024 Wanderia"
        }

        val docsVersionDir = projectDir.resolve("docs/version")
        val docsVersion = project.version.toString()
        val currentDocsDir = docsVersionDir.resolve(docsVersion)
        outputDirectory = currentDocsDir

        pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
            version = docsVersion
            olderVersionsDir = docsVersionDir
        }

        doLast {
            currentDocsDir.copyRecursively(file("docs-publishing/"), overwrite = true)
            currentDocsDir.resolve("older").deleteRecursively()
        }
    }

    val dokkaJar by
        registering(Jar::class) {
            from(dokkaHtml)
            dependsOn(dokkaHtml)
            archiveClassifier = "javadoc"
        }

    jar { from("LICENSE") { rename { "${it}_${project.base.archivesName.get()}" } } }

    test { useJUnitPlatform() }
}

val enableDCEVM: Boolean =
    if (System.getProperty("java.vendor") == "JetBrains s.r.o.") {
        println("JetBrains Runtime found, enabling Enhanced Class Redefinition (DCEVM)")
        true
    } else {
        false
    }

val testmod: SourceSet by
    sourceSets.creating {
        compileClasspath += sourceSets.main.get().compileClasspath
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().runtimeClasspath
        runtimeClasspath += sourceSets.main.get().output
    }

loom {
    runtimeOnlyLog4j = true
    runs {
        configureEach {
            if (enableDCEVM) {
                vmArgs("-XX:+AllowEnhancedClassRedefinition")
            }
            ideConfigGenerated(name.contains("testmod"))
            runDir("runs/$name")
        }

        create("testmodClient") {
            client()
            name = "Testmod Client"
            source(testmod)
        }

        create("testmodServer") {
            server()
            name = "Testmod Server"
            source(testmod)
        }
    }

    mods {
        create("wanderia-netlib") { sourceSet(sourceSets.main.get()) }

        create("testmod") { sourceSet(testmod) }
    }
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

kotlin {
    jvmToolchain(21)
    explicitApi()
}

java {
    withSourcesJar()
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

modrinth {
    token = System.getenv("MODRINTH_TOKEN")
    debugMode = System.getenv("MODRINTH_DEBUG") == "1"
    projectId = "netlib"
    versionNumber = project.version.toString()
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
        rootProject.changelog.renderItem(
            rootProject.changelog.getUnreleased().withEmptySections(false).withHeader(false),
            Changelog.OutputType.MARKDOWN,
        )
    syncBodyFrom = rootProject.file("README.md").readText()
}

spotless {
    kotlin {
        ktfmt().kotlinlangStyle()
        licenseHeaderFile("$projectDir/HEADER")
    }
    kotlinGradle { ktfmt().kotlinlangStyle() }
    java {
        googleJavaFormat()
        licenseHeaderFile("$projectDir/HEADER")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
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
            artifact(tasks.remapJar)
            artifact(tasks.remapSourcesJar)
            artifact(tasks["dokkaJar"])
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
