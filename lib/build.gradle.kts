import org.gradle.configurationcache.extensions.capitalized
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import groovy.util.Node
import groovy.xml.XmlParser
import java.text.DecimalFormat
import java.math.RoundingMode
import org.jetbrains.kotlin.incremental.createDirectory
import org.codehaus.groovy.runtime.ProcessGroovyMethods
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(kmpLibs.plugins.android.library)
    alias(kmpLibs.plugins.kotlin.multiplatform)
    alias(kmpLibs.plugins.kotlin.native.cocoapods)
    alias(kmpLibs.plugins.kotlin.serialization)
    alias(kmpLibs.plugins.kotlin.parcelize)
    alias(kmpLibs.plugins.kotlinx.kover)
    alias(kmpLibs.plugins.test.logger)
    alias(kmpLibs.plugins.dokka)
    alias(kmpLibs.plugins.buildkonfig)
    alias(kmpLibs.plugins.telereso.kmp)

    id("maven-publish")
}


// Packaging
val groupId: String by rootProject.extra
val scope: String by rootProject.extra

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

    cocoapods {
        version = (project.properties["publishVersion"] ?: "0.0.1-place-holder") as String?
        summary = "Client sdk for feature"
        homepage = "https://www.cashconv.com/"
        ios.deploymentTarget = "12.0"
        name = "CashconvClient"

        // Optional properties
        // Configure the Pod name here instead of changing the Gradle project name
        name = "CashconvClient"

        framework {
            baseName = "CashconvClient"

            // Optional properties
            // Dynamic framework support
            isStatic = false

            /**
             * working with multi-module. we need to export the external modules
             * run ./gradlew podPublishDebugXCFramework once completed
             * check the project podspec contains the exported
             */
            export(project(":cashconv-models"))

            export(kmpLibs.telereso.core)

            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.BITCODE)
        }
    }


    jvm {
        compilations.all {
//            kotlinOptions.jvmTarget = "1.8"
        }
        //withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    js(IR) {
        moduleName = "@$scope/${project.name}"
        version = project.version as String

        browser {
            testTask {
                useMocha()
            }
        }

        nodejs {
            testTask {
                useMocha()
            }
        }

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
                api(project(":cashconv-models"))
                api(kmpLibs.telereso.core)

                implementation(kmpLibs.bundles.ktor)

                implementation(kmpLibs.bundles.kotlinx)

                implementation(kmpLibs.bundles.sqldelight)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(kmpLibs.test.kotlinx.coroutines.test)
                implementation(kmpLibs.bundles.test.kotest)
                // Ktor Server Mock
                implementation(kmpLibs.test.ktor.client.mock)

                implementation(kmpLibs.test.turbine)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kmpLibs.ktor.client.okhttp)
                implementation(kmpLibs.okhttp.logging)
                implementation(kmpLibs.sqldelight.runtime.jvm)
            }
        }

        val jvmTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kmpLibs.sqldelight.sqlite.driver)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(kmpLibs.ktor.client.okhttp)
                implementation(kmpLibs.okhttp.logging)
                implementation(kmpLibs.sqldelight.android.driver)

            }
        }
        val androidUnitTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kmpLibs.sqldelight.sqlite.driver)
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
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting {
            dependsOn(commonTest)
        }
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
        }

        /**
         * Adding main and test for JS.
         */
        val jsMain by getting {
            dependencies {
                implementation(kmpLibs.ktor.client.js)

                implementation(kmpLibs.sqldelight.sqljs.driver)

                implementation(npm("sql.js", kmpLibs.versions.sqlJs.get()))
                implementation(npm("@js-joda/core", kmpLibs.versions.js.joda.core.get()))
            }
        }
        val jsTest by getting {
            dependsOn(commonTest)
            dependencies {
                implementation(kmpLibs.sqldelight.sqljs.driver)
            }
        }
    }

    /**
     * Since Kotlin 1.5.20
     * https://kotlinlang.org/docs/whatsnew1520.html#opt-in-export-of-kdoc-comments-to-generated-objective-c-headers
     * Note this is The ability to export KDoc comments to generated Objective-C headers is Experimental. It may be dropped or changed at any time
     * To try out this ability to export KDoc comments to Objective-C headers, use the -Xexport-kdoc compiler option. :)
     */
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations.get("main").kotlinOptions.freeCompilerArgs += "-Xexport-kdoc"
    }
}

