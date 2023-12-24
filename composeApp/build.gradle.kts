import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    alias(kmpLibs.plugins.kotlin.multiplatform)
    alias(kmpLibs.plugins.android.application)
    alias(libs.plugins.jetbrainsCompose)
    id("shot")
}


kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = kmpLibs.versions.java.get()
            }
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        moduleName = "composeApp"
//        browser {
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//            }
//        }
//        binaries.executable()
//    }
    
    sourceSets {
        all {
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }

        val desktopMain by getting
        val androidMain by getting {
            dependencies {
                implementation(libs.compose.ui)
                implementation(libs.compose.ui.tooling.preview)
                implementation(libs.androidx.activity.compose)
            }
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        val commonMain by getting {
            dependencies {
                api(project(":cashconv-client"))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(kmpLibs.ktor.client.darwin)
                implementation(kmpLibs.sqldelight.native.driver)
                //implementation("app.cash.paging:paging-runtime:$pagingVersion")
            }
        }
    }
}

android {
    namespace = "io.telereso.cashconv.app"
    testNamespace = "io.telereso.cashconv.app.test"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "io.telereso.cashconv.app"
        testApplicationId = "io.telereso.cashconv.app.test"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "com.karumi.shot.ShotTestRunner"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.valueOf("VERSION_${kmpLibs.versions.java.get()}")
        targetCompatibility = JavaVersion.valueOf("VERSION_${kmpLibs.versions.java.get()}")
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = kmpLibs.versions.java.get()
        }
    }

    flavorDimensions += "manifest"
    productFlavors {
        create("normal") {
            dimension = "manifest"
            isDefault = true
        }

        create("shot") {
            dimension = "manifest"
        }
    }


    dependencies {
        debugImplementation(libs.compose.ui.tooling)
        debugImplementation(libs.test.compose.ui.manifest)
        androidTestImplementation(libs.androidx.rules)
        androidTestImplementation(libs.androidx.junit.ktx)
        androidTestImplementation(libs.androidx.test.junit)
        androidTestImplementation(libs.test.compose.ui.junit4)
    }
    shot {
        applicationId = "io.telereso.cashconv.app"
        tolerance = 1.0 // 1,0% tolerance
//        showOnlyFailingTestsInReports = true
    }

    tasks.register("startEmulator") {

        doFirst {
            // Define the file you want to make executable
            listOf(
                "scripts/emulator/start.sh",
                "scripts/emulator/start.command",
                "scripts/emulator/wait.sh",
                "scripts/emulator/create.sh",
                "scripts/emulator/disable_animations.sh"
            ).forEach {
                val executableFile = rootProject.rootDir.toPath().resolve(it).toFile()
                executableFile.setExecutable(true)
                println("Executable permissions set for ${executableFile}")
            }

        }

        doLast {
            // Define the command and arguments for your external process
            val command = listOf("bash", "scripts/emulator/start.sh")

            // Create a ProcessBuilder instance
            val processBuilder = ProcessBuilder(command)

            // Set the working directory (optional)
            processBuilder.directory(rootProject.rootDir)

            // Start the external process
            processBuilder.start()

        }
    }

    tasks.register("waitEmulator") {
        dependsOn("startEmulator")
        doFirst {
            // Define the file you want to make executable
            listOf(
                "scripts/emulator/wait.sh",
            ).forEach {
                val executableFile = rootProject.rootDir.toPath().resolve(it).toFile()
                executableFile.setExecutable(true)
                println("Executable permissions set for ${executableFile}")
            }

        }

        doLast {
            // Define the command and arguments for your external process
            val command = listOf("bash", "scripts/emulator/wait.sh")

            // Create a ProcessBuilder instance
            val processBuilder = ProcessBuilder(command)

            // Set the working directory (optional)
            processBuilder.directory(rootProject.rootDir)

            // Start the external process
            val process = processBuilder.start()

            // Wait for the process to complete
            val exitCode = process.waitFor()

            // Print the process output (stdout and stderr)
            println("Process Output:")
            println(process.inputStream.read())
            println("Exit Code: $exitCode")
        }
    }


    tasks.register("stopEmulator") {
        doFirst {
            // Define the file you want to make executable
            listOf(
                "scripts/emulator/stop.sh",
                "scripts/emulator/stop.command",
                "scripts/emulator/wait.sh",
                "scripts/emulator/enable_animations.sh"
            ).forEach {
                val executableFile = rootProject.rootDir.toPath().resolve(it).toFile()
                executableFile.setExecutable(true)
                println("Executable permissions set for ${executableFile}")
            }

        }

        doLast {
            // Define the command and arguments for your external process
            val command = listOf("bash", "scripts/emulator/stop.sh")

            // Create a ProcessBuilder instance
            val processBuilder = ProcessBuilder(command)

            // Set the working directory (optional)
            processBuilder.directory(rootProject.rootDir)

            // Start the external process
            processBuilder.start()
        }
    }

    afterEvaluate {
        tasks.getByName("connectedShotDebugAndroidTest")
            .dependsOn("waitEmulator")

        tasks.getByName("shotDebugExecuteScreenshotTests")
            .dependsOn("waitEmulator")
            .finalizedBy("stopEmulator")

    }
}
dependencies {
    implementation("androidx.core:core-ktx:+")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.telereso.cashconv.app"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}