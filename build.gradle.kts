import com.android.build.gradle.BaseExtension
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithSimulatorTests

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
    classpath(libs.dokka.gradle.plugin)
    classpath(libs.mavenPublish.gradle.plugin)
    classpath(libs.benchmark.gradle.plugin)
  }
}

allprojects {
  group = "com.diglol.id"
  version = "0.3.0-SNAPSHOT"

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

  // TODO Remove when fixed
  plugins.withId("org.jetbrains.kotlin.multiplatform") {
    extensions.configure<KotlinMultiplatformExtension> {
      targets.withType<KotlinNativeTargetWithSimulatorTests> {
        testRuns["test"].apply {
          when {
            targetName.startsWith("watch") -> {
              deviceId = "Apple Watch Series 5 (44mm)"
            }
          }
        }
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

  tasks.withType<AbstractArchiveTask>().configureEach {
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
  }

  normalization {
    runtimeClasspath {
      metaInf {
        ignoreAttribute("Bnd-LastModified")
      }
    }
  }
}

allprojects {
  tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
      reportUndocumented.set(false)
      skipDeprecated.set(true)
      jdkVersion.set(8)
      noAndroidSdkLink.set(true)

      perPackageOption {
        matchingRegex.set("diglol\\.id\\.internal\\.*")
        suppress.set(true)
      }
    }
  }

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
      publishToMavenCentral(SonatypeHost.S01)
      signAllPublications()
      pom {
        description.set("A global Id generator for Kotlin Multiplatform.")
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

