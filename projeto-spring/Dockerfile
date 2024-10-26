# Use uma imagem base que tenha Java e Maven instalados
FROM maven:3.9.9-jdk-11-slim AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo pom.xml e o wrapper para o contêiner
COPY pom.xml ./
COPY .mvn ./.mvn
COPY src ./src

# Executa o Maven para construir o JAR
RUN ./mvnw clean package -DskipTests

# Use uma imagem mais leve para rodar a aplicação
FROM openjdk:11-jre-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR gerado do estágio anterior
COPY --from=build /app/target/projeto-spring.jar ./projeto-spring.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "projeto-spring.jar"]
