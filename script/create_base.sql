create database projdevsecops;
use projdevsecops;

CREATE TABLE usuarios (
id_usuario INT AUTO_INCREMENT PRIMARY KEY,
nome VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
senha VARCHAR(255) NOT NULL,
perfil VARCHAR(50) NOT NULL, -- Ex: 'usuario', 'administrador'
data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE perfis (
id_perfil INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT,
descricao TEXT
);
CREATE TABLE logs (
id_log INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT,
acao VARCHAR(255),
data_acao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ip_usuario VARCHAR(45)
);

CREATE TABLE comentarios (
id_comentario INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT,
id_postagem INT,
comentario TEXT,
data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE feedback (
id_feedback INT AUTO_INCREMENT PRIMARY KEY,
id_usuario INT,
comentario TEXT,
data_feedback TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);