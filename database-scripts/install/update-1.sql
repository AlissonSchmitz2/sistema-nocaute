/* #################### CIDADES #################### */

/* # Remove FK alunos_enderecos_f2 na tabela "alunos" */
ALTER TABLE "alunos" DROP CONSTRAINT alunos_enderecos_f2;

/* # Adiciona coluna id_cidade para tabela "cidades" */
ALTER TABLE cidades ADD COLUMN id_cidade serial NOT NULL;

/* # Remove PK na tabela "cidades" */
ALTER TABLE cidades DROP CONSTRAINT cidades_pk;

/* # Seta id_cidade como PK da tabela "cidades" */
ALTER TABLE cidades ADD CONSTRAINT cidades_pk PRIMARY KEY(id_cidade);

/* #################### ALUNOS #################### */

/* # Remove colunas cidade, estado e pais da tabela "alunos" */
ALTER TABLE alunos DROP COLUMN cidade;
ALTER TABLE alunos DROP COLUMN estado;
ALTER TABLE alunos DROP COLUMN pais;

/* # Adicionar FK id_cidade na tabela "alunos" */
ALTER TABLE alunos ADD COLUMN id_cidade integer;

/* # Adiciona FK alunos_enderecos_f2 na tabela "alunos" */
ALTER TABLE alunos ADD CONSTRAINT alunos_enderecos_f2 FOREIGN KEY (id_cidade) 
REFERENCES cidades(id_cidade) MATCH SIMPLE 
ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

/* #################### ASSIDUIDADE #################### */

/* # Adiciona PK id_assiduidade na tabela "assiduidade" */
ALTER TABLE assiduidade DROP CONSTRAINT assiduidade_pk;
ALTER TABLE assiduidade ADD COLUMN id_assiduidade serial;
ALTER TABLE assiduidade ADD CONSTRAINT assiduidade_pk PRIMARY KEY(id_assiduidade);

/* #################### GRADUAÇÕES #################### */

/* # Remove FK matriculas_modalidades */
ALTER TABLE matriculas_modalidades DROP CONSTRAINT matriculas_modalidades_f3;

/* # Add nova PK */
ALTER TABLE graduacoes DROP CONSTRAINT graduacoes_pk;
ALTER TABLE graduacoes ADD COLUMN id_graduacao serial;
ALTER TABLE graduacoes ADD CONSTRAINT graduacoes_pk PRIMARY KEY(id_graduacao);

/* Adiciona FK id_graduacao na tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades ADD COLUMN id_graduacao integer;

/* # Remove coluna graduacao da tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades DROP COLUMN graduacao;

/* Recria FK modalidade */
ALTER TABLE matriculas_modalidades ADD CONSTRAINT matriculas_modalidades_f3 FOREIGN KEY (id_graduacao) 
REFERENCES graduacoes(id_graduacao) MATCH SIMPLE 
ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

/* #################### MODALIDADES #################### */

/* # Remove FK graduacoes */
ALTER TABLE graduacoes DROP CONSTRAINT graduacoes_f1;

/* # Remove FK planos */
ALTER TABLE planos DROP CONSTRAINT planos_f1;

/* # Remove FK matriculas_modalidades */
ALTER TABLE matriculas_modalidades DROP CONSTRAINT matriculas_modalidades_f2;

/* # Remove FK matriculas_modalidades */
ALTER TABLE matriculas_modalidades DROP CONSTRAINT matriculas_modalidades_f4;

/* # Add nova PK */
ALTER TABLE modalidades DROP CONSTRAINT modalidades_pk;
ALTER TABLE modalidades ADD COLUMN id_modalidade serial;
ALTER TABLE modalidades ADD CONSTRAINT modalidades_pk PRIMARY KEY(id_modalidade);

/* Adiciona FK id_modalidade na tabela "graduacoes" */
ALTER TABLE graduacoes ADD COLUMN id_modalidade integer;

/* # Remove coluna modalidade da tabela "graduacoes" */
ALTER TABLE graduacoes DROP COLUMN modalidade;

/* Recria FK id_modalidade na tabela "graduacoes" */
ALTER TABLE graduacoes ADD CONSTRAINT graduacoes_f1 FOREIGN KEY (id_modalidade) 
REFERENCES modalidades(id_modalidade) MATCH SIMPLE 
ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

/* Adiciona FK id_modalidade na tabela "planos" */
ALTER TABLE planos ADD COLUMN id_modalidade integer;

/* # Remove coluna modalidade da tabela "planos" */
ALTER TABLE planos DROP COLUMN modalidade;

/* Recria FK id_modalidade na tabela "planos" */
ALTER TABLE planos ADD CONSTRAINT planos_f1 FOREIGN KEY (id_modalidade) 
REFERENCES modalidades(id_modalidade) MATCH SIMPLE 
ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

/* Adiciona FK id_modalidade na tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades ADD COLUMN id_modalidade integer;

/* # Remove coluna modalidade da tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades DROP COLUMN modalidade;

/* Recria FK id_modalidade na tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades ADD CONSTRAINT matriculas_modalidades_f2 FOREIGN KEY (id_modalidade) 
REFERENCES modalidades(id_modalidade) MATCH SIMPLE 
ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

/* #################### PLANOS #################### */

/* # Add nova PK */
ALTER TABLE planos ADD COLUMN id_plano serial;
ALTER TABLE planos ADD CONSTRAINT planos_pk PRIMARY KEY(id_plano);

/* Adiciona FK id_modalidade na tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades ADD COLUMN id_plano integer;

/* # Remove coluna modalidade da tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades DROP COLUMN plano;

/* Recria FK id_plano na tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades ADD CONSTRAINT matriculas_modalidades_f4 FOREIGN KEY (id_plano) 
REFERENCES planos(id_plano) MATCH SIMPLE 
ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

/* #################### USUÁRIOS #################### */

/* # Adiciona PK id_usuario na tabela "usuarios" */
ALTER TABLE usuarios DROP CONSTRAINT usuarios_pk;
ALTER TABLE usuarios ADD COLUMN id_usuario serial;
ALTER TABLE usuarios ADD CONSTRAINT usuarios_pk PRIMARY KEY(id_usuario);