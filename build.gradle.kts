

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.0.20-Beta2"
    id("org.jetbrains.kotlin.kapt") version "2.0.20-Beta2"
    id("io.github.goooler.shadow") version "8.1.8"
    id("maven-publish")
    id("java-library")
}

group "net.holograms"
version "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.opencollab.dev/maven-releases") }
    maven { url = uri("https://repo.opencollab.dev/maven-snapshots/") }
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    compileOnly("cn.nukkit:nukkit:1.0-SNAPSHOT")
    compileOnly("com.creeperface.nukkit.placeholderapi:PlaceholderAPI:1.4-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.holograms"
            artifactId = "holograms"
            version = "1.0"

            from(components["java"])
        }
    }
}