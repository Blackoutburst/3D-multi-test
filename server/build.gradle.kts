import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("application")
    kotlin("plugin.serialization") version "1.9.23"
}

group = "dev.blackoutburst.server"
version = "1.0"


application {
    mainClass.set("dev.blackoutburst.server.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")
    implementation("io.ktor:ktor-server-core-jvm:2.3.10")
    implementation("io.ktor:ktor-server-websockets:2.3.10")
    implementation("io.ktor:ktor-server-netty-jvm:2.3.10")
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.slf4j:slf4j-nop:2.0.13")
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("server-shadow")
    mergeServiceFiles()
}
