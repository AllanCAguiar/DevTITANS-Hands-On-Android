plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
    alias(libs.plugins.ksp) // Kotlin Symbol Processing
    alias(libs.plugins.hilt) // Hilt Gradle Plugin
}

android {
    namespace = "com.example.plaintext"
    compileSdk = 35 // Considere usar uma versão estável se 35 for preview

    defaultConfig {
        applicationId = "com.example.plaintext"
        minSdk = 30
        targetSdk = 34 // Mantenha targetSdk igual ou menor que compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom)) // Importa o BOM do Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview) // Para previews no Android Studio
    implementation(libs.androidx.material3)

    // Room - Usando KSP
    implementation(libs.androidx.room.runtime) // Alias correto para runtime
    implementation(libs.androidx.room.ktx)     // Alias correto para Kotlin extensions
    ksp(libs.androidx.room.compiler)        // Alias correto para o compiler com KSP

    // Navigation
    implementation(libs.navigation.compose)

    // Kotlinx Serialization
    implementation(libs.kotlinx.serialization.json)

    // Hilt - Dependency Injection
    implementation(libs.hilt.android)           // Hilt runtime
    ksp(libs.hilt.compiler)             // Hilt compiler com KSP (assumindo alias correto no TOML)
    implementation(libs.androidx.hilt.navigation.compose) // Hilt integration for Navigation Compose

    // Testes
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Para testes de UI do Compose
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging (Tooling para Compose)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

hilt {
    enableAggregatingTask = false
}
