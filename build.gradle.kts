plugins {
    id("java")
    id("jacoco")
    id ("org.springframework.boot") version "3.4.1"
    id ("io.spring.dependency-management") version "1.1.7"
}

group = "com.weather.app"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.12"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("org.liquibase:liquibase-core:4.29.0")
    implementation("org.postgresql:postgresql:42.7.7")
    testImplementation("com.h2database:h2")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("com.google.code.gson:gson:2.11.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}


tasks.test {
    useJUnitPlatform()
}