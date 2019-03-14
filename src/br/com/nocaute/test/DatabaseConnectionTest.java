package br.com.nocaute.test;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.nocaute.database.ConnectionFactory;

public class DatabaseConnectionTest {
public static void main(String[] args) {
		
		Connection conn = ConnectionFactory.getConnection("master", "admin", "admin");
		
		try {
			conn.setAutoCommit(false);
			System.out.println("Conectado com sucesso!!!");
		} catch (SQLException e) {
			e.getErrorCode();
		}
	}
}
