# Multi-Stage Dockerfile for Java Application using Maven

# Define base image for build stage
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY settings.xml /root/.m2/settings.xml
COPY pom.xml .
COPY ./src ./src

# Build the application
RUN mvn clean package -s /root/.m2/settings.xml

# Define base image for runtime stage
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the jar file from build stage
COPY --from=build /app/target/*.jar app.jar
COPY ["newvrn.csv", "."]

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
