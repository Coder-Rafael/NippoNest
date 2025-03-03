# Usa uma imagem com Maven para compilar o projeto
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Usa o OpenJDK para rodar a aplicação
FROM openjdk:17
WORKDIR /app

# Copia o .jar gerado na primeira etapa
COPY --from=build /app/target/nipponest-0.0.1-SNAPSHOT.jar app.jar

# Cria a pasta de uploads
RUN mkdir -p /app/uploads/users /app/uploads/products

CMD ["java", "-jar", "app.jar"]
