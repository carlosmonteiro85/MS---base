set foreign_key_checks = 0;

TRUNCATE TABLE credenciais ;
TRUNCATE TABLE token ;
TRUNCATE TABLE tipo_usuario ;
TRUNCATE TABLE especialidades ;
TRUNCATE TABLE usuario ;
TRUNCATE TABLE token_seq ;

INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(1, 'Cliente', 'USER');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(2, 'Médico', 'MEDICO');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(3, 'Administrador', 'ADM');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(4, 'Gerente', 'MANAGER');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(5, 'Root', 'ROOT');

INSERT INTO especialidades (id, descricao) VALUES(1, 'Clinico Geral');
INSERT INTO especialidades (id, descricao) VALUES(2, 'Pediatra');
INSERT INTO especialidades (id, descricao) VALUES(3, 'Cirurgião');
INSERT INTO especialidades (id, descricao) VALUES(4, 'Nutricionista');
INSERT INTO especialidades (id, descricao) VALUES(5, 'Pisicólogo');

INSERT INTO credenciais (id, cpf, email, password, `role`, username) VALUES (1, '123456789210', 'teste@teste', '$2a$10$qKCpEKy11yXP42R8xrGibu2qm7wM4do6qGOAHjf6wK0As/fA.0tM2', 'ROOT', '123456789210-teste');
INSERT INTO token (id, expired, revoked, token, token_type, id_credencial) VALUES(1, 1, 1, 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUFOQUdFUiIsInN1YiI6InRlc3RlIiwiaWF0IjoxNzEwNTMzMTQ5LCJleHAiOjE3MTA1MzY3NDl9.tCtz0vTT1wFQgOciPlIWcG7eXRGlZP56A-_BM8DpCeI', 'BEARER', 1);
INSERT INTO token_seq(next_val) VALUES(1);

INSERT INTO usuario(id, celular, data_nacimento, email, nome, telefone, id_especialidade, id_credencial, id_tipo_usuario) VALUES(1, '6199999999', '1985-09-27', 'teste@teste', 'Carlos Monteiro', '999999999999', 1, 1, 5);


set foreign_key_checks = 1;