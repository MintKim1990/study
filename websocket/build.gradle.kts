dependencies {
    // Websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Springboot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("io.github.microutils:kotlin-logging:3.0.5")

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // 코틀린 문법상 대부분 기본생성자가 없어서 RequestBody 파싱에러 에러나는 부분을 해결해주는 모듈
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}