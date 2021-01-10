plugins {
	kotlin("jvm")
}

java {
	sourceCompatibility = JavaVersion.VERSION_1_8
	targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
	implementation(kotlin("stdlib"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")

	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.1")
	testImplementation("io.kotest:kotest-runner-junit5:4.3.2")
	testImplementation("io.kotest:kotest-assertions-core:4.3.2")
	testImplementation("io.kotest:kotest-property:4.3.2")
	testImplementation("io.mockk:mockk:1.10.4")
}