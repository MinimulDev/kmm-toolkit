plugins {
    id("com.android.library")
    id("android-base")
}

version = loadVersion(project)

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}