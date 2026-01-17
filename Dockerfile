# Using Multi-Stage Builds so we can build the code inside the container so that the Windows version Java SDK does not matter

# Use a Docker image that already has Maven and Java 21 installed.
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the definition file first for better caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the actual source code
COPY src ./src

# Compile the code inside the container
RUN mvn clean package -DskipTests

# Switch to a tiny, empty Java 21 image for the final product.
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Grab the compiled JAR file from the Docker image that already has Maven and Java 21 installed.
COPY --from=build /app/target/Velosales-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]