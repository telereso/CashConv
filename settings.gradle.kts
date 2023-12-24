rootProject.name = "CashConv"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://s01.oss.sonatype.org/content/groups/staging") }
    }
    versionCatalogs {
        create("kmpLibs") {
            from("io.telereso.kmp:catalog:0.32")
            version("teleresoCore","0.0.48-alpha.169")
        }
    }
}

include(":composeApp")
include(":lib")
project(":lib").name = rootProject.name.lowercase() + "-client"
include(":models")
project(":models").name = rootProject.name.lowercase() + "-models"
include(":jvmApi")
