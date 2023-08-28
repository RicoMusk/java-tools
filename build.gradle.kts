plugins {
    java
    id("org.springframework.boot") version "3.0.0"
    id("io.spring.dependency-management") version "1.1.0"
}

group = "com.ricoandilet"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // aws sdk
    //implementation("software.amazon.awssdk:aws-sdk-java:2.20.134")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

    // Json
    implementation("com.alibaba:fastjson:2.0.32")
    implementation("cn.hutool:hutool-core:5.8.18")
    implementation("cn.hutool:hutool-extra:5.8.18")

    // Jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Domain verification
    implementation("dnsjava:dnsjava:3.5.2")
    implementation("com.amazonaws:aws-java-sdk-acm:1.12.535")

    // Elastic load balancing
    implementation("com.amazonaws:aws-java-sdk-elasticloadbalancingv2:1.12.537")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.processResources {
    filesMatching("application.*") {
        expand(project.properties)
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}
