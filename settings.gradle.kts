enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")

rootProject.name = "kmm-toolkit"
include("ui-compose", "arch", "routing", "routing-compose")
include(":example")

project(":arch").name = "toolkit-arch"
project(":ui-compose").name = "toolkit-ui-compose"
project(":routing").name = "toolkit-routing"
project(":routing-compose").name = "toolkit-routing-compose"
