# -------- Stage 1: Build --------
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the project and package it. Remove "-DskipTests" if you want tests to run.
RUN mvn clean package -DskipTests

# -------- Stage 2: Run --------
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the built jar from the build stage. Adjust jar name as per your artifact.
COPY --from=build /app/target/Calculator-1.0-SNAPSHOT.jar app.jar

# Expose a port if necessary (example: 8080). Remove if not needed.
EXPOSE 8080

# Run the application.
CMD ["java", "-jar", "app.jar"]
