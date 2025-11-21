// ============================
// 📱 app/build.gradle.kts (FINAL FIXED)
// ============================

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
}

android {
    namespace = "com.example.bookmyhealth"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bookmyhealth"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {

    // --------------------------
    // AndroidX Core + UI
    // --------------------------
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // --------------------------
    // Lifecycle + ViewModel + LiveData
    // --------------------------
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    // --------------------------
    // Glide
    // --------------------------
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    // --------------------------
    // Testing
    // --------------------------
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // --------------------------
    // Firebase BOM (LATEST)
    // --------------------------
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))

    // Firebase KTX modules
    implementation("com.google.firebase:firebase-auth-ktx:23.2.1")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.2")

    // --------------------------
    // Google Sign-In
    // --------------------------
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // --------------------------
    // Coroutines
    // --------------------------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // --------------------------
    // Lottie
    // --------------------------
    implementation("com.airbnb.android:lottie:6.5.0")
}
