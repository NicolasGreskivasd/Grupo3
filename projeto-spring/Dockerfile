# Use a imagem do Maven e JDK 17 para compilar a aplicação
FROM maven:3.8.5-openjdk-17 AS builder

# Defina o diretório de trabalho
WORKDIR /app

# Copie o código do projeto para o diretório de trabalho
COPY . .

# Compile o projeto e crie o pacote
RUN mvn clean package -DskipTests

# Use uma imagem leve do JDK 17 para o runtime
FROM openjdk:17-jdk-slim

# Defina o diretório de trabalho no contêiner final
WORKDIR /app

# Copie o .jar compilado da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta em que a aplicação será executada
EXPOSE 8080

# Comando para iniciar a aplicação
CMD ["java", "-jar", "app.jar"]
