plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.loader.common)
    alias(libs.plugins.loader.dokka)
    alias(libs.plugins.kotlinx.kover)
    alias(libs.plugins.loom)
    `maven-publish`
}

configurations {
    val commonKotlin by creating {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    val commonJava by creating {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    val commonResources by creating {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

repositories {
    mavenCentral()
    maven("https://maven.parchmentmc.org") { name = "Parchment" }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(
        loom.layered {
            officialMojangMappings()
            parchment(variantOf(libs.parchment.fabric) { artifactType("zip") })
        }
    )

    modImplementation(fabricApi.module("fabric-networking-api-v1", libs.versions.fabricApi.get()))

    implementation(libs.kotlinx.serializationCore)

    //    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.junit.params)
}

loom { runtimeOnlyLog4j = true }

kotlin { explicitApi() }

tasks { jar { from(rootProject.file("LICENSE")) { rename { "${it}_${rootProject.name}" } } } }

artifacts {
    sourceSets.main.get().kotlin.sourceDirectories.forEach { add("commonKotlin", it) }
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = base.archivesName.get()
            pom {
                name = "netlib-common"
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
