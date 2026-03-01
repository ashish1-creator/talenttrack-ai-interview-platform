FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

# Make mvnw executable and build
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/talenttrack-backend-0.0.1-SNAPSHOT.jar"]