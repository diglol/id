plugins {
  kotlin("multiplatform")
  application
}

application {
  mainClass.set("diglol.id.samples.Samples")
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
