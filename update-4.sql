TRUNCATE assiduidade CASCADE;
TRUNCATE faturas_matriculas CASCADE;
TRUNCATE matriculas_modalidades CASCADE;
TRUNCATE matriculas CASCADE;

ALTER TABLE faturas_matriculas ADD COLUMN quantidade_modalidade INTEGER;