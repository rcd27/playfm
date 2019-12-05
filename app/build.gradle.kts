import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.api.BaseVariantOutput
import com.android.build.gradle.internal.api.BaseVariantOutputImpl

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("org.jlleitschuh.gradle.ktlint")
    id("kotlin-kapt")
}

tasks.withType(JavaCompile::class.java) {
    /* Dagger Compiler options, see: https://dagger.dev/compiler-options */
    // Turn on Dagger Compiler incremental build
    options.compilerArgs.add("-Adagger.gradle.incremental")
    // Enable Dagger Compiler full binding path validation cause compilation error if something is missing
    options.compilerArgs.add("-Adagger.fullBindingGraphValidation=ERROR")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "com.rcd27.playfm"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // The following argument makes the Android Test Orchestrator run its
        // "pm clear" command after each test invocation. This command ensures
        // that the app's state is completely cleared between tests.
        testInstrumentationRunnerArgument("clearPackageData", "true")
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            buildConfigField("String", "API_URL", "\"https://v2api.play.fm/\"")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        // TODO: turn on code obfuscation!
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            buildConfigField("String", "API_URL", "\"https://v2api.play.fm/\"")
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
        }
        // FIXME: try to clean-up
        // Ugly but works. If try to move to lambdas call - doesn't work
        applicationVariants.all(object : Action<ApplicationVariant> {
            override fun execute(variant: ApplicationVariant) {
                variant.outputs.all(object : Action<BaseVariantOutput> {
                    override fun execute(t: BaseVariantOutput) {
                        val output = t as BaseVariantOutputImpl
                        val variantName = variant.buildType.name
                        output.outputFileName =
                            "Pikabu_test-case$variantName-v${defaultConfig.versionName}.apk"
                    }
                })
            }
        })
        flavorDimensions("api")
        productFlavors {
            create("mock") {
                setDimension("api")
                versionNameSuffix = "-mockApi"
            }
            create("net") {
                setDimension("api")
                versionNameSuffix = "-netApi"
            }
        }
        testOptions {
            execution = "ANDROIDX_TEST_ORCHESTRATOR"
            unitTests.isIncludeAndroidResources = true
        }
        lintOptions.isAbortOnError = false
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }

    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.50")

        // UI
        implementation("com.google.android.material:material:1.2.0-alpha02")
        implementation("androidx.appcompat:appcompat:1.1.0")
        implementation("androidx.constraintlayout:constraintlayout:1.1.3")
        implementation("androidx.fragment:fragment:1.2.0-rc03")

        // RecyclerView extensions
        implementation("com.hannesdorfmann:adapterdelegates4:4.2.0")
        implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl:4.2.0")
        implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:4.2.0")

        // Network
        implementation("com.squareup.retrofit2:retrofit:2.6.2")
        implementation("com.squareup.retrofit2:converter-gson:2.6.0")
        implementation("com.squareup.retrofit2:adapter-rxjava2:2.4.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.0.0")

        // Dependency Injection
        implementation("com.google.dagger:dagger:2.25.2")
        kapt("com.google.dagger:dagger-compiler:2.25.2")

        // Extensions
        implementation("androidx.core:core-ktx:1.1.0")

        // RxJava
        implementation("io.reactivex.rxjava2:rxjava:2.2.13")
        implementation("io.reactivex.rxjava2:rxandroid:2.1.0")
        implementation("com.jakewharton.rxrelay2:rxrelay:2.1.0")

        // Navigation
        implementation("androidx.navigation:navigation-fragment:2.2.0-rc03")
        implementation("androidx.navigation:navigation-ui:2.2.0-rc03")

        // Load images
        implementation("com.squareup.picasso:picasso:2.71828")
        implementation("com.github.chrisbanes:PhotoView:2.0.0")

        // Date
        implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")

        // Testing
        testImplementation("junit:junit:4.12")
        testImplementation("com.google.truth:truth:1.0")
        testImplementation("org.robolectric:robolectric:3.1.2")
        testImplementation("com.google.truth:truth:1.0")

        androidTestImplementation("androidx.test:runner:1.2.0")
        androidTestImplementation("androidx.test:core:1.2.0")
        androidTestImplementation("androidx.test:core-ktx:1.2.0")
        androidTestImplementation("androidx.test.ext:junit:1.1.1")
        androidTestImplementation("androidx.test.ext:junit-ktx:1.1.1")
        androidTestImplementation("androidx.test:rules:1.2.0")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
        androidTestImplementation("com.google.truth:truth:1.0")
        androidTestUtil("androidx.test:orchestrator:1.2.0")
    }

    ktlint {
        android.set(true)
        ignoreFailures.set(false) // We need this if we want to keep the code base clean
    }
}