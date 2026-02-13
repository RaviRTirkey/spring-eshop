# ---------- Stage 1: Build ----------
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copy everything
COPY . .

# If using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# If NOT using mvnw, use:
# RUN mvn clean package -DskipTests


# ---------- Stage 2: Run ----------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
