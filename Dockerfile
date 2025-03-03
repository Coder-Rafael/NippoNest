# Estágio de build
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Copia o código-fonte e o arquivo de configuração do Maven
COPY src /app/src
COPY pom.xml /app/pom.xml

# Baixa o Maven e executa o build
RUN apt-get update && apt-get install -y maven && mvn clean package

# Estágio de runtime
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copia o JAR gerado
COPY --from=build /app/target/nipponest-0.0.1-SNAPSHOT.jar app.jar

# Cria as pastas de uploads
RUN mkdir -p /app/uploads/users /app/uploads/products

# Copia a pasta uploads
COPY uploads /app/uploads

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]