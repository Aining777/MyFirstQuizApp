plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.dailyquizapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dailyquizapp"
        minSdk = 26 // Ensure this is 26 or higher for LocalDate
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // 修正了 testInstrumentationRunner 的语法错误
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 移除了 vectorDrawables 块，因为它可能导致 "Unexpected tokens" 错误
        // vectorDrawables {
        //     useSupportLibrary true
        // }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)
    androidTestImplementation(composeBom) // Added for androidTest to use Compose BOM

    // Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation ("androidx.navigation:navigation-compose:2.7.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation ("androidx.navigation:navigation-compose:2.7.0")
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.0")
    implementation ("androidx.compose.material3:material3:1.1.0")
    implementation ("androidx.compose.runtime:runtime:1.6.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0")
    implementation ("androidx.navigation:navigation-compose:2.7.0")
    implementation ("androidx.compose.material:material-icons-extended:1.6.0")

    // Material Components (retained from your version)
    implementation("com.google.android.material:material:1.11.0")

    // JSON Support
    implementation("com.google.code.gson:gson:2.10.1")

    // Added: Compose Runtime Saveable (required for rememberSaveable, mutableIntStateOf)
    implementation("androidx.compose.runtime:runtime-saveable")

    // Added: Kotlin Coroutines (for robust coroutine handling, especially with StateFlow)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // --- Added: Essential Test Dependencies to fix build errors ---
    // JUnit for local unit tests
    testImplementation("junit:junit:4.13.2")

    // AndroidX Test Core
    androidTestImplementation("androidx.test:core:1.5.0")

    // AndroidX JUnit for instrumented tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    // AndroidX Espresso for UI tests
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // AndroidX Test Runner and Rules (for RunWith and InstrumentationRegistry)
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    // --- End of Test Dependencies ---
}
