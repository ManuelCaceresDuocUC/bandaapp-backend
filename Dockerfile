# Etapa 1: Build con Maven y JDK 17
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar la app en imagen ligera de Java
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto que usará Render
EXPOSE 8080

# Comando para iniciar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
