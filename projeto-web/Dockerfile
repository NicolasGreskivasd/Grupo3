# Dockerfile para o Frontend
FROM node:14

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o package.json e o package-lock.json para instalar as dependências
COPY package*.json ./

# Instala as dependências
RUN npm install

# Copia o restante dos arquivos para o container
COPY . .

# Define o caminho base da aplicação para localizar o index.html em /projeto-web
ENV PUBLIC_URL=/projeto-web

# Compila a aplicação
RUN npm run build

# Expõe a porta da aplicação
EXPOSE 3000

# Comando para iniciar a aplicação
CMD ["npm", "start"]
