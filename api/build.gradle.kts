plugins {
    id("org.asciidoctor.jvm.convert") version "3.3.2"
    id("com.epages.restdocs-api-spec") version "0.19.2"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework:spring-tx:6.1.5")
    // swagger
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("com.epages:restdocs-api-spec-mockmvc:0.19.2")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")


}

tasks.test {
    useJUnitPlatform()
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}

openapi3 {
    this.setServer("https://localhost:8080") // list로 넣을 수 있어 각종 환경의 URL을 넣을 수 있음!
    title = "hhplus ticket reservation"
    description = "hhplus ticket reservation"
    version = "0.1.0"
    format = "yaml" // or json
}

tasks.register<Copy>("copyOasToSwagger") {
    delete("src/main/resources/static/swagger-ui/openapi3.yaml") // 기존 OAS 파일 삭제
    from("$buildDir/api-spec/openapi3.yaml") // 복제할 OAS 파일 지정
    into("src/main/resources/static/swagger-ui/.") // 타겟 디렉터리로 파일 복제
    dependsOn("openapi3") // openapi3 Task가 먼저 실행되도록 설정
}