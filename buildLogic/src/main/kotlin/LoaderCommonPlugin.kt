import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePluginExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.repositories
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

class LoaderCommonPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.kotlin.jvm")

            extensions.configure<BasePluginExtension> {
                archivesName = "${rootProject.name}-${project.name}"
            }

            extensions.configure<JavaPluginExtension> {
                toolchain.languageVersion.set(JavaLanguageVersion.of(21))
                withSourcesJar()
            }
            repositories {
                mavenCentral()
            }

            extensions.configure<KotlinJvmProjectExtension> {
                jvmToolchain(21)
                explicitApi()
            }

            listOf(
                "apiElements",
                "mainSourceElements",
                "runtimeElements",
            ).forEach { variant ->
                configurations.named(variant) {
                    outgoing {
                        capability("dev.wanderia:netlib-${project.name}:$version")
                        capability("dev.wanderia:netlib:$version")
                    }
                }
            }
        }
    }
}
