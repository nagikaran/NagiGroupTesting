# 1️⃣ Build Stage (Using Correct Maven Image)
FROM maven:3.8.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only the necessary files first (helps with caching)
COPY pom.xml .  
RUN mvn dependency:go-offline  

# Now copy the whole source code
COPY src ./src  

# Run the Maven build
RUN mvn clean package -DskipTests

# 2️⃣ Runtime Stage (Using OpenJDK 17)
FROM openjdk:17
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/NagiGroupDataManagement-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 9393

# Run the application
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
