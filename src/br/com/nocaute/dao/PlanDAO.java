package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.model.PlanModel;
import br.com.nocaute.model.ModalityModel;

public class PlanDAO extends AbstractDAO<PlanModel> {
	private static final String TABLE_NAME = "planos";
	
	private String columnId = "id_plano";
	
	private String defaultOrderBy = "plano ASC";
	
	private String[] defaultValuesToInsert = new String[] {
			"DEFAULT"
	};
	
	private String[] columnsToInsert = new String[] {
			"id_plano",
			"id_modalidade",
			"plano",
			"valor_mensal"
	};
	
	private String[] columnsToUpdate = new String[] {
			"id_modalidade",
			"plano",
			"valor_mensal"
	};
	
	Connection connection;
	
	public PlanDAO(Connection connection) throws SQLException {
		this.connection = connection;
		
		this.connection.setAutoCommit(false);
	}

	@Override
	public List<PlanModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<PlanModel> plansList = new ArrayList<PlanModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			PlanModel model = createModelFromResultSet(rst);

			plansList.add(model);
		}

		return plansList;
	}
	
	public List<PlanModel> search(String word) throws SQLException {
		String query = "SELECT p.*, m.modalidade FROM " + TABLE_NAME
				+ " AS p LEFT JOIN modalidades AS m ON p.id_modalidade=m.id_modalidade WHERE p.plano ILIKE ? OR m.modalidade ILIKE ? ORDER BY p."
				+ defaultOrderBy;
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, "%" + word + "%");
		setParam(pst, 2, "%" + word + "%");

		List<PlanModel> plansList = new ArrayList<PlanModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			PlanModel model = createModelFromResultSet(rst);
			
			if (Integer.valueOf(rst.getInt("id_modalidade")) != null) {
				ModalityModel modality = new ModalityModel();
				modality.setModalityId(rst.getInt("id_modalidade"));
				modality.setName(rst.getString("modalidade"));
				
				model.setModality(modality);
			}

			plansList.add(model);
		}

		return plansList;
	}

	@Override
	public PlanModel findById(Integer id) throws SQLException {
		PlanModel model = null;

		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		if (rst.next()) {
			model = createModelFromResultSet(rst);
		}

		return model;
	}
	
	public List<PlanModel> findByModalityId(Integer id) throws SQLException {
		PlanModel model = null;
		List<PlanModel> plansList = new ArrayList<>();
		
		String query = getFindByQuery(TABLE_NAME, "id_modalidade", "*", defaultOrderBy);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			model = createModelFromResultSet(rst);
			plansList.add(model);
		}

		return plansList;
	}

	@Override
	public PlanModel insert(PlanModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		pst.clearParameters();

		setParam(pst, 1, model.getModalityId());
		setParam(pst, 2, model.getName());
		setParam(pst, 3, model.getMonthlyValue());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedId = rs.getInt(columnId);

				// Antes de retornar, seta o código ao objeto aluno
				model.setPlanId(lastInsertedId);

				return model;
			}
		}

		return null;
	}

	@Override
	public boolean update(PlanModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getModalityId());
		setParam(pst, 2, model.getName());
		setParam(pst, 3, model.getMonthlyValue());

		// Identificador WHERE
		setParam(pst, 4, model.getPlanId());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(PlanModel model) throws SQLException {
		return deleteById(model.getPlanId());
	}

	@Override
	public boolean deleteById(Integer id) throws SQLException {
		String query = getDeleteQuery(TABLE_NAME, columnId);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	/**
	 * Cria um objeto Model a partir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return PlanModel
	 * @throws SQLException
	 */
	private PlanModel createModelFromResultSet(ResultSet rst) throws SQLException {
		PlanModel model = new PlanModel();

		model.setPlanId(rst.getInt("id_plano"));
		model.setModalityId(rst.getInt("id_modalidade"));
		model.setName(rst.getString("plano"));
		model.setMonthlyValue(rst.getBigDecimal("valor_mensal"));

		return model;
	}
	
}
