import org.jetbrains.compose.internal.utils.getLocalProperty

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath(libs.bundles.root)
        classpath(":build-logic")
    }
}

plugins {
    alias(libs.plugins.nexusPublish)
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            val u = project.getLocalProperty("ossrhUsername") ?: System.getenv("OSSRH_USERNAME")
            val p = project.getLocalProperty("ossrhPassword") ?: System.getenv("OSSRH_PASSWORD")
            username.set(u)
            password.set(p)
        }
    }
}

allprojects {
    group = "dev.minimul"
    version = "0.1.0"
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}