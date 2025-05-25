# 빌드 단계
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon -x test

# 실행 단계
FROM openjdk:17-jdk-slim
COPY --from=build /app/build/libs/dojooo-0.0.1-SNAPSHOT.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]
