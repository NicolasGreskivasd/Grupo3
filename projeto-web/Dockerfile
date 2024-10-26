# Usa uma imagem base Node.js para construção
FROM node:16 AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos de dependência e instala as dependências
COPY package*.json ./
RUN npm install

# Copia o restante dos arquivos e executa o build
COPY . .
RUN npm run build

# Usa uma imagem NGINX para servir o front-end
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html

# Expõe a porta padrão do NGINX
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
