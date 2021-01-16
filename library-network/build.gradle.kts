plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    compileSdkVersion(30)
    
    defaultConfig {
        minSdkVersion(16)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
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

dependencies {
    implementation(kotlin("stdlib"))
    implementation("androidx.core:core-ktx:1.3.2")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    implementation("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
    implementation("com.squareup.moshi:moshi-adapters:1.11.0")
    
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")

    implementation("org.koin:koin-android:2.2.1")
    implementation("org.koin:koin-android-viewmodel:2.2.1")
    implementation("org.koin:koin-android-ext:2.2.1")
}