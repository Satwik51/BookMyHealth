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
        versionCode = 2
        versionName = "1.1"
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

    // 1. ZXing Scanner (QR Code scan karne ke liye)
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    // 2. Glide (Firebase se Patient ki photo load karne ke liye)
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    // 3. Material Design (Buttons aur Cards ke liye agar error aaye)
    implementation ("com.google.android.material:material:1.11.0")

    implementation("com.google.zxing:core:3.5.4")

    // WorkManager Dependency (Kotlin version)
    implementation("androidx.work:work-runtime-ktx:2.10.0")

// Agar Concurrent ya Coroutine issues aa rahe hon toh ye bhi:
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // 🔥 Network (Gemini API)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // JSON parsing
    implementation("org.json:json:20240303")

    // Lifecycle (MVVM)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")

    // RecyclerView (chat UI)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    implementation("com.google.mlkit:text-recognition:16.0.1")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("com.tom-roush:pdfbox-android:2.0.27.0")

    implementation("com.google.android.material:material:1.11.0")

    // 🔥 Retrofit (API call ke liye)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
// 🔥 JSON convert (API response → Kotlin object)
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
// 🔥 Coroutines (background me API call)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
// 🔥 Lifecycle ViewModel + LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")

    implementation("com.google.code.gson:gson:2.13.2")
    // --------------------------
    // AndroidX Core + UI
    // --------------------------
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")

    // --------------------------
    // Lifecycle + ViewModel + LiveData
    // --------------------------
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.10.0")

    // --------------------------
    // Glide
    // --------------------------
    implementation("com.github.bumptech.glide:glide:5.0.5")
    implementation(libs.vision.common)
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation(libs.play.services.mlkit.text.recognition)
    implementation(libs.androidx.cardview)
    kapt("com.github.bumptech.glide:compiler:5.0.5")

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

    implementation("com.google.firebase:firebase-messaging-ktx:24.1.2")

    // Firebase KTX modules
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-auth-ktx:23.2.1")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.2")

    // --------------------------
    // Google Sign-In
    // --------------------------
    implementation("com.google.android.gms:play-services-auth:21.5.1")

    // --------------------------
    // Coroutines
    // --------------------------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

    // --------------------------
    // Lottie
    // --------------------------
    implementation("com.airbnb.android:lottie:6.7.1")
}
