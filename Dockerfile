# Multi-Stage Dockerfile for Java Application using Maven

# Define base image for build stage
FROM maven:3.9-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY CollegeCarPark/src ./src

# Build the application
RUN mvn clean package

# Define base image for runtime stage
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the jar file from build stage
COPY --from=build /app/target/*.jar app.jar

# Set the command to run the application
CMD ["java", "-jar", "app.jar"]
