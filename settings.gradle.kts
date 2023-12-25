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
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven { url = uri("https://s01.oss.sonatype.org/content/groups/staging") }
        maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental") }
    }
    versionCatalogs {
        create("kmpLibs") {
            from("io.telereso.kmp:catalog:0.32")
            version("teleresoCore","0.0.48-alpha.169")
            version("coroutines","1.8.0-RC2")
        }
    }

    versionCatalogs {
        create("kmpLibsWasm") {
            from("io.telereso.kmp:catalog:0.32-wasm")
        }
    }
}

include(":composeApp")
include(":lib")
project(":lib").name = rootProject.name.lowercase() + "-client"
include(":models")
project(":models").name = rootProject.name.lowercase() + "-models"
include(":jvmApi")
