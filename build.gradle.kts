plugins {
    java
    application
//    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "3.2.1"
    id("org.gradlex.extra-java-module-info") version "1.14.2"
}

group = "com.mllfjn"
version = "1.0.4"

repositories {
    mavenCentral()
}

val junitVersion = "5.12.1"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
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
    version = "25.0.4"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")

    compileOnly("org.jetbrains:annotations:26.0.2")

    // Source: https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
    implementation("com.fasterxml.jackson.core:jackson-databind:2.22.3")

    // Source: https://mvnrepository.com/artifact/com.google.zxing/core
    implementation("com.google.zxing:core:3.5.4")

    // Source: https://mvnrepository.com/artifact/org.msgpack/jackson-dataformat-msgpack
    implementation("org.msgpack:jackson-dataformat-msgpack:0.9.12")
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

extraJavaModuleInfo {
    // 为自动模块指定一个稳定的名称，或者自动生成 module-info
    automaticModule("org.msgpack:jackson-dataformat-msgpack", "org.msgpack.jackson")
    deriveAutomaticModuleNamesFromFileNames.set(true)
}