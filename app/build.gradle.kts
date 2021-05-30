plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 21
        targetSdk = 30

        applicationId = "com.vanced.manager"

        versionCode = 3000
        versionName = "3.0.0 (Re@Composed)"

        vectorDrawables.useSupportLibrary = true

        buildConfigField("String[]", "MANAGER_LANGUAGES", "{$languages}")
    }

    lint {
        disable("MissingTranslation", "ExtraTranslation")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        useIR = true
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
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

    implementation("androidx.core:core-ktx:1.5.0")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.browser:browser:1.3.0")

    val composeVersion = "1.0.0-beta07"
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-util:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-core:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")

    implementation("androidx.activity:activity-compose:1.3.0-alpha08")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
    implementation("androidx.navigation:navigation-compose:2.4.0-alpha01")
    implementation("androidx.preference:preference-ktx:1.1.1")

    val accompanistVersion = "0.10.0"
    implementation("com.google.accompanist:accompanist-glide:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-swiperefresh:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")

    implementation("com.github.madrapps:pikolo:2.0.1")

    val koinVersion = "3.0.1"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    implementation("dev.burnoo:compose-remember-preference:0.3.0")

    implementation("com.github.x1nto:apkhelper:1.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}
