import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("kapt") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	id("org.jetbrains.dokka") version ("1.6.10")
	jacoco
}

group = "com.lundih"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

val mapstructVersion by extra { "1.4.2.Final" }
val springdocOpenApiVersion by extra { "1.6.6" }

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	implementation("org.springdoc:springdoc-openapi-kotlin:$springdocOpenApiVersion")
	implementation("org.springdoc:springdoc-openapi-ui:$springdocOpenApiVersion")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.mapstruct:mapstruct:$mapstructVersion")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")

	runtimeOnly("com.h2database:h2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation(kotlin("test-junit"))
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
}

kapt {
	arguments {
		// Mapstruct configuration option
		arg("mapstruct.defaultComponentModel", "spring")
	}
}

jacoco {
	toolVersion = "0.8.7"
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "11"
		}
	}

	dokkaHtml.configure {
		outputDirectory.set(buildDir.resolve("dokka"))
		// Displayed in final output
		moduleName.set("Auth Server Dokka")
		// Suppress all inherited members that were not overriden in a given class
		suppressInheritedMembers.set(true)
		// Prevent resolving package-lists online. Only local files are resolved
		offlineMode.set(true)
		dokkaSourceSets {
			configureEach {
				// Include or exclude non-public members
				includeNonPublic.set(true)
				// Output deprecated members
				skipDeprecated.set(false)
				// Emit warnings about non-documented members
				reportUndocumented.set(false)
				// Do not create index pages for empty packages
				skipEmptyPackages.set(true)
				jdkVersion.set(8)
				// Disable linking to online kotlin-stdlib documentation
				noStdlibLink.set(false)
				// Disable linking to online JDK documentation
				noJdkLink.set(false)
			}
		}
	}

	jacocoTestReport {
		reports {
			xml.required.set(false)
			csv.required.set(false)
			html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
		}
	}

	jacocoTestCoverageVerification {
		dependsOn("jacocoTestReport")
		mustRunAfter("jacocoTestReport")
		violationRules {
			rule {
				limit {
					minimum = "0.7".toBigDecimal()
				}
			}
		}
	}

	getByName("check").dependsOn += "jacocoTestCoverageVerification"

	withType<Test> {
		useJUnitPlatform()
		reports {
			junitXml.required.set(true)
			junitXml.outputLocation.set(File("$buildDir/reports/junit/xml"))
			html.required.set(true)
			html.outputLocation.set(File("$buildDir/reports/junit/html"))
		}
	}

	bootJar {
		archiveFileName.set("auth-server.jar")
	}
}
