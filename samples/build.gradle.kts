plugins {
  kotlin("multiplatform")
  application
}

application {
  mainClass.set("prop.Samples")
}

kotlin {
  jvm {
    withJava()
  }

  sourceSets {
    commonMain {
      dependencies {
        implementation(projects.id)
      }
    }
  }
}
