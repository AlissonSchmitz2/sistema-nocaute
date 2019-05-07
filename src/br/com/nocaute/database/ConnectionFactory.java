package br.com.nocaute.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public static Connection getConnection(
			final String dbName,
			final String username,
			final String password) {
		return getConnection("localhost", "5432", dbName, username, password);
	}

	public static Connection getConnection(
			final String dbHost,
			final String port,
			final String dbName,
			final String username,
			final String password) {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:postgresql://" + dbHost + ":" + port + "/" + dbName, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}
}
