DELETE FROM assiduidade;
DELETE FROM faturas_matriculas;
DELETE FROM matriculas_modalidades;
DELETE FROM matriculas;

ALTER TABLE public.faturas_matriculas ADD COLUMN quantidade_modalidade INTEGER;