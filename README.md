<div align="center">

# 🏥 BookMyHealth

<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
<img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white" alt="Firebase"/>
<img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design"/>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white" alt="Gradle"/>
<img src="https://img.shields.io/badge/Google%20Sign--In-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google Sign-In"/>
<img src="https://img.shields.io/badge/AI%20Integration-FF5722?style=for-the-badge&logo=google-cloud&logoColor=white" alt="AI"/>

<br/>

<img src="https://img.shields.io/badge/API-24%2B-brightgreen?style=flat-square" alt="Min SDK"/>
<img src="https://img.shields.io/badge/Target%20SDK-35-blue?style=flat-square" alt="Target SDK"/>
<img src="https://img.shields.io/badge/Version-1.1-orange?style=flat-square" alt="Version"/>
<img src="https://img.shields.io/badge/License-MIT-green?style=flat-square" alt="License"/>

<br/>

**A next-gen, AI-powered healthcare appointment booking Android application built with Kotlin and Firebase**

[![GitHub stars](https://img.shields.io/github/stars/yourusername/BookMyHealth.svg?style=social&label=Star)](https://github.com/yourusername/BookMyHealth)
[![GitHub forks](https://img.shields.io/github/forks/yourusername/BookMyHealth.svg?style=social&label=Fork)](https://github.com/yourusername/BookMyHealth)

---

[Features](#-features) • [Tech Stack](#-tech-stack) • [Installation](#-installation) • [Screenshots](#-screenshots) • [Contributing](#-contributing)

</div>

---

## 📱 About

**BookMyHealth** is a comprehensive healthcare management application that connects patients with doctors, enabling seamless appointment booking and management. This updated version integrates **AI-powered diagnostic tools**, **Digital QR-based appointment slips**, and a **Verifier system** for doctors, ensuring a frictionless patient experience.

<div align="center">

<img src="https://img.shields.io/badge/🏥-Healthcare-4CAF50?style=flat-square" alt="Healthcare"/>
<img src="https://img.shields.io/badge/🤖-AI%20Diagnostics-FF5722?style=flat-square" alt="AI"/>
<img src="https://img.shields.io/badge/👨‍⚕️-Doctors-FF9800?style=flat-square" alt="Doctors"/>
<img src="https://img.shields.io/badge/👤-Patients-AA00FF?style=flat-square" alt="Patients"/>
<img src="https://img.shields.io/badge/⚡-Verified-039BE5?style=flat-square" alt="Verified"/>

</div>

---

## ✨ Features

### 🔐 Authentication & Profile
- **Multi-Auth:** Email/Password and one-tap Google Sign-In.
- **Role-Based Access:** Dedicated interfaces for Patients and Doctors.
- **Verified Status:** Verified doctor badge system.
- **Profile Management:** Detailed portfolios including Awards, Achievements, and Certifications.

### 👤 Patient Features
- **AI Symptom Analyzer:** Select symptoms for an AI-powered health analysis to get insights on potential conditions and recommended specialists.
- **Drug Information Module:** Search medications to get instant data on dosage, usage, and safety directions.
- **Digital Appointment Slip:** Access your appointment details via QR Code, Token Number, and Reference ID. Save/Download slips directly to your gallery.
- **Smart Discovery:** Filter doctors by Specialization (ENT, Orthopedic, etc.), Experience, and Availability.

### 👨‍⚕️ Doctor Features
- **Verifier Mode:** Built-in QR Code scanner to instantly verify patient appointments at the clinic reception.
- **Appointment Control:** Manage requests in real-time (Approve/Reject).
- **Dashboard Overview:** Track total patients, upcoming schedules, and manage clinical availability.

### 🎨 UI/UX Features
- **Material Design 3:** Modern, accessible interface.
- **Lottie Animations:** Smooth interactions (Role selection, Loading, AI analysis results).
- **Dark Theme:** Full support for dark mode.

---



- **Splash Screen**: Animated app launch with fade effects
- **Intro Sliders**: Beautiful onboarding experience with ViewPager2
- **Lottie Animations**: Smooth, engaging animations throughout the app
  - Doctor role animation (`doctor_role.json`)
  - User role animation (`user_role.json`)
  - Login animation (`login.json`)
  - Doctor profile animation (`doctor_profile_anim.json`)
  - Filter animation (`filter.json`)
  - Empty state animation (`empty.json`)
  - Multiple slide animations for both roles
- **Material Design 3**: Modern, clean interface following Material guidelines
- **Dark Theme Support**: Built-in dark mode support
- **Smooth Animations**: Fade, scale, slide, and bounce animations
- **Custom Toast Messages**: Beautiful, contextual toast notifications
- **Drawer Navigation**: Easy navigation with Material Drawer
- **Pull-to-Refresh**: Refresh data with swipe gesture
- **Empty States**: Friendly empty state illustrations

---

### Technology Stack Overview

```mermaid
graph LR
    A[Kotlin] --> B[Android SDK]
    B --> C[MVVM Architecture]
    C --> D[Firebase]
    D --> E[Realtime Database]
    D --> F[Authentication]
    D --> G[Storage]
    C --> H[Material 3]
    C --> I[ML Kit/AI Logic]
    C --> J[Glide]
```

### Core Technologies

<img src="https://img.shields.io/badge/Kotlin-2.0.21-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/Android-API%2024--35-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
<img src="https://img.shields.io/badge/Gradle-8.5.0-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle"/>
<img src="https://img.shields.io/badge/Architecture-MVVM-FF6F00?style=for-the-badge&logo=android&logoColor=white" alt="MVVM"/>

</div>



### Android Libraries
| Library | Version | Purpose | Logo |
|---------|---------|---------|------|
| **AndroidX Core KTX** | 1.12.0 | Kotlin extensions for Android | <img src="https://img.shields.io/badge/AndroidX-1.12.0-3DDC84?style=flat-square" alt="AndroidX"/> |
| **AndroidX AppCompat** | 1.7.0 | Backward compatibility | <img src="https://img.shields.io/badge/AppCompat-1.7.0-3DDC84?style=flat-square" alt="AppCompat"/> |
| **Material Design** | 1.13.0 | Modern UI components | <img src="https://img.shields.io/badge/Material-1.13.0-757575?style=flat-square" alt="Material"/> |
| **ConstraintLayout** | 2.2.0 | Flexible layout system | <img src="https://img.shields.io/badge/ConstraintLayout-2.2.0-3DDC84?style=flat-square" alt="ConstraintLayout"/> |
| **RecyclerView** | 1.3.2 | Efficient list rendering | <img src="https://img.shields.io/badge/RecyclerView-1.3.2-3DDC84?style=flat-square" alt="RecyclerView"/> |
| **ViewPager2** | Latest | Intro sliders and carousels | <img src="https://img.shields.io/badge/ViewPager2-Latest-3DDC84?style=flat-square" alt="ViewPager2"/> |
| **Activity KTX** | 1.8.2 | Activity extensions | <img src="https://img.shields.io/badge/Activity%20KTX-1.8.2-3DDC84?style=flat-square" alt="Activity KTX"/> |

### Architecture Components
| Component | Version | Purpose | Logo |
|-----------|---------|---------|------|
| **Lifecycle ViewModel** | 2.8.7 | ViewModel for UI data | <img src="https://img.shields.io/badge/ViewModel-2.8.7-FF6F00?style=flat-square" alt="ViewModel"/> |
| **Lifecycle LiveData** | 2.8.7 | Observable data holders | <img src="https://img.shields.io/badge/LiveData-2.8.7-FF6F00?style=flat-square" alt="LiveData"/> |
| **ViewBinding** | Built-in | Type-safe view references | <img src="https://img.shields.io/badge/ViewBinding-Built--in-3DDC84?style=flat-square" alt="ViewBinding"/> |

### 🔥 Firebase Services
<div align="center">

<img src="https://img.shields.io/badge/Firebase-BOM%2034.5.0-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase BOM"/>
<img src="https://img.shields.io/badge/Firebase-Auth-039BE5?style=for-the-badge&logo=firebase&logoColor=white" alt="Firebase Auth"/>
<img src="https://img.shields.io/badge/Firebase-Database-FFA000?style=for-the-badge&logo=firebase&logoColor=white" alt="Firebase Database"/>
<img src="https://img.shields.io/badge/Firebase-Storage-FF6F00?style=for-the-badge&logo=firebase&logoColor=white" alt="Firebase Storage"/>

</div>

| Service | Version | Purpose | Logo |
|---------|---------|---------|------|
| **Firebase BOM** | 34.5.0 | Firebase dependency management | <img src="https://img.shields.io/badge/BOM-34.5.0-FFCA28?style=flat-square&logo=firebase" alt="Firebase BOM"/> |
| **Firebase Auth KTX** | 23.2.1 | Authentication (Email/Password, Google) | <img src="https://img.shields.io/badge/Auth-23.2.1-039BE5?style=flat-square&logo=firebase" alt="Firebase Auth"/> |
| **Firebase Realtime Database KTX** | 21.0.0 | Real-time data storage | <img src="https://img.shields.io/badge/Database-21.0.0-FFA000?style=flat-square&logo=firebase" alt="Firebase Database"/> |
| **Firebase Storage KTX** | 21.0.2 | Image and file storage | <img src="https://img.shields.io/badge/Storage-21.0.2-FF6F00?style=flat-square&logo=firebase" alt="Firebase Storage"/> |

### Third-Party Libraries
<div align="center">

<img src="https://img.shields.io/badge/Lottie-6.5.0-00D9FF?style=for-the-badge&logo=lottie&logoColor=white" alt="Lottie"/>
<img src="https://img.shields.io/badge/Glide-4.16.0-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Glide"/>
<img src="https://img.shields.io/badge/Google%20Sign--In-21.2.0-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google Sign-In"/>
<img src="https://img.shields.io/badge/Coroutines-1.8.1-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Coroutines"/>

</div>

| Library | Version | Purpose | Logo |
|---------|---------|---------|------|
| **Lottie** | 6.5.0 | Beautiful JSON-based animations | <img src="https://img.shields.io/badge/Lottie-6.5.0-00D9FF?style=flat-square" alt="Lottie"/> |
| **Glide** | 4.16.0 | Image loading and caching | <img src="https://img.shields.io/badge/Glide-4.16.0-4285F4?style=flat-square" alt="Glide"/> |
| **Google Play Services Auth** | 21.2.0 | Google Sign-In integration | <img src="https://img.shields.io/badge/Google%20Auth-21.2.0-4285F4?style=flat-square" alt="Google Auth"/> |
| **Kotlin Coroutines** | 1.8.1 | Asynchronous programming | <img src="https://img.shields.io/badge/Coroutines-1.8.1-0095D5?style=flat-square" alt="Coroutines"/> |

### Development Tools
- **Kotlin KAPT**: Annotation processing for Glide
- **JUnit 4**: Unit testing
- **Espresso**: UI testing
- **AndroidX Test**: Instrumentation testing

---

## 📱 App Flow

<div align="center">

```mermaid
graph TD
    A[Splash Screen] --> B[Role Selection]
    B --> C{Choose Role}

    %% User Flow
    C -->|User| D[User Login/Signup]
    D --> F[User Dashboard]
    F --> H[Browse Doctors]
    F --> I[My Appointments]
    F --> J[User Profile]
    F --> Q[AI Symptom Analyzer]
    F --> R[Drug Info Module]
    H --> K[Book Appointment]
    K --> P[Appointment Confirmed]
    I --> S[Digital Appointment Slip / QR]

    %% Doctor Flow
    C -->|Doctor| E[Doctor Login/Signup]
    E --> G[Doctor Dashboard]
    G --> L[Appointment Requests]
    G --> M[Approved Appointments]
    G --> N[Doctor Profile]
    G --> T[QR Verifier / Scanner]
    L --> O{Approve/Reject}
    O -->|Approve| M
```

</div>

---

## 🏗 Architecture

The app follows **MVVM (Model-View-ViewModel)** architecture pattern:

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  Activities  │  │  Fragments   │  │   Adapters   │  │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘  │
│         │                  │                  │          │
│         └──────────────────┼──────────────────┘          │
│                            │                             │
│                    ┌────────▼────────┐                   │
│                    │   ViewModels    │                   │
│                    │  (LiveData)     │                   │
│                    └────────┬────────┘                   │
└─────────────────────────────┼─────────────────────────────┘
                              │
┌─────────────────────────────┼─────────────────────────────┐
│                    ┌────────▼────────┐                   │
│                    │   Repository    │                   │
│                    │  (Firebase)     │                   │
│                    └────────┬────────┘                   │
│                             │                             │
│                    ┌────────▼────────┐                   │
│                    │   Data Models   │                   │
│                    └─────────────────┘                   │
└───────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────┼─────────────────────────────┐
│                    ┌────────▼────────┐                   │
│                    │    Firebase     │                   │
│                    │  (Auth, DB,     │                   │
│                    │   Storage)      │                   │
│                    └─────────────────┘                   │
└───────────────────────────────────────────────────────────┘
```

### 📱 Activities & Screens

<div align="center">

#### 🚀 Splash & Onboarding
| Activity | Description |
|:--------:|:-----------|
| `SplashActivity` | Animated splash screen with app logo |
| `LoadingActivity` | Loading screen with animations |
| `RoleSelectActivity` | Role selection (Doctor/User) with intro sliders |

#### 🔐 Authentication Screens
| Activity | Description |
|:--------:|:-----------|
| `UserLoginActivity` | User login with Email/Password & Google Sign-In |
| `UserSignupActivity` | User registration |
| `DoctorLoginActivity` | Doctor login with Email/Password & Google Sign-In |
| `DoctorSignupActivity` | Doctor registration |

#### 👤 User Screens
| Activity | Description |
|:--------:|:-----------|
| `UserDashboardActivity` | Main dashboard with doctor list, search, filters |
| `BookAppointmentActivity` | Book appointment with date/time picker |
| `UserAppointmentsActivity` | View all user appointments |
| `UserProfileActivity` | User profile management |

#### 👨‍⚕️ Doctor Screens
| Activity | Description |
|:--------:|:-----------|
| `DoctorDashboardActivity` | Main dashboard with appointment requests |
| `DoctorApprovedListActivity` | View approved appointments |
| `DoctorProfileActivity` | Doctor profile management with full details |

#### 🤖 AI-Powered & Chat Features
| Activity | Description |
|:--------:|:-----------|
| `ChatActivity` | AI-powered chatbot interface for health queries |
| `SymptomCheckerActivity` | AI symptom analysis tool for initial screening |
| `DrugSearchActivity` | AI/FDA integrated drug database search |

#### 📋 Appointments & Utilities
| Activity | Description |
|:--------:|:-----------|
| `AppointmentSlipActivity` | Displays digital appointment slip with QR verification |
| `BookingConfirmDialog` | Confirmation popup for successful appointment booking |

</div>

### 📂 Project Directory Structure

```text
com.example.bookmyhealth/
├── adapter/              # RecyclerView & ViewPager adapters
│   ├── AnalysisResultAdapter.kt
│   ├── AppointmentAdapter.kt
│   ├── BannerAdapter.kt
│   ├── ChatAdapter.kt
│   ├── DoctorListAdapter.kt
│   ├── IntroAdapter.kt
│   ├── LottieSliderAdapter.kt
│   ├── PosterAdapter.kt
│   ├── SuggestionAdapter.kt
│   ├── SymptomAdapter.kt
│   ├── UserAppointmentAdapter.kt
│   └── UserLottieAdapter.kt
│
├── data/
│   ├── model/            # Data classes
│   │   ├── AnalysisResult.kt
│   │   ├── Appointment.kt
│   │   ├── ChatMessage.kt
│   │   ├── Disease.kt
│   │   ├── Doctor.kt
│   │   ├── Drug.kt
│   │   ├── DrugMeta.kt
│   │   ├── DrugMetaResults.kt
│   │   ├── DrugResponse.kt
│   │   ├── IntroItem.kt
│   │   ├── OpenFda.kt
│   │   ├── SlideItem.kt
│   │   ├── Symptom.kt
│   │   ├── SymptomCategory.kt
│   │   ├── User.kt
│   │   └── UserSlideItem.kt
│   └── repository/       # Data access layer
│       ├── ChatRepository.kt
│       ├── DrugRepository.kt
│       └── FirebaseRepository.kt
│
├── network/              # API & Networking
│   ├── GeminiService.kt
│   ├── OpenFdaApi.kt
│   └── RetrofitClient.kt
│
├── ui/
│   ├── auth/             # Authentication screens
│   │   ├── RoleSelectActivity.kt
│   │   ├── doctor/
│   │   │   ├── DoctorApprovedListActivity.kt
│   │   │   ├── DoctorDashboardActivity.kt
│   │   │   ├── DoctorLoginActivity.kt
│   │   │   └── DoctorSignupActivity.kt
│   │   └── user/
│   │       ├── BookAppointmentActivity.kt
│   │       ├── UserAppointmentsActivity.kt
│   │       ├── UserDashboardActivity.kt
│   │       ├── UserLoginActivity.kt
│   │       └── UserSignupActivity.kt
│   ├── chat/             # AI Chat Features
│   │   └── ChatActivity.kt
│   ├── dialog/           # Dialogs & Popups
│   │   └── BookingConfirmDialog.kt
│   ├── drug/             # Drug Search & Info
│   │   └── DrugSearchActivity.kt
│   ├── profile/          # Profile Management
│   │   ├── DoctorProfileActivity.kt
│   │   └── UserProfileActivity.kt
│   ├── splash/           # Splash & Loading
│   │   ├── LoadingActivity.kt
│   │   └── SplashActivity.kt
│   └── symptom/          # Symptom Analysis
│       └── SymptomCheckerActivity.kt
│
├── utils/                # Helper classes
│   ├── CaptureActivityPortrait.kt
│   ├── Constants.kt
│   ├── JsonUtils.kt
│   ├── NotificationHelper.kt
│   ├── NotificationWorker.kt
│   ├── QRUtils.kt
│   └── SuperToast.kt
│
└── viewmodel/            # ViewModels (MVVM)
    ├── AppointmentViewModel.kt
    ├── AuthViewModel.kt
    ├── ChatViewModel.kt
    ├── DoctorViewModel.kt
    ├── DrugViewModel.kt
    └── SymptomViewModel.kt
```
---

## 📊 Data Models

<div align="center">

### Core Data Structures

</div>

### 👤 User Model
```kotlin
data class User(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var age: String = "",
    var address: String = "",
    var imageUrl: String = "",
    var role: String = "User"
)
```

### 👨‍⚕️ Doctor Model
```kotlin
data class Doctor(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var specialization: String = "",
    var experience: String = "",
    var availableSlots: String = "",
    var imageUrl: String = "",
    var about: String = "",
    var clinicName: String = "",
    var consultationFee: String = "",
    var achievements: String = "",
    var weeklyAvailability: List<String> = listOf(),
    var role: String = "Doctor"
)
```

### 📅 Appointment Model
```kotlin
data class Appointment(
    val appointmentId: String = "",
    val userId: String = "",
    val userName: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val date: String = "",
    val time: String = "",
    val slot: String = "",
    val status: String = "Pending"  // Pending, Approved, Rejected
)
```

<div align="center">

### AI & Symptom Checker Models

</div>

### Symptom
```kotlin
// Symptom Analysis
data class Symptom(
    val id: String,
    val name: String,
    val category: SymptomCategory,
    var isSelected: Boolean = false,
    var severity: Int = 1
)

enum class SymptomCategory(val displayName: String) {
    GENERAL, PAIN, DIGESTIVE, RESPIRATORY, SKIN, NEUROLOGICAL, OTHER
}

data class AnalysisResult(
    val diseaseName: String,
    val confidence: Int,
    val matchedSymptoms: List<String>,
    val doctorType: String
)

// Chatbot Models
data class ChatMessage(
    val message: String,
    val isUser: Boolean, // true = User, false = AI
    val time: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false
)
```

### External API Models (Drug Information)
```kotlin
data class DrugResponse(val results: List<Drug>?, val meta: DrugMeta?)

data class Drug(
    val id: String?,
    val openfda: OpenFda?,
    val purpose: List<String>?,
    val indications_and_usage: List<String>?,
    val dosage_and_administration: List<String>?,
    val warnings: List<String>?,
    val contraindications: List<String>?
    // ... Additional utility getters (displayName, bestUsage, etc.)
)

data class OpenFda(
    val brand_name: List<String>?,
    val generic_name: List<String>?,
    val product_type: List<String>?,
    val route: List<String>?
)
```

### UI Helper Models
```kotlin
data class IntroItem(val icon: Int, val title: String, val description: String)
data class SlideItem(val animation: Int, val text: String)
data class UserSlideItem(val animation: Int, val text: String)
```

<div align="center">

<img src="https://img.shields.io/badge/📊-Data%20Models-1976D2?style=for-the-badge" alt="Data Models"/>
<img src="https://img.shields.io/badge/☁️-Firebase%20Realtime-FFA000?style=for-the-badge&logo=firebase" alt="Firebase"/>

</div>

---

## 🎨 Color Scheme

<div align="center">

### Primary Colors

<img src="https://img.shields.io/badge/Primary%20Blue-0D99FF-0D99FF?style=for-the-badge" alt="Primary Blue"/>
<img src="https://img.shields.io/badge/Success%20Green-4CAF50-4CAF50?style=for-the-badge" alt="Green"/>
<img src="https://img.shields.io/badge/Error%20Red-F44336-F44336?style=for-the-badge" alt="Red"/>
<img src="https://img.shields.io/badge/Warning%20Orange-FF9800-FF9800?style=for-the-badge" alt="Orange"/>
<img src="https://img.shields.io/badge/Accent%20Purple-AA00FF-AA00FF?style=for-the-badge" alt="Purple"/>

</div>

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio** (Hedgehog | 2023.1.1 or later)
- **JDK 11** or higher
- **Android SDK** (API 24 - 35)
- **Firebase Account** (for backend services)
- **Google Account** (for Google Sign-In setup)

---

## 🚀 Installation

<div align="center">

### ⚡ Quick Start Guide

<img src="https://img.shields.io/badge/Step%201-Clone%20Repo-1976D2?style=for-the-badge" alt="Step 1"/>
<img src="https://img.shields.io/badge/Step%202-Firebase%20Setup-FF9800?style=for-the-badge" alt="Step 2"/>
<img src="https://img.shields.io/badge/Step%203-Configure-4CAF50?style=for-the-badge" alt="Step 3"/>
<img src="https://img.shields.io/badge/Step%204-Build%20%26%20Run-AA00FF?style=for-the-badge" alt="Step 4"/>

</div>

> **Note**: Replace `yourusername` in GitHub URLs with your actual GitHub username before using the links.

---

### 1. Clone the Repository

<div align="center">

<img src="https://img.shields.io/badge/Git-Required-F05032?style=flat-square&logo=git&logoColor=white" alt="Git"/>
<img src="https://img.shields.io/badge/GitHub-Required-181717?style=flat-square&logo=github&logoColor=white" alt="GitHub"/>

</div>

```bash
git clone https://github.com/yourusername/BookMyHealth.git
cd BookMyHealth
```

### 2. Firebase Setup

<div align="center">

<img src="https://img.shields.io/badge/Firebase-Required-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase Required"/>

</div>

#### Create Firebase Project

<div align="center">

[![Firebase Console](https://img.shields.io/badge/Open-Firebase%20Console-FFCA28?style=flat-square&logo=firebase&logoColor=black)](https://console.firebase.google.com/)

</div>

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or use existing one
3. Enable the following services:

<div align="center">

<img src="https://img.shields.io/badge/✅-Authentication-039BE5?style=flat-square&logo=firebase" alt="Auth"/>
<img src="https://img.shields.io/badge/✅-Realtime%20Database-FFA000?style=flat-square&logo=firebase" alt="Database"/>
<img src="https://img.shields.io/badge/✅-Storage-FF6F00?style=flat-square&logo=firebase" alt="Storage"/>

</div>

   - **Authentication** (Email/Password & Google Sign-In)
   - **Realtime Database**
   - **Storage**

#### Configure Google Sign-In
1. In Firebase Console → Authentication → Sign-in method
2. Enable **Google** sign-in provider
3. Add your app's SHA-1 fingerprint:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```

#### Add Firebase Configuration
1. Download `google-services.json` from Firebase Console
2. Place it in `app/` directory

#### Configure Realtime Database
1. Go to Firebase Console → Realtime Database
2. Create database in **Asia-Southeast1** region (or update URL in code)
3. Set rules:
   ```json
   {
     "rules": {
       "users": {
         ".read": "auth != null",
         ".write": "auth != null"
       },
       "doctors": {
         ".read": true,
         ".write": "auth != null"
       },
       "appointments": {
         ".read": "auth != null",
         ".write": "auth != null"
       }
     }
   }
   ```

### 3. Update Google Sign-In Configuration
Update the Web Client ID in `app/src/main/res/values/strings.xml`:
```xml
<string name="default_web_client_id">
    YOUR_WEB_CLIENT_ID_HERE
</string>
```

### 4. Build and Run
```bash
# Using Gradle Wrapper
./gradlew assembleDebug

# Or open in Android Studio and click Run
```

---

## 📸 Screenshots

### 🚀 Onboarding & Authentication
| Role Selection | Login | Signup |
|:---:|:---:|:---:|
| <img src="Screenshots/Role_Select.png" width="200"> | <img src="Screenshots/Login.png" width="200"> | <img src="Screenshots/Signup.png" width="200"> |

### 👤 User Dashboard & Booking
| Dashboard | Doctor List | Filtering | Booking Confirmed |
|:---:|:---:|:---:|:---:|
| <img src="Screenshots/Dashboard.png" width="200"> | <img src="Screenshots/Doctors.png" width="200"> | <img src="Screenshots/Filter.png" width="200"> | <img src="Screenshots/Confirmed.png" width="200"> |

### 🤖 AI-Powered Health Features
| Symptom Analyzer | Analysis Results | Drug Information | Detailed Drug Info |
|:---:|:---:|:---:|:---:|
| <img src="Screenshots/Analyzer.png" width="200"> | <img src="Screenshots/Results.png" width="200"> | <img src="Screenshots/Drug_Info.png" width="200"> | <img src="Screenshots/Detailed_Drug_Info.png" width="200"> |

### 👨‍⚕️ Doctor Portal & Verification
| Doctor Dashboard | Pending Requests | QR Verifier Mode | Profile & Certs |
|:---:|:---:|:---:|:---:|
| <img src="Screenshots/Doc_Dash.png" width="200"> | <img src="Screenshots/Requests.png" width="200"> | <img src="Screenshots/QR_Scanner.png" width="200"> | <img src="Screenshots/Doc_Profile.png" width="200"> |

### 📋 Appointments & Profile
| Appointment Slip | Slip QR Code | User Profile | Side Menu |
|:---:|:---:|:---:|:---:|
| <img src="Screenshots/Slip.png" width="200"> | <img src="Screenshots/QR_Slip.png" width="200"> | <img src="Screenshots/Profile.png" width="200"> | <img src="Screenshots/Drawer.png" width="200"> |

## 🔧 Configuration

### Firebase Database URL
The app uses a regional Firebase Realtime Database. Update the URL in:
- `FirebaseRepository.kt`
- `SplashActivity.kt`
- `DoctorDashboardActivity.kt`
- `UserDashboardActivity.kt`

Current URL: `https://bookmyhealth-5920a-default-rtdb.asia-southeast1.firebasedatabase.app/`

### Build Configuration
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 35 (Android 15)
- **Compile SDK**: 35
- **Version Code**: 1
- **Version Name**: 1.0

---

## 🎯 Key Features Implementation

### Authentication Flow
```kotlin
// Email/Password Registration
FirebaseRepository.registerUser(name, email, password, role) { success, message ->
    // Handle result
}

// Google Sign-In
FirebaseRepository.signInWithGoogleIdToken(idToken, role) { success, message ->
    // Handle result
}
```

### Real-time Data Sync
```kotlin
// Listen to appointment changes
database.child("appointments")
    .orderByChild("doctorId")
    .equalTo(doctorId)
    .addValueEventListener { snapshot ->
        // Update UI in real-time
    }
```

### Image Loading
```kotlin
// Glide for profile images
Glide.with(context)
    .load(imageUrl)
    .placeholder(R.drawable.placeholder)
    .into(imageView)
```

---

## 🧪 Testing

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

---

## 📦 Dependencies

All dependencies are managed through:
- **Gradle Version Catalog** (`gradle/libs.versions.toml`)
- **Firebase BOM** (for version consistency)

See `app/build.gradle.kts` for complete dependency list.

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Maintain MVVM architecture pattern

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Your Name**
- GitHub: [Satwik51](https://github.com/Satwik51)
- Email: raiakki51@gmail.com

---

## 🙏 Acknowledgments

<div align="center">

### Special Thanks to These Amazing Technologies & Libraries

<table>
<tr>
<td align="center">
<a href="https://firebase.google.com/">
<img src="https://img.shields.io/badge/Firebase-Backend%20Services-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase"/>
</a>
<br/>
Backend Services
</td>
<td align="center">
<a href="https://lottiefiles.com/">
<img src="https://img.shields.io/badge/Lottie-Animations-00D9FF?style=for-the-badge&logo=lottie&logoColor=white" alt="Lottie"/>
</a>
<br/>
Beautiful Animations
</td>
</tr>
<tr>
<td align="center">
<a href="https://material.io/">
<img src="https://img.shields.io/badge/Material-Design%20Guidelines-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design"/>
</a>
<br/>
Design Guidelines
</td>
<td align="center">
<a href="https://github.com/bumptech/glide">
<img src="https://img.shields.io/badge/Glide-Image%20Loading-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Glide"/>
</a>
<br/>
Image Loading
</td>
</tr>
</table>

</div>

### Libraries & Tools Used

<div align="center">

[![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black)](https://firebase.google.com/)
[![Lottie](https://img.shields.io/badge/Lottie-00D9FF?style=flat-square)](https://lottiefiles.com/)
[![Material Design](https://img.shields.io/badge/Material%20Design-757575?style=flat-square&logo=material-design)](https://material.io/)
[![Glide](https://img.shields.io/badge/Glide-4285F4?style=flat-square&logo=google)](https://github.com/bumptech/glide)
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=flat-square&logo=kotlin)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android)](https://developer.android.com/)

</div>

---

## 📞 Support

If you have any questions or need help, please:
- Open an [Issue](https://github.com/Satwik51/BookMyHealth/issues)
- Contact: raiakki51@gmail.com

---

<div align="center">

## 🌟 Show Your Support

**Made with ❤️ using Kotlin and Firebase**

<div>

<img src="https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase"/>
<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>

</div>

---

### ⭐ Star this repo if you find it helpful!

[![GitHub stars](https://img.shields.io/github/stars/yourusername/BookMyHealth.svg?style=social&label=Star)](https://github.com/yourusername/BookMyHealth)
[![GitHub forks](https://img.shields.io/github/forks/yourusername/BookMyHealth.svg?style=social&label=Fork)](https://github.com/yourusername/BookMyHealth)
[![GitHub watchers](https://img.shields.io/github/watchers/yourusername/BookMyHealth.svg?style=social&label=Watch)](https://github.com/yourusername/BookMyHealth)

---

<div align="center">

### 📊 Project Statistics

<img src="https://img.shields.io/github/languages/count/yourusername/BookMyHealth?style=for-the-badge" alt="Languages"/>
<img src="https://img.shields.io/github/languages/top/yourusername/BookMyHealth?style=for-the-badge" alt="Top Language"/>
<img src="https://img.shields.io/github/repo-size/yourusername/BookMyHealth?style=for-the-badge" alt="Repo Size"/>
<img src="https://img.shields.io/github/last-commit/yourusername/BookMyHealth?style=for-the-badge" alt="Last Commit"/>

</div>

---

<div align="center">

### 🏆 Featured On

[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/yourusername/BookMyHealth)
[![Android](https://img.shields.io/badge/Android%20Apps-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://play.google.com/store/apps)

</div>

---

**BookMyHealth** - Your Complete Healthcare Solution 🏥

</div>

