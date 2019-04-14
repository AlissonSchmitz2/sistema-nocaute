package br.com.nocaute.dao.contracts;

import java.sql.SQLException;

public interface Insertable<T> {
	public T insert(T model) throws SQLException;
}
