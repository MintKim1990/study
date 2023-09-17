// 코틀린에서 엔티티 기본생성자 자동생성을 지원하는 플러그인
apply(plugin = "kotlin-jpa")

dependencies {
    // SpringBoot
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // 코틀린 문법상 대부분 기본생성자가 없어서 RequestBody 파싱에러 에러나는 부분을 해결해주는 모듈
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") 

    // H2DB
    runtimeOnly("com.h2database:h2")
}