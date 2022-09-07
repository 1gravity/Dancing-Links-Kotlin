buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("jvm")
}

group = "com.onegravity.dlx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(Kotlin.stdlib)
    implementation(KotlinX.coroutines.core)

    // Tests
    testImplementation(Testing.junit.jupiter.engine)
    testImplementation(KotlinX.coroutines.test)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}