GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE modalidades TO admin;
GRANT SELECT(id_modalidade), UPDATE(id_modalidade), INSERT(id_modalidade), REFERENCES(id_modalidade) ON modalidades TO admin;
GRANT SELECT, UPDATE ON modalidades_id_modalidade_seq TO admin;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE graduacoes TO admin;
GRANT SELECT(id_graduacao), UPDATE(id_graduacao), INSERT(id_graduacao), REFERENCES(id_graduacao) ON graduacoes TO admin;
GRANT SELECT, UPDATE ON graduacoes_id_graduacao_seq TO admin;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE planos TO admin;
GRANT SELECT(id_plano), UPDATE(id_plano), INSERT(id_plano), REFERENCES(id_plano) ON planos TO admin;
GRANT SELECT, UPDATE ON planos_id_plano_seq TO admin;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE usuarios TO admin;
GRANT SELECT(id_usuario), UPDATE(id_usuario), INSERT(id_usuario), REFERENCES(id_usuario) ON usuarios TO admin;
GRANT SELECT, UPDATE ON usuarios_id_usuario_seq TO admin;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE cidades TO admin;
GRANT SELECT(id_cidade), UPDATE(id_cidade), INSERT(id_cidade), REFERENCES(id_cidade) ON cidades TO admin;
GRANT SELECT, UPDATE ON cidades_id_cidade_seq TO admin;

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE assiduidade TO admin;
GRANT SELECT(id_assiduidade), UPDATE(id_assiduidade), INSERT(id_assiduidade), REFERENCES(id_assiduidade) ON assiduidade TO admin;
GRANT SELECT, UPDATE ON assiduidade_id_assiduidade_seq TO admin;
