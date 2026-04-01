// settings.gradle.kts
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    // Optional: define plugin versions centrally (not strictly required here because root sets them)
}

dependencyResolutionManagement {
    // Prevent plugins/projects from adding their own repositories
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // All artifact repositories for the build
    repositories {
        google()
        mavenCentral()

        // ✅ CORRECT (Kotlin DSL)
        maven("https://jitpack.io")
    }
}

rootProject.name = "BookMyHealth"
include(":app")
