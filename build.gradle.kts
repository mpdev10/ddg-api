plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
}

val khttpVersion: String by project

group = "pl.mpakula"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    google()
    maven("https://jitpack.io/")

}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
    implementation("khttp:khttp:$khttpVersion")
}
