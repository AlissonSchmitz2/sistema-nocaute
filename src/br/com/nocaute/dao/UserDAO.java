package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.dao.contracts.Searchable;
import br.com.nocaute.model.UserModel;

public class UserDAO extends AbstractCrudDAO<UserModel> implements Searchable<UserModel> {

	private static final String TABLE_NAME = "usuarios";

	private String columnId = "id_usuario";

	private String defaultOrderBy = "usuario ASC";

	private String[] columsToInsert = new String[] {
			"id_usuario",
			"usuario",
			"perfil"
	};

	private String[] defaultValuesToInsert = new String[] { "DEFAULT" };

	private String[] columsToUpdate = new String[] {
			"usuario",
			"perfil"
	};

	Connection connection;

	public UserDAO(Connection connection) throws Exception {
		this.connection = connection;

		connection.setAutoCommit(false);

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
	public List<UserModel> search(String word) throws SQLException {
		String query = "";
		PreparedStatement pst = null;

		query = "SELECT * FROM " + TABLE_NAME + " WHERE LOWER(usuario) LIKE LOWER(?) ORDER BY " + defaultOrderBy;
		pst = connection.prepareStatement(query);

		setParam(pst, 1, "%" + word + "%");

		List<UserModel> userList = new ArrayList<UserModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			UserModel model = createModelFromResultSet(rst);

			userList.add(model);
		}

		return userList;
	}

	public String searchPassword(String word)throws SQLException {
		String query = "";
		PreparedStatement pst = null;

		query = "select rolname, rolpassword from pg_roles where rolname = ?";
		pst = connection.prepareStatement(query);

		setParam(pst, 1, word);
		
		ResultSet rst = pst.executeQuery();

		if(rst.next()) {
			return rst.getString("rolpassword");
		}else {
			return "";
		}
		
		
	}
	public UserModel searchByUser(String word) throws SQLException {
		UserModel model = null;

		String query = "select * from usuarios where usuario = ?";
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, word);

		ResultSet rst = pst.executeQuery();

		if (rst.next()) {
			model = createModelFromResultSet(rst);
		}

		return model;
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
		return insert(model, true);
	}
	
	public UserModel insert(UserModel model, boolean createUser) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columsToInsert, defaultValuesToInsert);

		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

		pst.clearParameters();

		setParam(pst, 1, model.getUser());
		setParam(pst, 2, model.getProfile());
		
		System.out.println(pst);

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);

				// Antes de retornar, seta o código ao objeto usuario
				model.setCode(lastInsertedCode);

				if (createUser) {
					createUser(model);
				}
				
				return model;
			}
		}

		return null;
	}

	public boolean createUser(UserModel model) throws SQLException {

		String query = "create	role		" + model.getUser() +
				"	with		login" +
				"			encrypted password		'" + new String(model.getPassword()) + "'" +
				"			in role				admin";

		PreparedStatement pst = connection.prepareStatement(query);

		pst.executeUpdate();
		connection.commit();

		return true;
	}

	@Override
	public boolean update(UserModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getUser());
		setParam(pst, 2, model.getProfile());

		// Identificador WHERE
		setParam(pst, 3, model.getCode());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			//updateUser(model);
			return true;
		}
		return false;
	}

	public boolean updateUser(UserModel model) throws SQLException {
		String query = " alter	role		" + model.getUser() +
				"		with	encrypted password		'" + model.getPassword() + "'";

		PreparedStatement pst = connection.prepareStatement(query);

		pst.executeUpdate();
		connection.commit();

		return true;
	}

	@Override
	public boolean delete(UserModel model) throws SQLException {
		return deleteById(model.getCode()) && deleteUser(model.getUser());
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

	public boolean deleteUser(String user) throws SQLException {
		String query = "drop role " + user;
		PreparedStatement pst = connection.prepareStatement(query);

		pst.executeUpdate();
		connection.commit();

		return true;
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

		model.setCode(rst.getInt("id_usuario"));
		model.setUser(rst.getString("usuario"));
		model.setProfile(rst.getString("perfil"));

		return model;

	}

}
