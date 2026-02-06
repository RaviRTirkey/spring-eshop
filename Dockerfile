# Step 1: Use a lightweight JDK runtime
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file from your host to the container
# Replace 'target/*.jar' with 'build/libs/*.jar' if using Gradle
COPY target/*.jar app.jar

# Step 4: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]