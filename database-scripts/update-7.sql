ALTER TABLE alunos ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE assiduidade ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE cidades ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE faturas_matriculas ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE graduacoes ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE matriculas ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE matriculas_modalidades ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE modalidades ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE planos ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;
ALTER TABLE usuarios ADD ultima_atualizacao timestamptz DEFAULT current_timestamp NOT NULL;