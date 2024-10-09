import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project
import org.gradle.kotlin.dsl.withType
import org.gradle.language.jvm.tasks.ProcessResources
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class LoaderRuntimePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("loader-common")

            val commonKotlin = configurations.create("commonKotlin") {
                isCanBeResolved = true
            }

            val commonJava = configurations.create("commonJava") {
                isCanBeResolved = true
            }

            val commonResources = configurations.create("commonResources") {
                isCanBeResolved = true
            }

            dependencies {
                "compileOnly"(project(path = ":mod:common")) {
                    capabilities {
                        requireCapability("dev.wanderia:netlib")
                    }
                }

                commonKotlin(project(path = ":mod:common", configuration = "commonKotlin"))
                commonJava(project(path = ":mod:common", configuration = "commonJava"))
                commonResources(project(path = ":mod:common", configuration = "commonResources"))
            }

            tasks.withType<KotlinCompile> {
                dependsOn(commonKotlin)
                source(commonKotlin)
            }

            tasks.withType<JavaCompile> {
                dependsOn(commonJava)
                source(commonJava)
            }

            tasks.withType<ProcessResources> {
                dependsOn(commonResources)
                from(commonResources)
            }

            tasks.named("sourcesJar", Jar::class.java) {
                dependsOn(commonKotlin)
                from(commonKotlin)
                dependsOn(commonJava)
                from(commonJava)
                dependsOn(commonResources)
                from(commonResources)
            }
        }
    }
}
