plugins {
    kotlin("jvm") version "2.2.20"
    id("org.jetbrains.compose") version "1.9.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"
}

group = "lorry"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "lorry.MainKt"   // nom de la classe/fichier qui contient ton fun main()
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}