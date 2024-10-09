plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.kotlin.gradlePlugin)
    implementation(libs.dokka.gradlePlugin)
}

gradlePlugin {
    plugins {
        val loaderCommonPlugin by creating {
            id = "loader-common"
            implementationClass = "LoaderCommonPlugin"
        }
        val loaderDokkaPlugin by creating {
            id = "loader-dokka"
            implementationClass = "LoaderDokkaPlugin"
        }
        val loaderTargetPlugin by creating {
            id = "loader-runtime"
            implementationClass = "LoaderRuntimePlugin"
        }
    }
}
