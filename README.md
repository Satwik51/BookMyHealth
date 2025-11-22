<div align="center">

# 🏥 BookMyHealth

<img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
<img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/Firebase-039BE5?style=for-the-badge&logo=Firebase&logoColor=white" alt="Firebase"/>
<img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design"/>
<img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white" alt="Gradle"/>
<img src="https://img.shields.io/badge/Google%20Sign--In-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google Sign-In"/>

<br/>

<img src="https://img.shields.io/badge/API-24%2B-brightgreen?style=flat-square" alt="Min SDK"/>
<img src="https://img.shields.io/badge/Target%20SDK-35-blue?style=flat-square" alt="Target SDK"/>
<img src="https://img.shields.io/badge/Version-1.0-orange?style=flat-square" alt="Version"/>
<img src="https://img.shields.io/badge/License-MIT-green?style=flat-square" alt="License"/>

<br/>

**A modern healthcare appointment booking Android application built with Kotlin and Firebase**

[![GitHub stars](https://img.shields.io/github/stars/yourusername/BookMyHealth.svg?style=social&label=Star)](https://github.com/yourusername/BookMyHealth)
[![GitHub forks](https://img.shields.io/github/forks/yourusername/BookMyHealth.svg?style=social&label=Fork)](https://github.com/yourusername/BookMyHealth)

---

[Features](#-features) • [Tech Stack](#-tech-stack) • [Architecture](#-architecture) • [Installation](#-installation) • [Screenshots](#-screenshots) • [Contributing](#-contributing)

</div>

---

## 📱 About

<div align="center">

### 🏥 BookMyHealth App Icon

<img src="https://img.shields.io/badge/📱-App%20Icon-0D99FF?style=for-the-badge" alt="App Icon"/>

> App icon available in `app/src/main/mipmap-*/ic_launcher.png`

<img src="https://img.shields.io/badge/BookMyHealth-Healthcare%20App-0D99FF?style=for-the-badge&logo=hospital&logoColor=white" alt="BookMyHealth"/>

</div>

**BookMyHealth** is a comprehensive healthcare management application that connects patients with doctors, enabling seamless appointment booking and management. The app features a dual-role system supporting both **Doctors** and **Users (Patients)**, with a beautiful, modern UI powered by Material Design 3 and smooth animations.

<div align="center">

<img src="https://img.shields.io/badge/🏥-Healthcare-4CAF50?style=flat-square" alt="Healthcare"/>
<img src="https://img.shields.io/badge/📅-Appointments-1976D2?style=flat-square" alt="Appointments"/>
<img src="https://img.shields.io/badge/👨‍⚕️-Doctors-FF9800?style=flat-square" alt="Doctors"/>
<img src="https://img.shields.io/badge/👤-Patients-AA00FF?style=flat-square" alt="Patients"/>
<img src="https://img.shields.io/badge/☁️-Cloud%20Sync-039BE5?style=flat-square" alt="Cloud Sync"/>

</div>

### Key Highlights
- 🎯 **Dual Role System**: Separate interfaces for Doctors and Patients
- 🔐 **Multiple Authentication**: Email/Password and Google Sign-In
- 📅 **Appointment Management**: Book, view, and manage appointments
- 🔍 **Smart Search & Filter**: Find doctors by specialization, experience, and availability
- 🎨 **Modern UI/UX**: Beautiful animations, Lottie effects, and Material Design
- ☁️ **Cloud-Powered**: Real-time data synchronization with Firebase

---

## ✨ Features

<div align="center">

### 🎯 Core Features Overview

<img src="https://img.shields.io/badge/🔐-Authentication-1976D2?style=for-the-badge" alt="Auth"/>
<img src="https://img.shields.io/badge/👨‍⚕️-Doctor%20Portal-FF9800?style=for-the-badge" alt="Doctor"/>
<img src="https://img.shields.io/badge/👤-Patient%20Portal-4CAF50?style=for-the-badge" alt="Patient"/>
<img src="https://img.shields.io/badge/📅-Appointments-AA00FF?style=for-the-badge" alt="Appointments"/>
<img src="https://img.shields.io/badge/🎨-Modern%20UI-00C853?style=for-the-badge" alt="UI"/>

</div>

### 🔐 Authentication & User Management
- **Email/Password Authentication**: Secure registration and login
- **Google Sign-In Integration**: One-tap authentication with Google accounts
- **Role-Based Access**: Separate login flows for Doctors and Users
- **Password Reset**: Forgot password functionality
- **Auto Login**: Persistent session management
- **Profile Management**: Complete profile setup and editing

### 👨‍⚕️ Doctor Features
- **Doctor Dashboard**: View all appointment requests in real-time
- **Appointment Management**: Approve or reject appointment requests
- **Profile Management**: Complete doctor profile with:
  - Specialization, Experience, Clinic Name
  - Consultation Fee, About Section
  - Weekly Availability Schedule
  - Achievements & Awards
  - Profile Image Upload
- **Appointment History**: Track all past and upcoming appointments
- **Search & Filter**: Filter appointments by status, date, patient name

### 👤 User (Patient) Features
- **User Dashboard**: Browse available doctors with beautiful card layouts
- **Doctor Discovery**: 
  - Search doctors by name, specialization
  - Filter by specialization, experience, availability
  - View doctor profiles with complete details
- **Appointment Booking**: 
  - Select date and time slots
  - Book appointments with preferred doctors
  - View appointment status (Pending/Approved/Rejected)
- **My Appointments**: Track all booked appointments
- **Profile Management**: Edit personal information and profile picture

### 🎨 UI/UX Features

<div align="center">

<img src="https://img.shields.io/badge/🎨-Modern%20UI-AA00FF?style=for-the-badge" alt="UI"/>
<img src="https://img.shields.io/badge/✨-Animations-00D9FF?style=for-the-badge" alt="Animations"/>
<img src="https://img.shields.io/badge/🌙-Dark%20Mode-757575?style=for-the-badge" alt="Dark Mode"/>

</div>

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

## 🛠 Tech Stack

<div align="center">

### Technology Stack Overview

```mermaid
graph LR
    A[Kotlin] --> B[Android SDK]
    B --> C[MVVM Architecture]
    C --> D[Firebase]
    D --> E[Realtime Database]
    D --> F[Authentication]
    D --> G[Storage]
    C --> H[Material Design]
    C --> I[Lottie Animations]
    C --> J[Glide]
```

### Core Technologies

<img src="https://img.shields.io/badge/Kotlin-2.0.21-0095D5?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/Android-API%2024--35-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
<img src="https://img.shields.io/badge/Gradle-8.5.0-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle"/>
<img src="https://img.shields.io/badge/Architecture-MVVM-FF6F00?style=for-the-badge&logo=android&logoColor=white" alt="MVVM"/>

</div>

### Core Technologies
- **Language**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/kotlin/kotlin-original.svg" width="20" height="20"/> [Kotlin](https://kotlinlang.org/) **2.0.21**
- **Platform**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/android/android-original.svg" width="20" height="20"/> **Android** (API 24 - 35)
- **Build System**: <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/gradle/gradle-plain.svg" width="20" height="20"/> **Gradle 8.5.0** with Kotlin DSL
- **Architecture**: 🏗️ **MVVM** (Model-View-ViewModel)

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
    C -->|User| D[User Login/Signup]
    C -->|Doctor| E[Doctor Login/Signup]
    D --> F[User Dashboard]
    E --> G[Doctor Dashboard]
    F --> H[Browse Doctors]
    F --> I[My Appointments]
    F --> J[User Profile]
    H --> K[Book Appointment]
    G --> L[Appointment Requests]
    G --> M[Approved Appointments]
    G --> N[Doctor Profile]
    L --> O{Approve/Reject}
    O -->|Approve| M
    K --> P[Appointment Confirmed]
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

</div>

### Package Structure
```
com.example.bookmyhealth/
├── adapter/              # RecyclerView & ViewPager adapters
│   ├── AppointmentAdapter.kt
│   ├── DoctorListAdapter.kt
│   ├── IntroAdapter.kt
│   ├── LottieSliderAdapter.kt
│   ├── UserAppointmentAdapter.kt
│   └── UserLottieAdapter.kt
│
├── data/
│   ├── model/           # Data classes
│   │   ├── Appointment.kt
│   │   ├── Doctor.kt
│   │   ├── IntroItem.kt
│   │   ├── SlideItem.kt
│   │   ├── User.kt
│   │   └── UserSlideItem.kt
│   └── repository/      # Data access layer
│       └── FirebaseRepository.kt
│
├── ui/
│   ├── auth/           # Authentication screens
│   │   ├── RoleSelectActivity.kt
│   │   ├── doctor/     # Doctor auth screens
│   │   │   ├── DoctorLoginActivity.kt
│   │   │   ├── DoctorSignupActivity.kt
│   │   │   ├── DoctorDashboardActivity.kt
│   │   │   └── DoctorApprovedListActivity.kt
│   │   └── user/       # User auth screens
│   │       ├── UserLoginActivity.kt
│   │       ├── UserSignupActivity.kt
│   │       ├── UserDashboardActivity.kt
│   │       ├── BookAppointmentActivity.kt
│   │       └── UserAppointmentsActivity.kt
│   ├── profile/        # Profile management
│   │   ├── DoctorProfileActivity.kt
│   │   └── UserProfileActivity.kt
│   └── splash/        # Splash & loading screens
│       ├── LoadingActivity.kt
│       └── SplashActivity.kt
│
├── utils/              # Utility classes
│   └── SuperToast.kt
│
└── viewmodel/          # ViewModels
    ├── AppointmentViewModel.kt
    ├── AuthViewModel.kt
    └── DoctorViewModel.kt
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

<div align="center">

### 🚀 Splash & Onboarding

| Splash Screen | Role Selection | Intro Slider |
|:---:|:---:|:---:|
| <img src="https://via.placeholder.com/300x600/0D99FF/FFFFFF?text=Splash+Screen" width="200" alt="Splash Screen"/> | <img src="https://via.placeholder.com/300x600/4CAF50/FFFFFF?text=Role+Selection" width="200" alt="Role Selection"/> | <img src="https://via.placeholder.com/300x600/AA00FF/FFFFFF?text=Intro+Slider" width="200" alt="Intro Slider"/> |

### 🔐 Authentication

| User Login | User Signup | Doctor Login | Doctor Signup |
|:---:|:---:|:---:|:---:|
| <img src="https://via.placeholder.com/300x600/1976D2/FFFFFF?text=User+Login" width="150" alt="User Login"/> | <img src="https://via.placeholder.com/300x600/00C853/FFFFFF?text=User+Signup" width="150" alt="User Signup"/> | <img src="https://via.placeholder.com/300x600/1976D2/FFFFFF?text=Doctor+Login" width="150" alt="Doctor Login"/> | <img src="https://via.placeholder.com/300x600/00C853/FFFFFF?text=Doctor+Signup" width="150" alt="Doctor Signup"/> |

### 👤 User Features

| User Dashboard | Doctor List | Search & Filter | Book Appointment |
|:---:|:---:|:---:|:---:|
| <img src="https://via.placeholder.com/300x600/0D99FF/FFFFFF?text=User+Dashboard" width="150" alt="User Dashboard"/> | <img src="https://via.placeholder.com/300x600/4CAF50/FFFFFF?text=Doctor+List" width="150" alt="Doctor List"/> | <img src="https://via.placeholder.com/300x600/AA00FF/FFFFFF?text=Search+Filter" width="150" alt="Search & Filter"/> | <img src="https://via.placeholder.com/300x600/FF9800/FFFFFF?text=Book+Appointment" width="150" alt="Book Appointment"/> |

| Doctor Profile | My Appointments | User Profile |
|:---:|:---:|:---:|
| <img src="https://via.placeholder.com/300x600/1976D2/FFFFFF?text=Doctor+Profile" width="200" alt="Doctor Profile"/> | <img src="https://via.placeholder.com/300x600/00C853/FFFFFF?text=My+Appointments" width="200" alt="My Appointments"/> | <img src="https://via.placeholder.com/300x600/AA00FF/FFFFFF?text=User+Profile" width="200" alt="User Profile"/> |

### 👨‍⚕️ Doctor Features

| Doctor Dashboard | Appointment Requests | Approve/Reject | Approved List |
|:---:|:---:|:---:|:---:|
| <img src="https://via.placeholder.com/300x600/0D99FF/FFFFFF?text=Doctor+Dashboard" width="150" alt="Doctor Dashboard"/> | <img src="https://via.placeholder.com/300x600/FF9800/FFFFFF?text=Appointment+Requests" width="150" alt="Appointment Requests"/> | <img src="https://via.placeholder.com/300x600/4CAF50/FFFFFF?text=Approve+Reject" width="150" alt="Approve/Reject"/> | <img src="https://via.placeholder.com/300x600/00C853/FFFFFF?text=Approved+List" width="150" alt="Approved List"/> |

| Doctor Profile | Profile Management |
|:---:|:---:|
| <img src="https://via.placeholder.com/300x600/1976D2/FFFFFF?text=Doctor+Profile" width="200" alt="Doctor Profile"/> | <img src="https://via.placeholder.com/300x600/AA00FF/FFFFFF?text=Profile+Management" width="200" alt="Profile Management"/> |

> **Note**: Replace placeholder images with actual screenshots from your app for better presentation.

</div>

---

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
- GitHub: [@yourusername](https://github.com/yourusername)
- Email: your.email@example.com

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
- Open an [Issue](https://github.com/yourusername/BookMyHealth/issues)
- Contact: your.email@example.com

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

