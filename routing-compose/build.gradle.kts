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
                implementation(project(":toolkit-routing"))
                api(libs.bundles.composeApis)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.bundles.routingComposeAndroidImplementations)
            }
        }
    }
}