----------------------------------------------
----------------------------------------------
-- CRIA O ROLE ADMIN COM AS DEVIDAS PERMISS�ES
----------------------------------------------
----------------------------------------------

CREATE	ROLE		admin
	WITH		CREATEROLE
			LOGIN
			ENCRYPTED PASSWORD	'admin'
	;

--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	cidades
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	usuarios
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	alunos
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	alunos_codigo_aluno_seq
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	modalidades
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	graduacoes
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	planos
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	matriculas
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	UPDATE		ON	matriculas_codigo_matricula_seq
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	matriculas_modalidades
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	faturas_matriculas
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
GRANT	SELECT,
	INSERT,
	UPDATE,
	DELETE		ON	assiduidade
			TO	admin
	;
--
--
--------------------------------------------------------------------------------
--
--
