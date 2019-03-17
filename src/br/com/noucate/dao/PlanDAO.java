package br.com.noucate.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.model.PlanModel;

//TODO: Reestruturar a tabela 'planos' no banco de dados para que essa classe funcione corretamente.
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
			"nome_plano",
			"valor_mensal"
		};
	
	private String[] columnsToUpdate = new String[] {
			"nome_plano",
			"valor_mensal"
		};
	
	Connection connection;
	
	public PlanDAO(Connection connection) throws SQLException {
		this.connection = connection;
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

	@Override
	public PlanModel insert(PlanModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		pst.clearParameters();

		setParam(pst, 1, model.getModalityId());
		setParam(pst, 2, model.getPlanName());
		setParam(pst, 3, model.getMonthlyValue());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);

				// Antes de retornar, seta o id ao objeto plano.
				model.setModalityId(lastInsertedCode);

				return model;
			}
		}

		return null;
	}

	@Override
	public boolean update(PlanModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getPlanName());
		setParam(pst, 2, model.getMonthlyValue());

		// Identificador WHERE
		setParam(pst, 3, model.getPlanId());

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
		model.setPlanName(rst.getString("nome_plano"));
		model.setMonthlyValue(rst.getFloat("valor_mensal"));

		return model;
	}
	
}
