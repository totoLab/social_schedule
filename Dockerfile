# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the pom.xml file
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the project source
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Expose the port the app runs on
EXPOSE 7300

# Run the JAR file
CMD ["java", "-jar", "target/social-schedule-0.0.1-SNAPSHOT.jar"]
