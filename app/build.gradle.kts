import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("plugin.serialization")
}

val composeVersion = "1.1.1"

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31

        applicationId = "com.vanced.manager"

        versionCode = 3000
        versionName = "3.0.0 (Re@Composed)"

        vectorDrawables.useSupportLibrary = true

        buildConfigField("String[]", "MANAGER_LANGUAGES", "{$languages}")
    }

    lint {
        disable += "MissingTranslation"
        disable += "ExtraTranslation"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/*.kotlin_module")
    }

    // To inline the bytecode built with JVM target 1.8 into
    // bytecode that is being built with JVM target 1.6. (e.g. navArgs)
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

}


tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        optIn("androidx.compose.material3.ExperimentalMaterial3Api")
        optIn("androidx.compose.animation.ExperimentalAnimationApi")
        optIn("androidx.compose.foundation.ExperimentalFoundationApi")
    }
}

fun KotlinJvmOptions.optIn(library: String) {
    freeCompilerArgs = freeCompilerArgs +
            "-opt-in=$library"
}

val languages: String get() {
    val langs = arrayListOf("en", "bn_BD", "bn_IN", "pa_IN", "pa_PK", "pt_BR", "pt_PT", "zh_CN", "zh_TW")
    val exceptions = arrayOf("bn", "pa", "pt", "zh")

    File("$projectDir/src/main/res").listFiles()?.filter {
        val name = it.name
        name.startsWith("values") && !name.contains("v23") && !name.contains("night")
    }?.forEach { dir ->
        val dirname = dir.name.substringAfter("-").substringBefore("-")
        if (!exceptions.contains(dirname)) {
            langs.add(dirname)
        }
    }
    return langs.joinToString(", ") { "\"$it\"" }
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // AndroidX
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.browser:browser:1.4.0")
    implementation("androidx.preference:preference-ktx:1.2.0")
    implementation("androidx.activity:activity-compose:1.4.0")

    val lifecycleVersion = "2.4.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    // Compose
    implementation("androidx.compose.compiler:compiler:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material-icons-core:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha06")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-util:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")

    // Google
    implementation("com.google.android.material:material:1.5.0")

    val accompanistVersion = "0.22.1-rc"
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-placeholder-material:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

    // Other
    implementation("com.github.zsoltk:compose-router:0.28.0")

    implementation("io.coil-kt:coil-compose:1.4.0")

    implementation("com.github.skydoves:orchestra-colorpicker:1.1.0")

    val libsuVersion = "3.2.1"
    implementation("com.github.topjohnwu.libsu:core:$libsuVersion")
    implementation("com.github.topjohnwu.libsu:io:$libsuVersion")
    implementation("com.github.topjohnwu.libsu:busybox:$libsuVersion")

    val koinVersion = "3.1.3"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
