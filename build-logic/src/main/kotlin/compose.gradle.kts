import com.android.build.gradle.BaseExtension

plugins {
    id("org.jetbrains.compose")
}

configure<BaseExtension> {
    buildFeatures.apply {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.1"
    }
}