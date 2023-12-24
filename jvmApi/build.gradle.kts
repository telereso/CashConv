plugins {
	java
	war
	id("org.springframework.boot") version "2.7.10-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm")
}

group = "io.telereso"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-test")


	implementation(kmpLibs.telereso.core.jvm)
	implementation(project(":cashconv-models"))
	implementation(project(":cashconv-client"))

}

tasks.withType<Test> {
	useJUnitPlatform()
}
