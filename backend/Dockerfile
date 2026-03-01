# Use official Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy all backend files into container
COPY . .

# Build the Spring Boot app (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Expose port (Spring Boot will use PORT env variable automatically)
EXPOSE 8080

# Run the generated jar file
CMD ["java", "-jar", "target/talenttrack-backend-0.0.1-SNAPSHOT.jar"]