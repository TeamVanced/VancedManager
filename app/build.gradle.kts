plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(30)

    defaultConfig {
        applicationId = "com.vanced.manager"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 210
        versionName = "2.1.0 (CyberManager2077)"

        vectorDrawables.useSupportLibrary = true

        buildConfigField("String[]", "MANAGER_LANGUAGES", "{" + getLanguages() + "}")
        buildConfigField("Boolean", "ENABLE_CROWDIN_AUTH", "false")
        buildConfigField("String", "CROWDIN_HASH", "\"${System.getenv("CROWDIN_HASH")}\"")
        buildConfigField("String", "CROWDIN_CLIENT_ID", "\"${System.getenv("CROWDIN_CLIENT_ID")}\"")
        buildConfigField("String", "CROWDIN_CLIENT_SECRET", "\"${System.getenv("CROWDIN_CLIENT_SECRET")}\"")
    }

    lintOptions {
        disable("MissingTranslation", "ExtraTranslation")
    }

    applicationVariants.all {
        resValue("string", "versionName", versionName)
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        dataBinding = true // ObservableField migrate to flow or liveData
        viewBinding = true
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/*.kotlin_module")
    }

// To inline the bytecode built with JVM target 1.8 into
// bytecode that is being built with JVM target 1.6. (e.g. navArgs)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }

}

fun getLanguages(): String {
    val langs = arrayListOf("en", "bn_BD", "bn_IN", "pt_BR", "pt_PT", "zh_CN", "zh_TW")
    val exceptions = arrayOf("bn", "pt", "zh")
    val pattern = "-(\\w+)-".toRegex()

    File("$projectDir/src/main/res").listFiles()?.forEach { dir ->
        if (dir.name.startsWith("values-") && !dir.name.contains("v23")) {
            val match = pattern.find(dir.name)
            if (match != null) {
                val matchedDir = match.value.removeSurrounding("-")
                if (!exceptions.any { matchedDir == it }) {
                    langs.add(matchedDir)
                }
            }
        }
    }
    return langs.joinToString(", ") { "\"$it\"" }
}

dependencies {

    implementation(project(":core-presentation"))
    implementation(project(":core-ui"))

    implementation(project(":library-network"))

// Kotlin
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

// AndroidX
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.2")
    implementation("androidx.preference:preference-ktx:1.1.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Appearance
    implementation("com.github.madrapps:pikolo:2.0.1")
    implementation("com.google.android.material:material:1.3.0-beta01")

    // JSON parser
    implementation("com.beust:klaxon:5.4")

    // Crowdin
    implementation("com.crowdin.platform:mobile-sdk:1.2.0")

    // Tips
    implementation("com.github.florent37:viewtooltip:1.2.2")

    // HTTP networking
    implementation("com.github.kittinunf.fuel:fuel:2.3.0")
    implementation("com.github.kittinunf.fuel:fuel-coroutines:2.2.3")
    implementation("com.github.kittinunf.fuel:fuel-json:2.2.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Root permissions
    implementation("com.github.topjohnwu.libsu:core:3.0.2")
    implementation("com.github.topjohnwu.libsu:io:3.0.2")

    // Layout
    implementation("com.google.android:flexbox:2.0.1")

    // Firebase
    implementation("com.google.firebase:firebase-analytics-ktx:18.0.0")
    implementation("com.google.firebase:firebase-crashlytics:17.3.0")
    implementation("com.google.firebase:firebase-messaging:21.0.1")
    implementation("com.google.firebase:firebase-perf:19.0.11")
}
