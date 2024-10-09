plugins {
    alias(libs.plugins.loader.dokka)
    alias(libs.plugins.dokka.javadoc)
}

repositories {
    mavenCentral()
}

dependencies {
    dokka(projects.mod.common)
    dokka(projects.mod.fabric)
    dokka(projects.mod.neoforge)

    dokkaHtmlPlugin(libs.dokka.versioning)
}

dokka {
    moduleName = "netlib"

    pluginsConfiguration {
        versioning {
            version = rootProject.version.toString()
            olderVersionsDir = layout.projectDirectory.dir("version")
        }

        html {
            customAssets.from(rootProject.file("artSrc/logo-icon.svg"))
            footerMessage = "(c) 2024 Wanderia"
        }
    }
}

tasks {
    dokkaGenerate {
        doLast {
            val output = layout.buildDirectory.dir("dokka/html").get().asFile
            val versions = layout.projectDirectory.dir("version")
            output.copyRecursively(rootProject.file("docs-publishing/"), overwrite = true)

            output.resolve("older/").deleteRecursively()
            output.copyRecursively(versions.dir(rootProject.version.toString()).asFile)
        }
    }

    val dokkaHtmlJar by registering(Jar::class) {
        description = "A HTML Documentation JAR containing Dokka HTML"
        from(dokkaGeneratePublicationHtml.flatMap { it.outputDirectory })
        archiveClassifier.set("html-doc")

        destinationDirectory.set(layout.buildDirectory.dir("libs"))
        archiveBaseName.set("netlib")
        archiveVersion.set(rootProject.version.toString())
    }

//    val dokkaJavadocJar by registering(Jar::class) {
//        description = "A Javadoc JAR containing Dokka Javadoc"
//        from(dokkaGeneratePublicationJavadoc.flatMap { it.outputDirectory })
//        archiveClassifier.set("javadoc")
//    }
}
