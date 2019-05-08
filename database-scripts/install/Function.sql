--
--
------------------------------------------------------------------------------
-- Funções instr equivalentes a do Oracle.
-- Sintaxe: instr(string1, string2, [n], [m])
-- onde [] indica que os parâmetros são opcionais.
--
-- Procura em string1, a partir no n-ésimo caractere, a m-ésima
-- ocorrência de string2. Se n for negativo, procura para trás.
-- Se m não for fornecido, é assumido como sendo igual a 1
-- (procura a partir do primeiro caractere).
------------------------------------------------------------------------------
--
--

CREATE	FUNCTION
	instr
	(
		varchar,
		varchar
	)
	RETURNS
		integer
	AS
		$$
	DECLARE
		ln_position		integer;
	BEGIN
		ln_position		:=	instr($1, $2, 1);

		RETURN	ln_position;
	END;
$$ LANGUAGE plpgsql STRICT IMMUTABLE;


CREATE	FUNCTION
	instr
	(
		as_text			varchar,
		as_to_search		varchar,
		an_start		integer
	)
	RETURNS
		integer
	AS
		$$
	DECLARE
		ln_position		integer		NOT NULL	DEFAULT 0;
		ls_temp			varchar;
		ln_index		integer;
		ln_text_length		integer;
		ln_search_length	integer;
	BEGIN
		IF
			an_start		>	0
		THEN
			ls_temp			:=	substring(as_text FROM an_start);
			ln_position		:=	position(as_to_search IN ls_temp);

			IF
				ln_position		=	0
			THEN
				RETURN 0;
			ELSE
				RETURN ln_position + an_start - 1;
			END IF;
		ELSE
			ln_search_length	:=	char_length(as_to_search);
			ln_text_length		:=	char_length(as_text);
			ln_index		:=	ln_text_length + an_start - ln_search_length + 2;

			WHILE
				ln_index		>	0
			LOOP
				ls_temp			:=	substring(as_text FROM ln_index FOR ln_search_length);
				ln_position		:=	position(as_to_search IN ls_temp);

				IF
					ln_position		>	0
				THEN
					RETURN	ln_index;
				END IF;

				ln_index		:=	ln_index - 1;
			END LOOP;

			RETURN	0;
		END IF;
	END;
$$ LANGUAGE plpgsql STRICT IMMUTABLE;


CREATE	FUNCTION
	instr
	(
		as_text			varchar,
		as_to_search		varchar,
		an_start		integer,
		an_occur		integer
	)
	RETURNS
		integer
	AS
		$$
	DECLARE
		ln_position		integer		NOT NULL	DEFAULT 0;
		ln_occur_idx		integer		NOT NULL	DEFAULT 0;
		ls_temp			varchar;
		ln_index		integer;
		i			integer;
		ln_text_length		integer;
		ln_search_length	integer;
	BEGIN
		IF
			an_start		>	0
		THEN
			ln_index		:=	an_start;
			ls_temp			:=	substring(as_text FROM an_start);

			FOR
				i IN 1..an_occur
			LOOP
				ln_position		:=	position(as_to_search IN ls_temp);

				IF
					i			=	1
				THEN
					ln_index		:=	ln_index + ln_position - 1;
				ELSE
					ln_index		:=	ln_index + ln_position;
				END IF;

				ls_temp			:=	substring(as_text FROM ln_index + 1);
			END LOOP;

			IF
				ln_position		=	0
			THEN
				RETURN	0;
			ELSE
				RETURN	ln_index;
			END IF;
		ELSE
			ln_search_length	:=	char_length(as_to_search);
			ln_text_length		:=	char_length(as_text);
			ln_index		:=	ln_text_length + an_start - ln_search_length + 2;

			WHILE
				ln_index		>	0
			LOOP
				ls_temp			:=	substring(as_text FROM ln_index FOR ln_search_length);
				ln_position		:=	position(as_to_search IN ls_temp);

				IF
					ln_position		>	0
				THEN
					ln_occur_idx		:=	ln_occur_idx + 1;

					IF
						ln_occur_idx		=	an_occur
					THEN
						RETURN	ln_index;
					END IF;
				END IF;

				ln_index		:=	ln_index - 1;
			END LOOP;

			RETURN	0;
		END IF;
	END;
$$ LANGUAGE plpgsql STRICT IMMUTABLE;




--
--
------------------------------------------------------------------------------
-- Funcao to_number criada para ser compativel com o script do oracle
------------------------------------------------------------------------------
--
--
create	function	to_number
			(
				as_number		text
			)
	returns		numeric
	as
$$
	declare
		an_return		numeric;
	begin
		begin
			an_return		:=	as_number :: numeric;
		exception
			when	OTHERS
			then
				an_return		:=	null;
		end;

		return	an_return;

	end

$$ language plpgsql;
