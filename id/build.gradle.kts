import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
  id("com.android.library")
  id("org.jetbrains.dokka")
  id("com.vanniktech.maven.publish.base")
}

kotlin {
  android {
    publishLibraryVariants("release")
  }
  jvm()
  js(BOTH) {
    browser()
    nodejs()
  }

  macosX64()
  macosArm64()
  iosX64()
  iosArm64()
  iosArm32()
  iosSimulatorArm64()
  watchosArm32()
  watchosArm64()
  watchosSimulatorArm64()
  watchosX86()
  watchosX64()
  tvosArm64()
  tvosSimulatorArm64()
  tvosX64()

  linuxX64()
  mingwX64()

  sourceSets {
    all {
      languageSettings.optIn("kotlin.RequiresOptIn")
    }
    matching { it.name.endsWith("Test") }.all {
      languageSettings {
        optIn("kotlin.RequiresOptIn")
      }
    }

    val commonMain by sourceSets.getting {
      dependencies {
        api(libs.diglol.crypto.random)
        compileOnly(libs.kotlinx.serialization.core)
      }
    }
    val commonTest by sourceSets.getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }

    val commonJvmMain by sourceSets.creating {
      dependsOn(commonMain)
    }
    val commonJvmTest by sourceSets.creating {
      dependsOn(commonJvmMain)
      dependsOn(commonTest)
    }

    val jvmMain by sourceSets.getting {
      dependsOn(commonJvmMain)
    }
    val jvmTest by sourceSets.getting {
      dependsOn(jvmMain)
      dependsOn(commonJvmTest)
    }

    val androidMain by sourceSets.getting {
      dependsOn(commonJvmMain)
    }
    val androidTest by sourceSets.getting {
      dependsOn(androidMain)
      dependsOn(commonJvmTest)
    }

    val jsMain by sourceSets.getting {
      dependencies {
        api(libs.kotlinx.serialization.core)
      }
    }
    val jsTest by sourceSets.getting

    val nativeMain by sourceSets.creating {
      dependencies {
        api(libs.kotlinx.serialization.core)
      }
    }
    nativeMain.dependsOn(commonMain)

    val linuxMain by sourceSets.creating {
      dependsOn(nativeMain)
    }

    val mingwMain by sourceSets.creating {
      dependsOn(nativeMain)
    }

    targets.withType<KotlinNativeTarget>().all {
      val main by compilations.getting
      val test by compilations.getting

      main.defaultSourceSet.dependsOn(nativeMain)
      test.defaultSourceSet.dependsOn(commonTest)
    }
  }
}

android {
  namespace = "diglol.id"

  compileSdk = libs.versions.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.minSdk.get().toInt()

    consumerProguardFiles("proguard-rules.pro")
  }

  testOptions {
    unitTests.isReturnDefaultValues = true
  }
}

dependencies {
  androidTestImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.runner)
}

configure<MavenPublishBaseExtension> {
  configure(
    KotlinMultiplatform(
      javadocJar = JavadocJar.Dokka("dokkaGfm")
    )
  )
}
