# =========================
# 1) Build stage
# =========================
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Gradle wrapper / 설정 먼저 (캐시 이득)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle* settings.gradle* ./

# 소스 복사
COPY src ./src

# 빌드
RUN chmod +x ./gradlew \
 && ./gradlew clean bootJar -x test

# =========================
# 2) Run stage
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]