buildkonfig {
    //  Set the package name where BuildKonfig is being placed. Required.
    packageName = "$groupId.${project.name.replace("-", ".")}"
    // objectName Set the name of the generated object. Defaults to BuildKonfig.
    // objectName = "YourAwesomeConfig"
    // exposeObjectWithName Set the name of the generated object, and make it public.
    // exposeObjectWithName = "YourAwesomePublicConfig"

    //defaultConfigs Set values which you want to have in common. Required.
    defaultConfigs {
        buildConfigField(
            STRING,
            "SDK_VERSION",
            "${project.properties["publishVersion"] ?: "0.0.0"}"
        )
        buildConfigField(STRING, "SDK_NAME", rootProject.name)
    }
}

dependencies {
    kover(project(":cashconv-models"))
}

/**
 * https://kotlin.github.io/dokka/1.6.0/user_guide/gradle/usage/
 */
tasks.dokkaHtml.configure {
    // Set module name displayed in the final output
    moduleName.set(rootProject.name.split("-").joinToString(" ") { it.capitalized() })

    outputDirectory.set(
        rootDir.resolve(
            "docs/client${
                project.findProperty("publishVersion")?.let { "/$it" } ?: ""
            }"
        )
    )

    dokkaSourceSets {
        configureEach { // Or source set name, for single-platform the default source sets are `main` and `test`

            // Used when configuring source sets manually for declaring which source sets this one depends on
            // dependsOn("module")

            // Used to remove a source set from documentation, test source sets are suppressed by default
            //suppress.set(false)

            // Use to include or exclude non public members THIS IS DEPRACATED
            // includeNonPublic.set(true)

            /**
             * includeNonPublic is currently deprcated. recommened way to expose private or internal classes and funs is using this approach
             * we define the visbilites we are interersted in.
             * note this will make all private funs or clases or interfaces or val public on the doc level.
             * use suppress annotation to reomve any classes of fun you dont want part of the doc.
             * In our project we have classes with no package. the doc displats them in a root,. by rifght we chsould have a packages for each.
             */
            documentedVisibilities.set(
                setOf(
                    org.jetbrains.dokka.DokkaConfiguration.Visibility.PUBLIC, // Same for both Kotlin and Java
                    org.jetbrains.dokka.DokkaConfiguration.Visibility.PRIVATE, // Same for both Kotlin and Java
                    // DokkaConfiguration.Visibility.PROTECTED, // Same for both Kotlin and Java
                    org.jetbrains.dokka.DokkaConfiguration.Visibility.INTERNAL, // Kotlin-specific internal modifier
                    //  DokkaConfiguration.Visibility.PACKAGE, // Java-specific package-private visibility
                )
            )

            // Do not output deprecated members. Applies globally, can be overridden by packageOptions
            skipDeprecated.set(false)

            // Emit warnings about not documented members. Applies globally, also can be overridden by packageOptions
            reportUndocumented.set(true)

            // Do not create index pages for empty packages
            skipEmptyPackages.set(true)

            // This name will be shown in the final output
            // displayName.set("JVM")

            // Platform used for code analysis. See the "Platforms" section of this readme
            // platform.set(org.jetbrains.dokka.Platform.jvm)


            // Allows to customize documentation generation options on a per-package basis
            // Repeat for multiple packageOptions
            // If multiple packages match the same matchingRegex, the longuest matchingRegex will be used
//            perPackageOption {
//                matchingRegex.set("kotlin($|\\.).*") // will match kotlin and all sub-packages of it
//                // All options are optional, default values are below:
//                skipDeprecated.set(false)
//                reportUndocumented.set(true) // Emit warnings about not documented members
//                includeNonPublic.set(false)
//            }
            // Suppress a package
//            perPackageOption {
//                matchingRegex.set(""".*\.internal.*""") // will match all .internal packages and sub-packages
//                suppress.set(true)
//            }

            // Include generated files in documentation
            // By default Dokka will omit all files in folder named generated that is a child of buildDir
            //  suppressGeneratedFiles.set(false)
        }
    }
}

tasks.register<Copy>("copyiOSTestResources") {
    from("${rootDir}/lib/src/commonTest/resources")
    into("${rootDir}/lib/build/bin/iosSimulatorArm64/debugTest/resources")
}
tasks.findByName("iosSimulatorArm64Test")?.dependsOn("copyiOSTestResources")

