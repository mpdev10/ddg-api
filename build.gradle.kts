plugins {
    kotlin("jvm") version "1.4.32"
}

val fuelVersion: String by project

group = "pl.mpakula"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.github.kittinunf.fuel:$fuelVersion")
}
