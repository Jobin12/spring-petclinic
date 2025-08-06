# === Stage 1: Build with Maven ===
FROM maven:3.8.7-openjdk-18-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q clean package -DskipTests

# === Stage 2: Runtime ===
FROM openjdk:18-jdk-slim
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
WORKDIR /home/spring
COPY --from=builder /app/target/spring-petclinic-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
