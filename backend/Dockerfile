FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Build project
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/talenttrack-backend-0.0.1-SNAPSHOT.jar"]ackend-0.0.1-SNAPSHOT.jar"]