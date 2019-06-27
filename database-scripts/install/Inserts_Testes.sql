insert into cidades values ('Crici�ma', 'SC', 'Brasil');
insert into enderecos values ('Rua Bela Vista', 'S�o Crist�v�o', 'Crici�ma', 'SC', 'Brasil', 88802560);
insert into alunos values('Rodrigo', '25/06/1986', 'M', '34373737', '99444444', 'rodrigo@rodrigo.com', 'Aluno VBI.');
insert into alunos_enderecos values ('Rodrigo', 'Rua Bela Vista', '401', null, 'S�o Crist�v�o', 'Crici�ma', 'SC', 'Brasil', 88802560);
insert into modalidades values ('Taekwondo');
insert into graduacoes values ('Taekwondo', 'Iniciante');
insert into planos values ('Taekwondo', '2 X Por Semana', 100);
insert into matriculas values ('Rodrigo', current_date, 'Taekwondo', 'Iniciante', '2 X Por Semana', 10);
insert into contas_alunos values ('Rodrigo', current_date + 30, 100, null);
