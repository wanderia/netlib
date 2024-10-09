import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.dokka.gradle.DokkaExtension

class LoaderDokkaPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("org.jetbrains.dokka")


            extensions.configure<DokkaExtension> {
                // todo: exclude testmod sources
                dokkaSourceSets.configureEach {
                    sourceLink {
                        remoteUrl("https://github.com/wanderia/netlib")
                        localDirectory.set(rootDir)
                    }
                }
            }
        }
    }
}
