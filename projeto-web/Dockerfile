# Dockerfile para servir aplicação web com Nginx
FROM nginx:alpine

# Define o diretório de trabalho no container
WORKDIR /usr/share/nginx/html

# Remove arquivos padrão do Nginx
RUN rm -rf ./*

# Copia o conteúdo da pasta `projeto-web` para o diretório de trabalho
COPY projeto-web/ .

# Expõe a porta padrão do Nginx
EXPOSE 80

# Comando padrão para iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]
