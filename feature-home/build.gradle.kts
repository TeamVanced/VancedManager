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

	buildFeatures {
		viewBinding = true
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

android.testOptions {
	unitTests.all {
		it.useJUnitPlatform()
	}
}


dependencies {
	implementation(project(":core-ui"))
	implementation(project(":core-presentation"))
	implementation(project(":library-network"))

	implementation(kotlin("stdlib"))
	implementation("androidx.appcompat:appcompat:1.2.0")
	implementation("androidx.core:core-ktx:1.3.2")
	implementation("androidx.fragment:fragment-ktx:1.2.5")
	implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.2.0")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
	implementation("com.google.android.material:material:1.3.0-beta01")
	implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

	implementation("com.squareup.retrofit2:retrofit:2.9.0")

	implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
	implementation("com.squareup.moshi:moshi-kotlin-codegen:1.11.0")
	implementation("com.squareup.moshi:moshi-adapters:1.11.0")

	implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")

	implementation("org.koin:koin-android:2.2.1")
	implementation("org.koin:koin-android-viewmodel:2.2.1")
	implementation("org.koin:koin-android-ext:2.2.1")

	testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
	testImplementation("io.kotest:kotest-assertions-core:4.3.2")
	testImplementation("io.kotest:kotest-property:4.3.2")
	testImplementation("io.mockk:mockk:1.10.4")

	androidTestImplementation("io.mockk:mockk-android:1.10.4")
	androidTestImplementation("androidx.test:core:1.3.0")
	androidTestImplementation("androidx.test:runner:1.3.0")
	androidTestImplementation("androidx.test:rules:1.3.0")
}