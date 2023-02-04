import org.jetbrains.compose.internal.utils.getLocalProperty
import java.util.*

plugins {
    id("org.gradle.maven-publish")
    id("signing")
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    repositories {
        if (canPublishLocal(project)) {
            mavenLocal()
        }
    }
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())
        groupId = "dev.minimul"
        pom {
            name.set("MinimulDev KMM Toolkit")
            description.set("A set of opinionated tools for everyday KMM development.")
            url.set("https://github.com/MinimulDev/kmm-toolkit")
            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("brendangoldberg")
                    name.set("Brendan Goldberg")
                    email.set("goldberg.brendan@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/MinimulDev/kmm-toolkit")
            }
        }
    }
}

signing {
    val ciRunning = System.getenv("CI_RUNNING")?.toBoolean() ?: false
    if (!ciRunning) {
        val keyId = project.getLocalProperty("signing.keyId") ?: System.getenv("SIGNING_KEY_ID")
        val secretKey =
            project.getLocalProperty("signing.secretKey") ?: System.getenv("SIGNING_SECRET_KEY")
        val password =
            project.getLocalProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")

        val secretKeyDecoded = String(Base64.getDecoder().decode(secretKey))

        useInMemoryPgpKeys(keyId, secretKeyDecoded, password)
        sign(publishing.publications)
    }
}
