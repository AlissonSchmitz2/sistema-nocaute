----------------------------------------------
----------------------------------------------
-- CRIACAO DE TABELAS
----------------------------------------------
----------------------------------------------

/*********************************************
CIDADES
*********************************************/

CREATE	TABLE				cidades
	(
	cidade				text
					NOT NULL
					,
	estado				char(2)
					NOT NULL
					,
	pais				text
					NOT NULL
					,
					CONSTRAINT cidades_pk
						PRIMARY KEY(cidade, estado, pais)
	)
;

/*********************************************
USUARIOS
*********************************************/

CREATE	TABLE				usuarios
	(
	usuario				text
					NOT NULL
					CONSTRAINT usuarios_pk
						PRIMARY KEY
					,
	perfil				text
					NOT NULL
	)
;

/*********************************************
ALUNOS
*********************************************/

CREATE	TABLE				alunos
	(
	codigo_aluno			serial
					CONSTRAINT alunos_pk
					PRIMARY KEY
					,
	aluno				text
					NOT NULL
					,
	data_nascimento			date
					,
	sexo				char(1)
					CONSTRAINT alunos_sexo
						check
						(
							sexo			in	('M', 'F')
						)
					,
	telefone			text
					,
	celular				text
					,
	email				text
					,
	observacao			text
					,
	endereco			text
					,
	numero				text
					,
	complemento			text
					,
	bairro				text
					,
	cidade				text
					,
	estado				char(2)
					,
	pais				text
					,
					CONSTRAINT alunos_enderecos_f2
						FOREIGN KEY(cidade, estado, pais)
							REFERENCES cidades(cidade, estado, pais)
						DEFERRABLE
					,
	cep				text
	)
;

--
--

CREATE	INDEX				alunos_1
	ON				alunos(aluno)
;

--
--

/*********************************************
MODALIDADES
*********************************************/

CREATE	TABLE				modalidades
	(
	modalidade			text
					NOT NULL
					CONSTRAINT modalidades_pk
						PRIMARY KEY
	)
;

/*********************************************
GRADUACOES
*********************************************/

CREATE	TABLE				graduacoes
	(
	modalidade			text
					NOT NULL
					,
					CONSTRAINT graduacoes_f1
						FOREIGN KEY(modalidade)
							REFERENCES modalidades(modalidade)
						DEFERRABLE
					,
	graduacao			text
					NOT NULL
					,
					CONSTRAINT graduacoes_pk
						PRIMARY KEY(modalidade, graduacao)
	)
;

/*********************************************
PLANOS
*********************************************/

CREATE	TABLE				planos
	(
	modalidade			text
					NOT NULL
					,
					CONSTRAINT planos_f1
						FOREIGN KEY(modalidade)
							REFERENCES modalidades(modalidade)
						DEFERRABLE
					,
	plano				text
					NOT NULL
					,
					CONSTRAINT planos_pk
						PRIMARY KEY(modalidade, plano)
					,
	valor_mensal			numeric(9,2)
					NOT NULL
	)
;

/*********************************************
MATRICULAS
*********************************************/

CREATE	TABLE				matriculas
	(
	codigo_matricula		serial
					CONSTRAINT matriculas_pk
						PRIMARY KEY
					,
	codigo_aluno			integer
					NOT NULL
					,
					CONSTRAINT matriculas_f1
						FOREIGN KEY(codigo_aluno)
							REFERENCES alunos(codigo_aluno)
						DEFERRABLE
					,
	data_matricula			date
					NOT NULL
					,
	dia_vencimento			integer
					NOT NULL
					,
	data_encerramento		date
	)
;

/*********************************************
MATRICULAS_MODALIDADES
*********************************************/

CREATE	TABLE				matriculas_modalidades
	(
	codigo_matricula		integer
					NOT NULL
					,
					CONSTRAINT matriculas_modalidades_f1
						FOREIGN KEY(codigo_matricula)
							REFERENCES matriculas(codigo_matricula)
						DEFERRABLE
					,
	modalidade			text
					NOT NULL
					,
					CONSTRAINT matriculas_modalidades_f2
						FOREIGN KEY(modalidade)
							REFERENCES modalidades(modalidade)
						DEFERRABLE
					,
					CONSTRAINT matriculas_modalidades_pk
						PRIMARY KEY(codigo_matricula, modalidade)
					,
	graduacao			text
					,
					CONSTRAINT matriculas_modalidades_f3
						FOREIGN KEY(modalidade, graduacao)
							REFERENCES graduacoes(modalidade, graduacao)
						DEFERRABLE
					,
	plano				text
					NOT NULL
					,
					CONSTRAINT matriculas_modalidades_f4
						FOREIGN KEY(modalidade, plano)
							REFERENCES planos(modalidade, plano)
						DEFERRABLE
					,
	data_inicio			date
					DEFAULT current_date
					,
	data_fim			date
	)
;

/*********************************************
FATURAS_MATRICULAS
*********************************************/

CREATE	TABLE				faturas_matriculas
	(
	codigo_matricula		integer
					NOT NULL
					,
					CONSTRAINT faturas_matriculas_f1
						FOREIGN KEY(codigo_matricula)
							REFERENCES matriculas(codigo_matricula)
						DEFERRABLE
					,
	data_vencimento			date
					NOT NULL
					,
					CONSTRAINT faturas_matriculas_pk
						PRIMARY KEY(codigo_matricula, data_vencimento)
					,
	valor				numeric(9,2)
					NOT NULL
					,
	data_pagamento			date
					,
	data_cancelamento		date
	)
;

/*********************************************
ASSIDUIDADE
*********************************************/

CREATE	TABLE				assiduidade
	(
	codigo_matricula		integer
					NOT NULL
					,
					CONSTRAINT assiduidade_f1
						FOREIGN KEY(codigo_matricula)
							REFERENCES matriculas(codigo_matricula)
						DEFERRABLE
					,
	data_entrada			timestamp
					NOT NULL
					DEFAULT localtimestamp
					,
					CONSTRAINT assiduidade_pk
						PRIMARY KEY(codigo_matricula, data_entrada)
	)
;