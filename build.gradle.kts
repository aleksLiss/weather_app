plugins {
    id("java")
    id ("jacoco")
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
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.8")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.7")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.5.6")
    implementation("org.liquibase:liquibase-core:5.0.0")
    implementation("org.postgresql:postgresql:42.7.7")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.8")
    implementation("com.google.code.gson:gson:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")
    testImplementation("com.h2database:h2:2.3.232")
}

tasks.test {
    useJUnitPlatform()
}