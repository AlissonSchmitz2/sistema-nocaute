package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.model.UserModel;

public class UserDAO extends AbstractDAO<UserModel> {

	private static final String TABLE_NAME = "usuarios";

	private String columnId = "id_usuario";

	private String defaultOrderBy = "usuario ASC";

	private String[] columsToInsert = new String[] { "id_usuario", "usuario", "perfil" };

	private String[] defaultValuesToInsert = new String[] { "DEFAULT" };

	private String[] columsToUpdate = new String[] { "usuario", "perfil" };

	Connection connection;

	public UserDAO(Connection connection) throws Exception {
		this.connection = connection;
		
		this.connection.setAutoCommit(false);
	}

	@Override
	public List<UserModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<UserModel> userList = new ArrayList<UserModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			UserModel user = createModelFromResultSet(rst);

			userList.add(user);
		}

		return userList;
	}

	@Override
	public UserModel findById(Integer id) throws SQLException {
		UserModel model = null;

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
	public UserModel insert(UserModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		pst.clearParameters();

		setParam(pst, 1, model.getUser());
		setParam(pst, 2, model.getProfile());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(1);

				// Antes de retornar, seta o código ao objeto usuario
				model.setCode(lastInsertedCode);

				return model;
			}
		}

		return null;
	}

	@Override
	public boolean update(UserModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getUser());
		setParam(pst, 3, model.getProfile());

		// Identificador WHERE
		setParam(pst, 4, model.getCode());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(UserModel model) throws SQLException {
		return deleteById(model.getCode());
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
	 * Cria um objeto Model apartir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return UserModel
	 * @throws SQLException
	 */
	private UserModel createModelFromResultSet(ResultSet rst) throws SQLException {
		UserModel model = new UserModel();

		model.setCode(rst.getInt("id"));
		model.setUser(rst.getString("usuario"));
		model.setProfile(rst.getString("perfil"));

		return model;

	}

}
