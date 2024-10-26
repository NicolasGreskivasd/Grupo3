# Usa a imagem do Nginx
FROM nginx:alpine

# Define o diretório de trabalho dentro do container
WORKDIR /usr/share/nginx/html

# Remove arquivos padrão do Nginx
RUN rm -rf ./*

# Copia todos os arquivos do projeto para o diretório de trabalho do Nginx
COPY projeto-web/ .

# Expõe a porta padrão do Nginx
EXPOSE 80

# Comando padrão para iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]