/**
 * this task will run when ./gradlew iosSimulatorArm64Test testing Darwin test.
 * When we do not specificy the device ID, KMP will fallback to Iphone 12 which now
 * not supported by Xcode 14.
 * I set it to the latest supported which is the iPhone 14.
 */
tasks.named(
    "iosSimulatorArm64Test",
    org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest::class.java
).configure {
    device = kmpLibs.versions.test.iphone.device.get()
}

android {
    namespace = "$groupId.${project.name.replace("-",".")}"
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

// We can filter out some classes in the generated report
koverReport {
    defaults {
        html {
            setReportDir(rootDir.resolve("docs/unit-test"))
        }
    }

    val exclude =
        listOf(
            "*.*Genre*",
            "$groupId..cache.*",
            "*.CashconvClientManagerKt",
            "*PlatformJVMKt",
            "*CashconvClientDatabaseDriverFactory",
            "*PlatformKt",
            "*ConvertersKt"
        )
    filters {
        excludes {
            classes(exclude)
        }
    }
    // The koverVerify currently only supports line counter values.
    // we can also configure this to run after the unit tests task.
    verify {
        // Add VMs in the includes [list]. VMs added,their coverage % will be tracked.
        filters {
            excludes {
                classes(exclude)
            }
        }
        // Enforce Test Coverage
        rule("Minimal line coverage rate in percent") {
            bound {
                minValue = 80
            }
        }
    }

}

testlogger {

    theme =
        com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA // pick a theme - mocha, standard or plain
    showExceptions = true // show detailed failure logs
    showStackTraces = true
    showFullStackTraces =
        false // shows full exception stack traces,  useful to see the entirety of the stack trace.
    showCauses = true

    /**
     * sets threshold in milliseconds to highlight slow tests,
     * any tests that take longer than 0.5 seconds to run would have their durations logged using a warning style
     * and those that take longer than 1 seconds to run using an error style.
     */
    slowThreshold = 1000

    showSummary =
        true // displays a breakdown of passes, failures and skips along with total duration
    showSimpleNames = false
    showPassed = true
    showSkipped = true
    showFailed = true
    showOnlySlow = false
    /**
     * filter the log output based on the type of the test result.
     */
    showStandardStreams = true
    showPassedStandardStreams = true
    showSkippedStandardStreams = true
    showFailedStandardStreams = true

    logLevel = LogLevel.LIFECYCLE
}

teleresoKmp {
    disableJsonConverters = true
    enableReactNativeExport = false
}


fun String.execute(): Process = ProcessGroovyMethods.execute(this)
fun Process.text(): String = ProcessGroovyMethods.getText(this)


fun download(url: String, path: String) {
    val destFile = File(path)
    if (!destFile.exists())
        destFile.createNewFile()
    ant.invokeMethod("get", mapOf("src" to url, "dest" to destFile))
}

tasks.register("createCoverageBadge") {
    doLast {

        val report = buildDir.resolve("reports/kover/report.xml")
        val coverage = if (report.exists()) {
            val node = (groovy.xml.XmlParser().parse(report)
                .children()
                .first { (it as Node).attribute("type") == "LINE" } as Node)

            val missed = node.attribute("missed").toString().toDouble()
            val covered = node.attribute("covered").toString().toDouble()
            val total = missed + covered
            val coverage = DecimalFormat("#.#").apply {
                roundingMode = RoundingMode.UP
            }.format((covered * 100) / total)
            coverage.toDouble()
        } else {
            null
        }

        val badgeColor = when {
            coverage == null -> "inactive"
            coverage >= 90 -> "brightgreen"
            coverage >= 65 -> "green"
            coverage >= 50 -> "yellowgreen"
            coverage >= 35 -> "yellow"
            coverage >= 20 -> "orange"
            else -> "red"
        }

        val koverDir = rootDir.resolve("docs/unit-test").apply {
            if (!exists())
                createDirectory()
        }
        download(
            "https://img.shields.io/badge/coverage-${coverage?.toString().plus("%25") ?: "unknown"}-$badgeColor",
            koverDir.resolve("badge.svg").path
        )
    }
}


tasks.findByName("koverHtmlReport")?.apply {
    finalizedBy("createCoverageBadge")
}


tasks.findByName("koverXmlReport")?.apply {
    finalizedBy("createCoverageBadge")
}