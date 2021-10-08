plugins {
	id("com.android.library")
	kotlin("android")
}

android {
	compileSdkVersion(30)

	defaultConfig {
		minSdkVersion(16)
		targetSdkVersion(30)

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
	implementation("androidx.appcompat:appcompat:1.3.1")
	implementation("androidx.core:core-ktx:1.6.0")
	implementation("androidx.fragment:fragment-ktx:1.3.6")
	implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:2.3.1")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
	implementation("com.google.android.material:material:1.4.0")
	implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

	implementation("com.squareup.retrofit2:retrofit:2.9.0")

	val moshiVersion = "1.12.0"
	implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
	implementation("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
	implementation("com.squareup.moshi:moshi-adapters:$moshiVersion")

	implementation("com.squareup.okhttp3:logging-interceptor:4.9.2")

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

	implementation("io.insert-koin:koin-android:3.1.2")

	val kotestVersion = "5.0.0.M2"
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("io.kotest:kotest-property:$kotestVersion")

	val mockkVersion = "1.12.0"
	testImplementation("io.mockk:mockk:$mockkVersion")

	androidTestImplementation("io.mockk:mockk-android:$mockkVersion")

	val androidxTestVersion = "1.4.0"
	androidTestImplementation("androidx.test:core:$androidxTestVersion")
	androidTestImplementation("androidx.test:runner:$androidxTestVersion")
	androidTestImplementation("androidx.test:rules:$androidxTestVersion")
}