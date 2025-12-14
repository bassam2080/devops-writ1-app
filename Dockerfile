 # Multi-Stage Dockerfile for Java Application using Maven

# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# If you rely on a Maven mirror/proxy, keep this.
# (Make sure settings.xml exists in the repo root)
COPY settings.xml /root/.m2/settings.xml

# Copy only pom first (better cache usage)
COPY pom.xml .

# Copy sources
COPY ./src ./src

# Allow CI to decide whether to skip tests
ARG SKIP_TESTS=true

# Build the application (run tests when SKIP_TESTS=false)
RUN mvn -B -ntp -s /root/.m2/settings.xml clean package -DskipTests=${SKIP_TESTS}
RUN test -d /app/target/surefire-reports && ls -la /app/target/surefire-reports || (echo "NO surefire reports found" && exit 1)

# ---- Runtime stage ----
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Your CSV
COPY ["newvrn.csv", "."]

ENTRYPOINT ["java", "-jar", "app.jar"]

