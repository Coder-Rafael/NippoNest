# Usa a imagem do OpenJDK
FROM openjdk:17

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR da aplicação para o container
COPY target/nipponest-0.0.1-SNAPSHOT.jar app.jar

#CRIA A PASTA DE UPLOADS
RUN mkdir -p /app/uploads/users /app/uploads/products

# Expõe a porta da aplicação
EXPOSE 8080

# Define o comando de execução da aplicação
CMD ["java", "-jar", "app.jar"]
