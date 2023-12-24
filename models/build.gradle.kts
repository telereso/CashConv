import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(kmpLibs.plugins.kotlin.multiplatform)
    alias(kmpLibs.plugins.kotlin.serialization)
    alias(kmpLibs.plugins.kotlinx.kover)
    alias(kmpLibs.plugins.android.library)
    alias(kmpLibs.plugins.dokka)
    alias(kmpLibs.plugins.telereso.kmp)

    id("maven-publish")
}

// Setup extras variables
val groupId: String by rootProject.extra
val scope: String by rootProject.extra

// Setup publish variables
val baseProjectName = rootProject.name.replace("-client", "")
project.ext["artifactName"] = "${project.name}"

group = "$groupId.${project.name}"
version = project.findProperty("publishVersion") ?: "0.0.1"

publishing {
    repositories {
        maven {
            url = uri("${project.findProperty("artifactoryUrl") ?: "test"}/mobile-gradle")
            credentials {
                username = (project.findProperty("artifactoryUser") ?: "test") as String
                password = (project.findProperty("artifactoryPassword") ?: "test") as String
            }
        }
    }
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    jvm {
        //withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }


    js(IR) {
        moduleName = "@$scope/${project.name}"
        version = project.version as String


        browser()

        nodejs()

        binaries.library()
        binaries.executable()
        generateTypeScriptDefinitions()
    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs()

    sourceSets {

        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }

        val commonMain by getting {
            dependencies {
                api(kmpLibs.telereso.core)

                implementation(kmpLibs.bundles.kotlinx)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation(npm("@js-joda/core", kmpLibs.versions.js.joda.core.get()))
            }
        }
        val jsTest by getting
    }
}

android {
    namespace = "${(group as String).replace("-", ".")}"
    compileSdk = kmpLibs.versions.compileSdk.get().toInt()
    buildFeatures {
        buildConfig = false
    }
    defaultConfig {
        minSdk = kmpLibs.versions.minSdk.get().toInt()
    }
    resourcePrefix = "${rootProject.name.replace("-", "_")}_"
    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_${kmpLibs.versions.java.get()}")
        targetCompatibility = JavaVersion.valueOf("VERSION_${kmpLibs.versions.java.get()}")
    }
}

tasks.dokkaHtml.configure {
    // Set module name displayed in the final output
    moduleName.set(project.name)
    outputDirectory.set(
        rootDir.resolve(
            "docs${
                project.findProperty("publishVersion")?.let { "/$it" } ?: ""
            }/models"
        )
    )
}
