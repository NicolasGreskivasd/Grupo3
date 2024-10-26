# Usa uma imagem base do OpenJDK para o Spring Boot
FROM openjdk:11-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo JAR da aplicação para o contêiner
COPY target/projeto-spring.jar projeto-spring.jar

# Expõe a porta padrão do Spring Boot (8080)
EXPOSE 8080

# Executa o JAR da aplicação
ENTRYPOINT ["java", "-jar", "/app/projeto-spring.jar"]
