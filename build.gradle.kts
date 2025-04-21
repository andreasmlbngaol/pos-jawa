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
  google()
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
  mainClass.set("com.jawa.utsposclient.MainApp")
}

javafx {
  version = libs.versions.javafx.get()
  modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
  implementation(libs.bootstrapfx.core)
  implementation(libs.javafx.controls)
  implementation(libs.javafx.fxml)
  implementation(libs.okhttp)
  implementation(libs.okhttp.urlconnection)
  implementation(libs.gson)

  implementation(libs.postgresql)
  implementation(libs.jbcrypt)
  implementation(libs.hibernate.core)
  implementation(libs.ikonli)
  implementation(libs.material.icon)
}