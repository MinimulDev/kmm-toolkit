import org.gradle.api.Project
import java.util.*

fun getAndLoadLocalProperties(project: Project): Properties {
    val file = project.rootProject.file("local.properties")
    val props = Properties()
    props.load(file.inputStream())
    return props
}

fun loadVersion(project: Project): String {
    val env = System.getenv("PROJECT_VERSION")

    if (env != null) {
        return env
    }

    val props = getAndLoadLocalProperties(project)

    return props.getProperty("PROJECT_VERSION")
}

fun canPublishLocal(project: Project): Boolean {
    val props = getAndLoadLocalProperties(project)
    return props.getProperty("canPublishLocal", "false").toBoolean()
}