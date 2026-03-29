# Step 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-22 AS build
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Run the application using JDK
FROM eclipse-temurin:22-jdk-jammy
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]