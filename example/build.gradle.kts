plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example"
        minSdk = 24
        targetSdk = 33
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.1"
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":toolkit-ui-compose"))
    implementation(project(":toolkit-routing"))
    implementation(project(":toolkit-routing-compose"))

    implementation(libs.bundles.uiComposeAndroidImplementations)

    implementation(libs.koinAndroid)
    implementation(libs.koinCompose)
    implementation(libs.androidxActivityCompose)
    implementation(libs.androidxActivity)
}