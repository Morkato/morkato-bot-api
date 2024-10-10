FROM openjdk:17-jdk-slim AS build
WORKDIR /usr/app
COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN ./gradlew bootJar
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/usr/app/build/libs/api-0.0.1.jar"]
