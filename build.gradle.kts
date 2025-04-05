//import org.beryx.jlink.JPackageTask
//import org.gradle.internal.os.OperatingSystem
//import org.beryx.jlink.JlinkZipTask

plugins {
  java
  application
  alias(libs.plugins.modularity.module)
  alias(libs.plugins.openjfx.javafx)
  alias(libs.plugins.beryx.jlink)
}

group = "com.jawa"
version = "1.0"

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

//jlink {
//  imageZip.set(layout.buildDirectory.file("distributions/jawa-pos-client-${OperatingSystem.current().familyName}.zip"))
//  options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
//  launcher {
//    name = "app"
//  }
//
//  jpackage {
//    imageName = "JawaPos"
//    installerName = "JawaPOS-Installer"
//    installerType = OperatingSystem.current().executableSuffix.replace(".", "")
//    appVersion = project.version.toString()
//    icon = "src/main/resources/cat.ico"
//  }
//}
//
//tasks.named<JlinkZipTask>("jlinkZip") {
//  group = "distribution"
//}
//
//tasks.named<JPackageTask>("jpackage") {
//  doFirst {
//    val outputFile = file("build/jpackage/JawaPos/JawaPOS-Installer.exe")
//    if (outputFile.exists()) {
//      outputFile.delete()
//    }
//  }
//}
