# Etapa 1 - Construção da aplicação
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2 - Execução da aplicação
FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar



EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]