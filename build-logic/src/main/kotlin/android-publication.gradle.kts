import com.android.build.gradle.BaseExtension
import org.gradle.jvm.tasks.Jar

plugins {
    id("android-lib")
    id("publish")
}

afterEvaluate {
    val androidSourcesJar = tasks.create("androidSourcesJar", Jar::class) {
        archiveClassifier.set("sources")
        val androidExtension: BaseExtension =
            this.project.extensions.getByType(BaseExtension::class)
        from(androidExtension.sourceSets.getByName("main").java.srcDirs)
    }

    publishing.publications {
        create("release", MavenPublication::class.java) {
            from(components.getByName("release"))
            artifact(androidSourcesJar)
        }
    }
}