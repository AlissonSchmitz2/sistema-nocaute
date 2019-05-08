@echo off
set path_old=%path%
set path=C:\Arquivos de programas\PostgreSQL\9.0\bin;%path%
psql -U postgres -f Create_Database.sql
pause
set path=%path_old%
set path_old=
