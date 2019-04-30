ALTER TABLE faturas_matriculas ADD COLUMN quantidade_modalidade INTEGER;

TRUNCATE assiduidade;
TRUNCATE faturas_matriculas;
TRUNCATE matriculas_modalidades;
TRUNCATE matriculas;