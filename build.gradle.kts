// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        classpath("com.google.gms:google-services:4.3.4")
        classpath("com.google.firebase:perf-plugin:1.3.4")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.4.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.2")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}