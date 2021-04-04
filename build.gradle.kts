plugins {
    kotlin("jvm") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
    id("maven-publish")
}

val khttpVersion: String by project
val autoDslVersion: String by project
val jUnitVersion: String by project
val wiremockVersion: String by project
val striktVersion: String by project

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
    implementation("khttp:khttp:$khttpVersion") {
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
    }
    testImplementation("junit:junit:$jUnitVersion")
    testImplementation("io.strikt:strikt-core:$striktVersion")
    testImplementation("com.marcinziolo:kotlin-wiremock:$wiremockVersion")
}

publishing {
    publications {
        create<MavenPublication>("ddg-api") {
            from(components["kotlin"])
        }
    }
}