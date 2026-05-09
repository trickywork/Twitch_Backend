FROM eclipse-temurin:21-jdk AS build

WORKDIR /workspace
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=build /workspace/build/libs/twitch-0.0.1-SNAPSHOT.jar app.jar

ENV PORT=8080
ENV TWITCH_DEMO_MODE=true
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
