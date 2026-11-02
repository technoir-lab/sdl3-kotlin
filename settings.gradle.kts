pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        mavenLocal()
    }
    plugins {
        val conventionPluginsVersion = "v62"
        id("io.technoirlab.cmake-import") version "1.4.0"
        id("io.technoirlab.conventions.android-application") version conventionPluginsVersion
        id("io.technoirlab.conventions.kotlin-multiplatform-application") version conventionPluginsVersion
        id("io.technoirlab.conventions.kotlin-multiplatform-library") version conventionPluginsVersion
        id("io.technoirlab.conventions.root") version conventionPluginsVersion
        id("io.technoirlab.conventions.settings") version conventionPluginsVersion
    }
}

plugins {
    id("io.technoirlab.cmake-import") apply false
    id("io.technoirlab.conventions.android-application") apply false
    id("io.technoirlab.conventions.kotlin-multiplatform-application") apply false
    id("io.technoirlab.conventions.kotlin-multiplatform-library") apply false
    id("io.technoirlab.conventions.root") apply false
    id("io.technoirlab.conventions.settings")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

globalSettings {
    projectId = "sdl3-kotlin"

    metadata {
        description = "SDL3 bindings for Kotlin Multiplatform."
        developer(name = "technoir", email = "technoir.dev@gmail.com")
        license(name = "The Apache Software License, Version 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.txt")
    }
}

include(":sample")
include(":sample:android")
include(":sdl3")
