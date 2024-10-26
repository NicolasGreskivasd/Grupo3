# Usar a imagem do Node.js
FROM node:16 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos de configuração do Node.js
COPY package*.json ./

# Instala as dependências
RUN npm install

# Copia o restante dos arquivos
COPY . .

# Executa o build da aplicação
RUN npm run build

# Usar a imagem do Nginx para servir a aplicação
FROM nginx:alpine

# Copia os arquivos construídos para o diretório do Nginx
COPY --from=build /app/build /usr/share/nginx/html

# Expondo a porta 80
EXPOSE 80

# Comando para executar o Nginx
CMD ["nginx", "-g", "daemon off;"]
