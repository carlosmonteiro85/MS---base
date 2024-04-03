set foreign_key_checks = 0;


INSERT INTO PERFILS (ID , DESCRICAO ) VALUES ( 5, 'CLIENTE');
INSERT INTO PERFILS (ID , DESCRICAO ) VALUES ( 3, 'MANAGER');
INSERT INTO PERFILS (ID , DESCRICAO ) VALUES ( 2, 'ADM');
INSERT INTO PERFILS (ID , DESCRICAO ) VALUES ( 4, 'MEDICO');
INSERT INTO PERFILS (ID , DESCRICAO ) VALUES ( 1, 'ROOT');

INSERT INTO especialidades (id, descricao) VALUES(1, 'Clinico Geral');
INSERT INTO especialidades (id, descricao) VALUES(2, 'Pediatra');
INSERT INTO especialidades (id, descricao) VALUES(3, 'Cirurgião');
INSERT INTO especialidades (id, descricao) VALUES(4, 'Nutricionista');
INSERT INTO especialidades (id, descricao) VALUES(5, 'Pisicólogo');

INSERT INTO PERMISSAO (id, TIPO_PERMISAO ) VALUES(1, 'READ');
INSERT INTO PERMISSAO (id, TIPO_PERMISAO ) VALUES(2, 'CREATE');
INSERT INTO PERMISSAO (id, TIPO_PERMISAO ) VALUES(3, 'UPDATE');
INSERT INTO PERMISSAO (id, TIPO_PERMISAO ) VALUES(4, 'DELETE');

-- root
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (1, 1);
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (1, 2);
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (1, 3);
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (1, 4);

-- ADMIN
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (2, 1);
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (2, 2);
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (2, 3);

-- MANAGER
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (3, 1);
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (3, 2);

-- CLIENT
INSERT INTO PERFILS_PERMISAO (ID_PERFIL , ID_PERMISSAO ) VALUES (4, 1);

INSERT INTO usuario(id, celular, data_nacimento, email, nome, telefone, id_especialidade, id_credencial, id_tipo_usuario) VALUES('f47ac10b58cc4372a5670e02b2c3d479', '6199999999', '1985-09-27', 'teste@teste', 'Carlos Monteiro', '999999999999', 1, 1, 5);

INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(1, 'Cliente', 'USER');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(2, 'Médico', 'MEDICO');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(3, 'Administrador', 'ADM');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(4, 'Gerente', 'MANAGER');
INSERT INTO tipo_usuario (id, descricao, marcador) VALUES(5, 'Root', 'ROOT');

INSERT INTO credenciais (id, cpf, email, password, `role`, username) VALUES (1, '123456789210', 'teste@teste', '	$2a$10$bgbOZXrMjLRknn9a1kvzreItEqd./B3HHUnucgaLHxWmFyT2pXMKi', 'ROOT', '123456789210-teste');
INSERT INTO token (id, expired, revoked, token, token_type, id_credencial) VALUES(1, 1, 1, 'eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUFOQUdFUiIsInN1YiI6InRlc3RlIiwiaWF0IjoxNzEwNTMzMTQ5LCJleHAiOjE3MTA1MzY3NDl9.tCtz0vTT1wFQgOciPlIWcG7eXRGlZP56A-_BM8DpCeI', 'BEARER', 1);

set foreign_key_checks = 1;