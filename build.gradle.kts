import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.14"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.6.21"
}

group = "com.my"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

// 상위 프로젝트 및 하위 프로젝트에 대한 전체 프로젝트 설정
allprojects {
	group = "com.kotlin"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}
}

// 하위 프로젝트에 대한 프로젝트 설정
subprojects {

	// 하위프로젝트에는 상위 프로젝트에 플러그인 정보가 Import 되지않아 apply 메서드로 추가 필요
	// 플러그인은 모든 하위프로젝트에서 활성화
	apply(plugin = "kotlin")
	apply(plugin = "kotlin-spring")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "kotlin-kapt")

	dependencies {
		// Kotlin
		implementation("org.jetbrains.kotlin:kotlin-reflect")

		// SpringBoot
		implementation("org.springframework.boot:spring-boot-starter")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}

	// 하위 프로젝트에서 활성화한 dependency-management 플러그인을 사용하기 위한 설정
	dependencyManagement {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		}
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = "11"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

}