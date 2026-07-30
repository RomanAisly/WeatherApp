import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use {
        localProperties.load(it)
    }
}

android {
    namespace = "com.weatherapp.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 29
        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY") ?: ""}\"")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.bundles.ktor)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.bundles.koin)
}