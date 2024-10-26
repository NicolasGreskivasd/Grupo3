# Etapa de construção
FROM node:16 AS build

WORKDIR /app

# Copia o package.json e o package-lock.json
COPY package*.json ./

# Instala dependências
RUN npm install

# Copia o restante do código
COPY . .

# Executa a build do frontend
RUN npm run build

# Etapa de produção
FROM nginx:alpine

# Copia os arquivos de build para o Nginx
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
