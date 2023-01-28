@file:Suppress("PropertyName")

import com.android.build.gradle.BaseExtension

val _compileSdkVersion = 33
val _targetSdkVersion = _compileSdkVersion
val _minSdkVersion = 24
val _javaVersion = JavaVersion.VERSION_11

configure<BaseExtension> {
    compileSdkVersion(_compileSdkVersion)
    defaultConfig {
        minSdk = _minSdkVersion
        targetSdk = _targetSdkVersion
    }
    compileOptions {
        targetCompatibility = _javaVersion
        sourceCompatibility = _javaVersion
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = _javaVersion.toString()
    }
}