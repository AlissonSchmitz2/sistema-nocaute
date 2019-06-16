create  or replace function data_alterecao()
returns trigger as $$
begin
	new.ultima_atualizacao = current_timestamp;
	return new;
end;
$$language 'plpgsql';

create trigger tgr_usuarios_after_u
	before update
	ON usuarios
	FOR EACH ROW
EXECUTE procedure data_alterecao();

create trigger tgr_alunos_after_u
	before update
	ON alunos
	FOR EACH ROW
EXECUTE procedure data_alteracao();

create trigger tgr_assiduidade_after_u
	before update
	ON assiduidade
	FOR EACH ROW
EXECUTE procedure data_alterecao();

create trigger tgr_cidades_after_u
	before update
	ON cidades
	FOR EACH ROW
EXECUTE procedure data_alterecao();

create trigger tgr_faturas_matriculas_after_u
	before update
	ON faturas_matriculas 
	FOR EACH ROW
EXECUTE procedure data_alterecao();

create trigger tgr_graduacoes_after_u
	before update
	ON graduacoes
	FOR EACH ROW
EXECUTE procedure data_alterecao();

create trigger tgr_matriculas_after_u
	before update
	ON matriculas
	FOR EACH ROW
EXECUTE procedure data_alterecao();

create trigger tgr_matriculas_modalidades_after_u
	before update
	ON matriculas_modalidades
	FOR EACH ROW
EXECUTE procedure data_alterecao();

create trigger tgr_modalidades_after_u
	before update
	ON modalidades
	FOR EACH ROW
EXECUTE procedure data_alterecao();


create trigger tgr_planos_after_u
	before update
	ON planos
	FOR EACH ROW
EXECUTE procedure data_alterecao();