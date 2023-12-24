plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.jetbrainsCompose) apply false

    alias(kmpLibs.plugins.kotlin.android) apply false
    alias(kmpLibs.plugins.android.application) apply false
    alias(kmpLibs.plugins.android.library) apply false
    alias(kmpLibs.plugins.kotlin.jvm) apply false
    alias(kmpLibs.plugins.kotlin.multiplatform) apply false
    alias(kmpLibs.plugins.kotlin.serialization) apply false
    alias(kmpLibs.plugins.kotlin.parcelize) apply false
    alias(kmpLibs.plugins.kotlin.native.cocoapods) apply false
    alias(kmpLibs.plugins.buildkonfig) apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }

    dependencies {
        classpath("com.karumi:shot:${libs.versions.test.shot.get()}")
    }
}