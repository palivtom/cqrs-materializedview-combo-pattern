import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"

	id("org.openapi.generator") version "6.2.1"

	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
}

group = "cz.ctu.fee.palivtom"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

sourceSets {
	main {
		java {
			srcDirs("$buildDir/generated/src/main/kotlin")
		}
	}
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.flywaydb:flyway-core:9.8.2")
	implementation("org.springframework.kafka:spring-kafka")
	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
	dependsOn(tasks.withType(GenerateTask::class.java))
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<GenerateTask> {
	val packageBase = "${project.group}.orderservice"

	inputSpec.set("$projectDir/src/main/resources/openapi/order-service.yml")
	outputDir.set("$buildDir/generated")
	generatorName.set("kotlin-spring")
	apiPackage.set("$packageBase.api")
	modelPackage.set("$packageBase.model")
	modelNameSuffix.set("Dto")
    configOptions.set(
		mapOf(
			"title" to "order-service",
			"serializationLibrary" to "jackson",
			"interfaceOnly" to "true",
			"documentationProvider" to "source",
			"exceptionHandler" to "false",
			"useBeanValidation" to "false"
		)
	)
}