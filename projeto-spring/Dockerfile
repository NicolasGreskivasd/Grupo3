# Dockerfile para o Backend
FROM openjdk:11-jre-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR do projeto para o container
COPY target/*.jar app.jar

# Expõe a porta que a aplicação usará
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "app.jar"]
