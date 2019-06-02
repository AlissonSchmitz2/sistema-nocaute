/* # Adiciona coluna id_matricula_modalidade para tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades ADD COLUMN id_matricula_modalidade serial NOT NULL;

/* # Seta id_matricula_modalidade como PK da tabela "matriculas_modalidades" */
ALTER TABLE matriculas_modalidades ADD CONSTRAINT matriculas_modalidades_pk PRIMARY KEY(id_matricula_modalidade);

/* # Permissões */
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE matriculas_modalidades TO admin;
GRANT SELECT(id_matricula_modalidade), UPDATE(id_matricula_modalidade), INSERT(id_matricula_modalidade), REFERENCES(id_matricula_modalidade) ON matriculas_modalidades TO admin;
GRANT SELECT, UPDATE ON matriculas_modalidades_id_matricula_modalidade_seq TO admin;