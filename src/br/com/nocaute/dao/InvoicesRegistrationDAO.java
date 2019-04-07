package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.dao.contracts.Selectable;
import br.com.nocaute.model.InvoicesRegistrationModel;

public class InvoicesRegistrationDAO extends AbstractDAO<InvoicesRegistrationModel>  implements Selectable<InvoicesRegistrationModel>{	
	private static final String TABLE_NAME = "faturas_matriculas";
	
	private String columnId = "codigo_matricula";
	
	private String defaultOrderBy = "codigo_matricula ASC";
	
	private String[] defaultValuesToInsert = new String[] {};
	
	private String[] columnsToInsert = new String[] {
			"codigo_matricula",
			"data_vencimento",
			"valor",
			"data_pagamento",
			"data_cancelamento"
	};
	
	private String[] columnsToUpdate = new String[] {
			"valor",
			"data_pagamento"
	};
	
	Connection connection;
	
	public InvoicesRegistrationDAO(Connection connection) throws SQLException{
		this.connection = connection;
		
		this.connection.setAutoCommit(false);
	}
		
	public InvoicesRegistrationModel insert(InvoicesRegistrationModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		pst.clearParameters();

		setParam(pst, 1, model.getRegistrationCode());
		setParam(pst, 2, model.getDueDate());
		setParam(pst, 3, model.getValue());
		setParam(pst, 4, model.getPaymentDate());
		setParam(pst, 5, model.getCancellationDate());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				return model;
			}
		}

		return null;
	}
	
	public boolean update(InvoicesRegistrationModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, "data_vencimento", columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getValue());
		setParam(pst, 2, model.getPaymentDate());

		// Identificador WHERE
		setParam(pst, 3, model.getDueDate());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}
	
	//TODO: Verificar utilidade.
	@Override
	public List<InvoicesRegistrationModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<InvoicesRegistrationModel> invoicesRegistrationList = new ArrayList<InvoicesRegistrationModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			InvoicesRegistrationModel model = createModelFromResultSet(rst);

			invoicesRegistrationList.add(model);
		}

		return invoicesRegistrationList;
	}
	
	//TODO: Verificar utilidade.
	@Override
	public InvoicesRegistrationModel findById(Integer id) throws SQLException {
		InvoicesRegistrationModel model = null;
		
		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, id);
		
		ResultSet rst = pst.executeQuery();
		
		if(rst.next()) {
			model = createModelFromResultSet(rst);
			
			return model;
		}
		
		return null;
	}
	
	/**
	 * Recupera a lista de faturas relacionadas a um determinado código de matrícula.
	 * 
	 * @param registrationCode
	 * @return invoicesRegistrationList
	 */
	public List<InvoicesRegistrationModel> getByRegistrationCode(Integer registrationCode) throws SQLException {		
		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, registrationCode);
		
		List<InvoicesRegistrationModel> invoicesRegistrationList = new ArrayList<InvoicesRegistrationModel>();
		
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()) {
			InvoicesRegistrationModel model = createModelFromResultSet(rst);
			
			invoicesRegistrationList.add(model);
		}
		
		return invoicesRegistrationList;
	}
	
	/**
	 * Recupera a lista de faturas entre duas datas de acordo com a situação da fatura.
	 * 
	 * @param startDate
	 * @param finishDate
	 * @param situation
	 * @return invoicesRegistrationList
	 */
	public List<InvoicesRegistrationModel> searchInvoices(String startDate, String finishDate, String situation) throws SQLException{
		String query = "";
		
		if (situation.equals("Todas")) {
			query = "SELECT * FROM " + TABLE_NAME + " WHERE data_vencimento BETWEEN '" + startDate + "'" + " AND '"
					+ finishDate + "'";
		} else if (situation.equals("Em Aberto")) {
			query = "SELECT * FROM " + TABLE_NAME + " WHERE data_vencimento BETWEEN '" + startDate + "'" + " AND '"
					+ finishDate + "'" + " AND data_pagamento IS NULL AND data_cancelamento IS NULL";
		} else if (situation.equals("Pagas")) {
			query = "SELECT * FROM " + TABLE_NAME + " WHERE data_vencimento BETWEEN '" + startDate + "'" + " AND '"
					+ finishDate + "'" + " AND data_pagamento IS NOT NULL";
		} else if (situation.equals("Canceladas")) {
			query = "SELECT * FROM " + TABLE_NAME + " WHERE data_vencimento BETWEEN '" + startDate + "'" + " AND '"
					+ finishDate + "'" + " AND data_cancelamento IS NOT NULL";
		}

		PreparedStatement pst = connection.prepareStatement(query);
		
		List<InvoicesRegistrationModel> invoicesRegistrationList = new ArrayList<InvoicesRegistrationModel>();
				
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()) {
			InvoicesRegistrationModel model = createModelFromResultSet(rst);
			
			invoicesRegistrationList.add(model);
		}
		
		return invoicesRegistrationList;
	}
	
	/**
	 * Cria um objeto Model a partir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return InvoicesRegistrationModel
	 * @throws SQLException
	 */
	private InvoicesRegistrationModel createModelFromResultSet(ResultSet rst) throws SQLException {
		InvoicesRegistrationModel model = new InvoicesRegistrationModel();

		model.setRegistrationCode(rst.getInt("codigo_matricula"));
		model.setDueDate(rst.getDate("data_vencimento"));
		model.setValue(rst.getFloat("valor"));
		model.setPaymentDate(rst.getDate("data_pagamento"));
		model.setCancellationDate(rst.getDate("data_cancelamento"));

		return model;
	}
}
