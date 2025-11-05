plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "com.mllfjn"
version = "1.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.mllfjn.simyys")
    mainClass.set("com.mllfjn.simyys.starter.Launcher")
}

javafx {
    version = "17.0.14"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
/*
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.testfx:testfx-core:4.0.18")
    implementation("org.testfx:testfx-junit5:4.0.18")
//    testCompile 'org.junit.jupiter:junit-jupiter-api:5.5.1'
    implementation(group = "org.assertj", name = "assertj-core", version = "3.13.2")
    implementation(group = "org.hamcrest", name = "hamcrest", version = "2.1")*/
    compileOnly("org.jetbrains:annotations:26.0.2")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/simYYS-${version}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "simYYS"
    }
}
