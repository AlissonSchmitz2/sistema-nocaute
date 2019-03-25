package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.model.GraduationModel;

public class GraduationDAO extends AbstractDAO<GraduationModel> {
	private static final String TABLE_NAME = "graduacoes";
	
	private String columnId = "id_graduacao";
	
	private String defaultOrderBy = "graduacao ASC";
	
	private String[] defaultValuesToInsert = new String[] {
			"DEFAULT"
	};
	
	private String[] columnsToInsert = new String[] {
			"id_graduacao",
			"id_modalidade",
			"graduacao"
	};
	
	private String[] columnsToUpdate = new String[] {
			"graduacao"
	};
	
	Connection connection;
	
	public GraduationDAO(Connection connection) throws SQLException{
		this.connection = connection;
		
		this.connection.setAutoCommit(false);
	}
	
	@Override
	public List<GraduationModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<GraduationModel> graduationsList = new ArrayList<GraduationModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			GraduationModel model = createModelFromResultSet(rst);

			graduationsList.add(model);
		}

		return graduationsList;
	}
	
	@Override
	public GraduationModel findById(Integer id) throws SQLException {
		GraduationModel model = null;

		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		if (rst.next()) {
			model = createModelFromResultSet(rst);
		}

		return model;
	}
	
	public List<GraduationModel> findAllByModalityId(Integer id) throws SQLException {
		GraduationModel model = null;
		List<GraduationModel> graduationsList = new ArrayList<>();
		
		String query = getFindByQuery(TABLE_NAME, "id_modalidade", "*", defaultOrderBy);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			model = createModelFromResultSet(rst);
			graduationsList.add(model);
		}

		return graduationsList;
	}
	
	@Override
	public GraduationModel insert(GraduationModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		pst.clearParameters();

		setParam(pst, 1, model.getModalityId());
		setParam(pst, 2, model.getGraduationName());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);

				// Antes de retornar, seta o id ao objeto graduação
				model.setGraduationId(lastInsertedCode);

				return model;
			}
		}

		return null;
	}
	
	@Override
	public boolean update(GraduationModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getGraduationName());

		// Identificador WHERE
		setParam(pst, 2, model.getGraduationId());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}
	
	@Override
	public boolean delete(GraduationModel model) throws SQLException {
		return deleteById(model.getGraduationId());
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
	 * @return GraduationModel
	 * @throws SQLException
	 */
	private GraduationModel createModelFromResultSet(ResultSet rst) throws SQLException {
		GraduationModel model = new GraduationModel();

		model.setGraduationId(rst.getInt("id_graduacao"));
		model.setModalityId(rst.getInt("id_modalidade"));
		model.setGraduationName(rst.getString("graduacao"));

		return model;
	}
	
}
