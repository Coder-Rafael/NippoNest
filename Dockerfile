# Usa a imagem do OpenJDK com Maven ou Gradle pré-instalado
FROM maven:3.8.6-openjdk-17 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o código-fonte para o container
COPY src /app/src
COPY pom.xml /app/pom.xml

# Executa o build do projeto
RUN mvn clean package

# Usa a imagem do OpenJDK para rodar a aplicação
FROM openjdk:17

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=build /app/target/nipponest-0.0.1-SNAPSHOT.jar app.jar

# Cria as pastas de uploads
RUN mkdir -p /app/uploads/users /app/uploads/products

# Copia a pasta uploads para dentro do container
COPY uploads /app/uploads

# Expõe a porta da aplicação
EXPOSE 8080

# Define o comando de execução da aplicação
CMD ["java", "-jar", "app.jar"]