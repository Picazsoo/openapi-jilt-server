plugins {
    java
    id("org.springframework.boot") version "2.7.11"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.openapi.generator") version "6.5.0"
}

group = "cz.metlicka"
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

extra["springCloudVersion"] = "2021.0.6"

dependencies {
    annotationProcessor("cc.jilt:jilt:1.2")
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    compileOnly("cc.jilt:jilt:1.2")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("io.github.openfeign:feign-jackson:12.3")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.8")
    implementation("io.swagger.core.v3:swagger-models:2.2.8")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.mapstruct:mapstruct:1.5.4.Final")
    implementation("org.openapitools:jackson-databind-nullable:0.2.1")
    implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val spec = "$rootDir/src/main/resources/openapi.yaml"
val generatedSourcesDir = "$buildDir/generated/sources/openapi"

openApiGenerate {
    generatorName.set("spring")
    generateApiTests.set(false)
    generateModelTests.set(false)
    inputSpec.set(spec)
    outputDir.set(generatedSourcesDir)
    apiPackage.set("cz.metlicka.server.api")
    modelPackage.set("cz.metlicka.server.model")
    configOptions.set(mapOf(
            "dateLibrary" to "java8",
            "library" to "spring-boot",
            "additionalModelTypeAnnotations" to """
                @lombok.AllArgsConstructor
                @org.jilt.Builder(style = org.jilt.BuilderStyle.TYPE_SAFE_UNGROUPED_OPTIONALS)""".trimIndent(),

            ))
    ignoreFileOverride.set("$rootDir/.openapi-generator-ignore")
}

sourceSets {
    getByName("main") {
        java {
            srcDir("$generatedSourcesDir/src/main/java")
        }
    }
}

tasks {
    val openApiGenerate by getting

    val compileJava by getting {
        dependsOn(openApiGenerate)
    }
}
