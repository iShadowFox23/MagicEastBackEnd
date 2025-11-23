plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	kotlin("plugin.jpa") version "2.2.21"
	id("org.springframework.boot") version "4.0.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.magiceast"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

repositories {
	mavenCentral()
}

dependencies {
	// Backend principal
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")

	// Kotlin + JSON
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	// Desarrollo
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// --- Oracle JDBC + Wallet ---
	// BOM para que todas las libs de Oracle usen la misma versión
	implementation(platform("com.oracle.database.jdbc:ojdbc-bom:23.9.0.25.07"))

	// Driver JDBC
	runtimeOnly("com.oracle.database.jdbc:ojdbc11")

	// Librerías de seguridad necesarias para wallet/mTLS
	runtimeOnly("com.oracle.database.security:oraclepki")


	// Tests
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-security-test")
}


kotlin {
	jvmToolchain(17)
	compilerOptions {
		freeCompilerArgs.addAll(
			"-Xjsr305=strict",
			"-Xannotation-default-target=param-property"
		)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
