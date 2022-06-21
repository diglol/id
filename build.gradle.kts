import com.android.build.gradle.BaseExtension
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

buildscript {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
  dependencies {
    classpath(libs.android.gradle.plugin)
    classpath(libs.kotlin.gradle.plugin)
    classpath(libs.kotlin.serialization)
    classpath(libs.mavenPublish.gradle.plugin)
  }
}

allprojects {
  group = "com.diglol.id"
  version = "0.1.0-SNAPSHOT"

  repositories {
    mavenCentral()
    google()
  }
}

subprojects {
  plugins.withId("com.android.library") {
    extensions.configure<BaseExtension> {
      lintOptions {
        textReport = true
        textOutput("stdout")
        lintConfig = rootProject.file("lint.xml")

        isCheckDependencies = true
        isCheckTestSources = false
        isExplainIssues = false

        isCheckReleaseBuilds = false
      }
    }
  }

  tasks.withType(Test::class).configureEach {
    testLogging {
      if (System.getenv("CI") == "true") {
        events = setOf(TestLogEvent.FAILED, TestLogEvent.SKIPPED, TestLogEvent.PASSED)
      }
      exceptionFormat = TestExceptionFormat.FULL
    }
  }
}

allprojects {
  plugins.withId("com.vanniktech.maven.publish.base") {
    configure<PublishingExtension> {
      repositories {
        maven {
          name = "testMaven"
          url = file("${rootProject.buildDir}/testMaven").toURI()
        }
      }
    }
    configure<MavenPublishBaseExtension> {
      publishToMavenCentral(SonatypeHost.DEFAULT)
      signAllPublications()
      pom {
        description.set("Diglol Id Generator")
        name.set(project.name)
        url.set("https://github.com/diglol/id/")
        licenses {
          license {
            name.set("The Apache Software License, Version 2.0")
            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            distribution.set("repo")
          }
        }
        developers {
          developer {
            id.set("diglol")
            name.set("Diglol")
            url.set("https://github.com/diglol/")
          }
        }
        scm {
          url.set("https://github.com/diglol/id/")
          connection.set("scm:git:https://github.com/diglol/id.git")
          developerConnection.set("scm:git:ssh://git@github.com/diglol/id.git")
        }
      }
    }
  }
}

