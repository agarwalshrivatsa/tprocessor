# Uncomment the following line if use multi-stage build
# FROM maven:3.8-openjdk-17-slim AS builder
# WORKDIR /usr/app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-jammy
WORKDIR /usr/app

# Delete the following line if use multi-stage build
COPY target/*.jar app.jar 

# Uncomment the following line if use multi-stage build
# COPY --from=builder /usr/app/target/*.jar app.jar

EXPOSE 8081
# Define the entry point for the application
ENTRYPOINT ["java", "-jar", "app.jar"]