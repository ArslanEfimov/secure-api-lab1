plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"

	// SAST spotbugs
	id("com.github.spotbugs") version "6.2.7"

	//SCA snyk
	id("io.snyk.gradle.plugin.snykplugin") version "0.7.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Developing Secure REST API with CI/CD integration"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.5")
	// https://mvnrepository.com/artifact/org.postgresql/postgresql
	implementation("org.postgresql:postgresql:42.7.7")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security
	implementation("org.springframework.boot:spring-boot-starter-security:3.5.5")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
	implementation("org.springframework.boot:spring-boot-starter-validation:3.5.5")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")

	// jwt
	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	// https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

	spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.14.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	implementation("org.apache.commons:commons-lang3:3.18.0")
	spotbugs("com.github.spotbugs:spotbugs:4.9.4")
	spotbugs("org.apache.commons:commons-lang3:3.18.0")
}

spotbugs {
	toolVersion.set("4.8.6") // версия SpotBugs (проверить актуальную)
	ignoreFailures.set(false) // если true — билд не упадёт при найденных багам
	showProgress.set(true)
	effort.set(com.github.spotbugs.snom.Effort.DEFAULT) // уровень анализа: MIN, DEFAULT, MAX
	reportLevel.set(com.github.spotbugs.snom.Confidence.HIGH) // LOW, MEDIUM, HIGH
}

snyk {
	setArguments("--all-sub-projects")
	setSeverity("low")
	setApi("https://api.snyk.io")
	setAutoDownload(true)
	setAutoUpdate(true)
}


tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
	reports.create("html") {
		required.set(true)
		outputLocation.set(layout.buildDirectory.file("reports/spotbugs/${name}.html"))
		setStylesheet("fancy-hist.xsl")
	}
}




tasks.withType<Test> {
	useJUnitPlatform()
}
