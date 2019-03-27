package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.model.RegistrationModalityModel;

public class RegistrationModalityDAO extends AbstractDAO<RegistrationModalityModel> {
private static final String TABLE_NAME = "matriculas_modalidades";
	
	private String defaultOrderBy = "data_inicio";
	
	Connection connection;
	
	public RegistrationModalityDAO(Connection connection) throws SQLException{
		this.connection = connection;
		
		this.connection.setAutoCommit(false);
	}

	public RegistrationModalityModel findByRegistrationCode(Integer id) throws SQLException {
		/*RegistrationModalityModel model = null;
		
		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, id);
		
		ResultSet rst = pst.executeQuery();
		
		if(rst.next()) {
			model = createModelFromResultSet(rst);
			
			return model;
		}*/
		
		return null;
	}

	/**
	 * Cria um objeto Model a partir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return RegistrationModalityModel
	 * @throws SQLException
	 */
	private RegistrationModalityModel createModelFromResultSet(ResultSet rst) throws SQLException {
		RegistrationModalityModel model = new RegistrationModalityModel();

		//TODO: mapear resultSet para model
		/*model.setId(rst.getInt("id_cidade"));
		model.setName(rst.getString("cidade"));
		model.setState(rst.getString("estado"));
		model.setCountry(rst.getString("pais"));*/

		return model;
	}
}
