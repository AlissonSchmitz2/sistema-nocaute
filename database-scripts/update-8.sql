create  or replace function data_alterecao_usuario()
returns trigger as $$
begin
	update usuarios set ultima_atualizacao = current_timestamp where usuario = new.usuario;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_usuarios_after_u
	after update
	ON usuarios
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_usuario();

create  or replace function data_alteracao_alunos()
returns trigger as $$
begin
	update alunos set ultima_atualizacao = current_timestamp where codigo_aluno = new.codigo_aluno;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_alunos_after_u
	after update
	ON alunos
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alteracao_alunos();



create  or replace function data_alterecao_assiduidade()
returns trigger as $$
begin
	update assiduidade set ultima_atualizacao = current_timestamp where id_assiduidade = new.id_assiduidade;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_assiduidade_after_u
	after update
	ON assiduidade
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_assiduidade();


create  or replace function data_alterecao_cidades()
returns trigger as $$
begin
	update cidades set ultima_atualizacao = current_timestamp where id_cidade= new.id_cidade;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_cidades_after_u
	after update
	ON cidades
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_cidades();

create  or replace function data_alterecao_faturas_matriculas()
returns trigger as $$
begin
	update faturas_matriculas set ultima_atualizacao = current_timestamp where codigo_matricula = new.codigo_matricula and data_vencimento = new.data_vencimento;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_faturas_matriculas_after_u
	after update
	ON faturas_matriculas 
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_faturas_matriculas ();

create  or replace function data_alterecao_graduacoes()
returns trigger as $$
begin
	update graduacoes set ultima_atualizacao = current_timestamp where id_graduacao = new.id_graduacao;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_graduacoes_after_u
	after update
	ON graduacoes
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_graduacoes();

create  or replace function data_alterecao_matriculas()
returns trigger as $$
begin
	update matriculas set ultima_atualizacao = current_timestamp where codigo_matricula = new.codigo_matricula;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_matriculas_after_u
	after update
	ON matriculas
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_matriculas();

create  or replace function data_alterecao_matriculas_modalidades()
returns trigger as $$
begin
	update matriculas_modalidades set ultima_atualizacao = current_timestamp where id_matricula_modalidade = new.id_matricula_modalidade;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_matriculas_modalidades_after_u
	after update
	ON matriculas_modalidades
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_matriculas_modalidades();

create  or replace function data_alterecao_modalidades()
returns trigger as $$
begin
	update modalidades set ultima_atualizacao = current_timestamp where id_modalidade = new.id_modalidade;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_modalidades_after_u
	after update
	ON modalidades
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_modalidades();


create  or replace function data_alterecao_planos()
returns trigger as $$
begin
	update planos set ultima_atualizacao = current_timestamp where id_plano = new.id_plano;
	return null;
end;
$$language 'plpgsql';

create trigger tgr_planos_after_u
	after update
	ON planos
	FOR EACH ROW
	WHEN (pg_trigger_depth() = 0) 
EXECUTE procedure data_alterecao_planos();