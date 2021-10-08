plugins {
	kotlin("jvm")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2-native-mt")

	val kotestVersion = "4.4.3"
	testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
	testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	testImplementation("io.kotest:kotest-property:$kotestVersion")

	testImplementation("io.mockk:mockk:1.12.0")
}