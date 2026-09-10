CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255),
    creation_time DATETIME(6),
    modification_time DATETIME(6)
);

CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE Tarefas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255),
    status VARCHAR(255),
    creation_time DATETIME(6),
    modification_time DATETIME(6),
    id_user BIGINT,
    id_categoria BIGINT,
    CONSTRAINT fk_tarefas_usuario FOREIGN KEY (id_user) REFERENCES usuario(id),
    CONSTRAINT fk_tarefas_categoria FOREIGN KEY (id_categoria) REFERENCES category(id)
);
