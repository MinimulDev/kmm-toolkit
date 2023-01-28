plugins {
    id("android-lib")
    kotlin("multiplatform")
    id("compose")
    id("publish")
}

kotlin {
    android()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":toolkit-arch"))
                api(libs.bundles.composeApis)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.uiComposeAndroidImplementations)
            }
        }
    }
}