# Dockerfile para o Frontend
FROM node:14

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o package.json e o package-lock.json para instalar as dependências
COPY package*.json ./

# Instala as dependências
RUN npm install

# Atualiza Babel para a versão mais recente
RUN npm install @babel/core@^7.16.0 --save-dev

# Copia o restante dos arquivos para o container
COPY . .

# Cria o diretório src e move o index.js para lá temporariamente
RUN mkdir -p src && cp ./index.js ./src/index.js

# Mover index.html temporariamente para a pasta public para o build
RUN mkdir -p public && cp ./index.html ./public/index.html

# Define o caminho base da aplicação para localizar o index.html em /projeto-web
ENV PUBLIC_URL=/projeto-web

# Compila a aplicação
RUN npm run build

# Expõe a porta da aplicação
EXPOSE 3000

# Comando para iniciar a aplicação
CMD ["npm", "start"]
