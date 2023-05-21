import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

buildscript {
  repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
  }
  dependencies {
    classpath(libs.android.gradle.plugin)
    classpath(libs.kotlin.gradle.plugin)
    classpath(libs.kotlin.allopen)
    classpath(libs.kotlin.serialization)
    classpath(libs.dokka.gradle.plugin)
    classpath(libs.mavenPublish.gradle.plugin)
    classpath(libs.benchmark.gradle.plugin)
  }
}

allprojects {
  group = "com.diglol.id"
  version = "0.4.0-SNAPSHOT"

  repositories {
    mavenCentral()
    google()
  }
}

subprojects {
  fun CommonExtension<*, *, *, *>.applyAndroid() {
    lint {
      textReport = true
      textOutput = file("stdout")
      lintConfig = rootProject.file("lint.xml")

      checkDependencies = true
      checkTestSources = false
      explainIssues = false

      checkReleaseBuilds = true
    }

    compileSdk = 33
    defaultConfig {
      minSdk = 21
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
    }
  }

  plugins.withType<LibraryPlugin>().configureEach {
    extensions.configure<LibraryExtension> { applyAndroid() }
  }

  plugins.withType<AppPlugin>().configureEach {
    extensions.configure<BaseAppModuleExtension> {
      applyAndroid()
      defaultConfig.targetSdk = 33
    }
  }

  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
  }

  tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_1_8)
      freeCompilerArgs.addAll("-Xjvm-default=all")
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

