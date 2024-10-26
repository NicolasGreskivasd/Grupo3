# Usar a imagem Nginx leve
FROM nginx:alpine

# Copia os arquivos estáticos da sua aplicação frontend para o diretório padrão do Nginx
COPY ./projeto-web /usr/share/nginx/html

# Exponha a porta 80
EXPOSE 80

# Comando para iniciar o Nginx
CMD ["nginx", "-g", "daemon off;"]
