# ---- Build Stage ----
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and POM files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Give executable permission to mvnw
RUN chmod +x mvnw

# Build Spring Boot application (skip tests to speed up build)
RUN ./mvnw clean package -DskipTests

# ---- Runtime Stage ----
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8000

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
