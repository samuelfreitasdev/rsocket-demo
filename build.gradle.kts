plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "br.com.sf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("MainKt")
}

dependencies {

//    implementation("io.rsocket:rsocket-core:1.1.4")
//    implementation("io.rsocket:rsocket-transport-netty:1.1.4")
    implementation("io.rsocket:rsocket-core:0.11.13")
    implementation("io.rsocket:rsocket-transport-netty:0.11.13")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.10.0")

    testImplementation(kotlin("test"))
}
