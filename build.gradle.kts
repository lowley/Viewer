import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
    id("org.jetbrains.compose") version "1.9.2"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21"
}

group = "lorry"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven (url ="https://jitpack.io")
}

compose.desktop {
    application {
        mainClass = "lorry.ui.MainKt" // ← ta classe main
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe, // installeur .exe
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.AppImage // dossier portable avec .exe
            )
            packageName = "Viewer"
            packageVersion = "1.0.0"
            includeAllModules = true // jlink auto; embarque le runtime
            windows {
                menu = true
                shortcut = true
                // iconFile.set(project.file("icons/app.ico"))
                console = true
            }
        }
    }
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
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.21")

    implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.9.5")

    implementation("com.google.code.gson:gson:2.10.1")

    ///////////////
    // WriterAPI //
    ///////////////
    //github -> https://jitpack.io/#lowley/WriterAPI
//    implementation("com.github.lowley:WriterAPI:v1.0.18")
    //local
    implementation("io.github.lowley:periscope:1.0.4")

    /////////////////////////////////////////
    // programmation fonctionnelle: Either //
    /////////////////////////////////////////
    implementation("io.arrow-kt:arrow-core:1.2.4")

    ////////////////////
    // pour Flow<xxx> //
    ////////////////////
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.8.1")

    /////////////////////
    // pour Dispachers //
    /////////////////////
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

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
    jvmToolchain(17)
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
    freeCompilerArgs.set(
        listOf(
            "-Xcontext-parameters",
            "-Xnested-type-aliases",
        )
    )
}