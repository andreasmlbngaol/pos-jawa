import org.gradle.internal.os.OperatingSystem
import org.beryx.jlink.JlinkZipTask

plugins {
  java
  application
  alias(libs.plugins.modularity.module)
  alias(libs.plugins.openjfx.javafx)
  alias(libs.plugins.beryx.jlink)
}

group = "com.jawa"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_20
  targetCompatibility = JavaVersion.VERSION_20
}

tasks.withType<JavaCompile>().configureEach {
  options.encoding = "UTF-8"
}

application {
  mainModule.set("com.jawa.utsposclient")
  mainClass.set("com.jawa.utsposclient.HelloApplication")
}

javafx {
  version = libs.versions.javafx.get()
  modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
  implementation(libs.bootstrapfx.core)
  implementation(libs.javafx.controls)
  implementation(libs.javafx.fxml)

  testImplementation(libs.junit.jupiter.api)
  testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.test {
  useJUnitPlatform()
}

jlink {
  val platform = OperatingSystem.current().let {
    when {
      it.isWindows -> "win"
      it.isMacOsX -> "mac"
      it.isLinux -> "linux"
      else -> throw GradleException("Unsupported OS: ${it.name}")
    }
  }

  imageZip.set(layout.buildDirectory.file("distributions/app-$platform.zip"))
  options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
  launcher {
    name = "app"
  }
}

tasks.named<JlinkZipTask>("jlinkZip") {
  group = "distribution"
}
