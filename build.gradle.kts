plugins {
    java
    id("org.springframework.boot") version "2.7.3"
}
apply(plugin = "io.spring.dependency-management")

group "com.dptablo"
version "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    /* spring */
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    /* database */
    implementation("org.mariadb.jdbc:mariadb-java-client:3.0.7")

    /* dev */
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    compileOnly("org.projectlombok:lombok")

    /* testing */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.assertj:assertj-core:3.23.1")

    /* testing - database */
    testImplementation("com.h2database:h2:2.1.214")

    /* testing - mocking api server */
    testImplementation("com.squareup.okhttp3:okhttp:4.10.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
}

java {
    toolchain {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}