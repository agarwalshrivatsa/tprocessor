FROM maven:3.8-openjdk-17 AS builder
WORKDIR /usr/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim
WORKDIR /usr/app
COPY --from=builder /usr/app/target/*.jar app.jar
# Define the entry point for the application
ENTRYPOINT ["java", "-jar", "app.jar"]

