plugins {
    id("mpp")
    id("android-publication")
}

dependencies {
    commonMainImplementation(libs.bundles.archCommonImplementations)
    androidMainImplementation(libs.bundles.archAndroidImplementations)
}
