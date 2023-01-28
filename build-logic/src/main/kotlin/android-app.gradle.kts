import com.android.build.gradle.BaseExtension

plugins {
    id("com.android.application")
    id("android-base")
}

configure<BaseExtension> {
    defaultConfig {
        multiDexEnabled = true
    }
}
