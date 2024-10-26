# Usa uma imagem leve do NGINX para servir arquivos HTML estáticos
FROM nginx:alpine

# Define o diretório onde os arquivos serão copiados
WORKDIR /usr/share/nginx/html

# Copia todos os arquivos do front-end para o diretório padrão do NGINX
COPY . .

# Expõe a porta 80 para o NGINX
EXPOSE 80

# Comando padrão para iniciar o NGINX
CMD ["nginx", "-g", "daemon off;"]
