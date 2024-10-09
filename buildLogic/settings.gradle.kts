rootProject.name = "buildLogic"
dependencyResolutionManagement {
    versionCatalogs {
        val libs by creating {
            from(files("../libs.versions.toml"))
        }
    }
}
