package br.com.noucate.dao;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import br.com.nocaute.model.Model;

public abstract class AbstractDAO<T extends Model> {
	public abstract List<T> selectAll() throws SQLException;
	
	public abstract T findById(Integer id) throws SQLException;
	
	public abstract T insert(T model) throws SQLException;
	
	public abstract boolean update(T model) throws SQLException;
	
	public abstract boolean delete(T model) throws SQLException;
	
	public abstract boolean deleteById(Integer id) throws SQLException;
	
	public void setParam(PreparedStatement pst, int position, Object value) throws SQLException {
		if (value == null) {
			pst.setNull(position, Types.NULL);
		} else if (value instanceof Integer) {
			pst.setInt(position, (int) value);
		} else if (value instanceof String) {
			pst.setString(position, (String) value);
		} else if (value instanceof Date) {
			pst.setDate(position, new java.sql.Date(((Date) value).getTime()));			
			// pst.setTimestamp(position, new Timestamp(((java.util.Date) value).getTime()));
		} else if (value instanceof Character) {
			pst.setString(position,((Character) value).toString());
		} else if (value	instanceof	Boolean) {
			pst.setString(position,((Boolean) value).booleanValue() ? "S" : "N");
		} else if (value	instanceof	BigDecimal) {
			pst.setBigDecimal(position,(BigDecimal) value);
		}
	}
	
	protected final String getSelectAllQuery(String tableName, String columns, String defaultOrderBy) {
		return "SELECT " + columns + " FROM " + tableName + " ORDER BY " + defaultOrderBy;
	}
	
	protected final String getFindByQuery(String tableName, String identifierColumn, String columns, String defaultOrderBy) {
		return "SELECT " + columns + " FROM " + tableName + " WHERE " + identifierColumn + "=? ORDER BY " + defaultOrderBy;
	}
	
	protected final String getInsertQuery(String tableName, String[] columns, String[] values) {
		String query = "INSERT INTO " + tableName + " (";
		
		// Concatena colunas
		query += String.join(",", columns);
		
		query += ") VALUES (";
		
		// Concatena valores
		String concatenedValues = "";
		for (int i = 0; i < columns.length; i++) {
			if (!concatenedValues.isEmpty()) {
				concatenedValues += ", ";
			}
			
			if(values.length > i && values[i] != null) {
				concatenedValues += values[i];
			} else {
				concatenedValues += "?";
			}
        } 
		
		query += concatenedValues + ")";
		
		return query;
	}
	
	protected final String getUpdateQuery(String tableName, String identifierColumn, String[] columns) {
		String query = "UPDATE " + tableName + " SET ";
		
		// Concatena colunas
		query += String.join("=?, ", columns);
		query += "=? WHERE " + identifierColumn + "=?";
		
		return query;
	}
	
	protected final String getDeleteQuery(String tableName, String identifierColumn) {
		return "DELETE FROM " + tableName + " WHERE " + identifierColumn + "=?";
	}
}
