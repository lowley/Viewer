import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation("androidx.lifecycle:lifecycle-viewmodel-desktop:2.9.4")
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)

    //////////////////////////////
    // injections de dépendance //
    //////////////////////////////
    implementation("io.insert-koin:koin-core:4.0.0")      // version à adapter si besoin
    implementation("io.insert-koin:koin-compose:4.0.0")   // intégration Compose

    /////////////////////////
    // material expressive //
    /////////////////////////
    implementation("org.jetbrains.compose.material3:material3:1.9.0-alpha04")

    ///////////////
    // reflexion //
    ///////////////
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.24")

    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1") // par ex.

    implementation("com.google.code.gson:gson:2.10.1")
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
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xcontext-parameters", "-Xnested-type-aliases"))
}