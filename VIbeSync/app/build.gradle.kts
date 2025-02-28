plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.vibesync"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.vibesync"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true  // ✅ Enable Jetpack Compose
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"  // ✅ Latest Compose Compiler
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Core AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Jetpack Compose BOM (Recommended)
    implementation(platform("androidx.compose:compose-bom:2024.02.02"))

    // Jetpack Compose dependencies
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3") // Material 3
    implementation("androidx.activity:activity-compose:1.8.2") // Compose Activity
    implementation("androidx.navigation:navigation-compose:2.7.6") // Navigation (if needed)

    // Firebase
    implementation("com.google.firebase:firebase-auth-ktx:22.0.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")

    // Image loading
    implementation("io.coil-kt:coil-compose:2.2.2")

    // Hilt for Compose Navigation (if using Dependency Injection)
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    // Debugging
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.accompanist:accompanist-placeholder:0.31.1-alpha")
    implementation("io.coil-kt:coil-compose:2.0.0")
    implementation("io.coil-kt:coil-svg:2.0.0")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.0.0")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.31.0-alpha")
//    implementation('com.squareup.retrofit2:retrofit:2.9.0')
//    implementation('com.squareup.retrofit2:converter-gson:2.9.0')
//    implementation('com.squareup.okhttp3:logging-interceptor:4.9.3')

}
