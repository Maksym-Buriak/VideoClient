plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.gms)
}

android {
    namespace = "com.maks_buriak.videoclient"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.maks_buriak.videoclient"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

//dependencies {
//    implementation(project(":domain"))
//    implementation(project(":data"))
//
//    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
//    implementation("com.google.firebase:firebase-auth-ktx")
//    implementation("com.google.firebase:firebase-common")
//    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.firebase:firebase-database")
//
//    implementation(platform("androidx.compose:compose-bom:2025.05.01"))
//
//    // ViewModel для Compose
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
//
//    val koin_version = "4.0.3"
//    implementation("io.insert-koin:koin-core:$koin_version")
//    implementation("io.insert-koin:koin-android:$koin_version")
//    testImplementation("io.insert-koin:koin-test:$koin_version")
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.lifecycle.runtime.ktx)
//    implementation(libs.androidx.activity.compose)
//    implementation(libs.androidx.ui)
//    implementation(libs.androidx.ui.graphics)
//    implementation(libs.androidx.ui.tooling.preview)
//    implementation(libs.androidx.material3)
//    implementation(libs.androidx.foundation)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.ui.test.junit4)
//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)
//}


dependencies {
    // Локальні модулі
    implementation(project(":domain"))
    implementation(project(":data"))

    // Firebase BoM (для автоматичного керування версіями)
//    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
//    implementation("com.google.firebase:firebase-auth-ktx:23.2.1")
//    implementation("com.google.android.gms:play-services-auth:21.4.0") // Google Sign-In
//    implementation("com.google.firebase:firebase-common")
//    implementation("com.google.firebase:firebase-analytics")
//    implementation("com.google.firebase:firebase-database")
//    implementation("com.google.firebase:firebase-firestore")
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.play.services.auth)

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.3")

    // Koin core + Android
    implementation("io.insert-koin:koin-android:3.4.3")
    // Koin для Compose
    implementation("io.insert-koin:koin-androidx-compose:3.4.3")

    // Compose BoM
    implementation(platform("androidx.compose:compose-bom:2025.05.01"))

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)

    // ViewModel для Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation(libs.androidx.camera.core)

    // CameraX
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.guava)

    // Permissions
    implementation(libs.accompanist.permissions)

//    // Koin
//    val koin_version = "4.0.3"
//    implementation("io.insert-koin:koin-core:$koin_version")
//    implementation("io.insert-koin:koin-android:$koin_version")
//    testImplementation("io.insert-koin:koin-test:$koin_version")

    // Тестування
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Інструменти для debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}