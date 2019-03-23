package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.model.CityModel;

public class CityDAO extends AbstractDAO<CityModel>{

private static final String TABLE_NAME = "cidades";
	
	private String columnId = "id_cidade";
	
	private String defaultOrderBy = "cidade";
	
	Connection connection;
	
	public CityDAO(Connection connection) throws SQLException{
		this.connection = connection;
	}

	@Override
	public List<CityModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		List<CityModel> cityList = new ArrayList<CityModel>();
		
		ResultSet rst = pst.executeQuery();
		
		while(rst.next()) {
			CityModel model = new CityModel();
			model.setId(rst.getInt("id_cidade"));
			model.setName(rst.getString("cidade"));
			model.setState(rst.getString("estado"));
			model.setCountry(rst.getString("pais"));
			
			cityList.add(model);
		}
		
		return cityList;
	}
	
	public List<CityModel> search(String word) throws SQLException {
		return null;
	}

	@Override
	public CityModel findById(Integer id) throws SQLException {
		CityModel model = null;
		
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

	@Override
	public CityModel insert(CityModel model) throws SQLException { return null; }

	@Override
	public boolean update(CityModel model) throws SQLException   { return false;}

	@Override
	public boolean delete(CityModel model) throws SQLException   { return false;}

	@Override
	public boolean deleteById(Integer id) throws SQLException    { return false;}
		
	/**
	 * Cria um objeto Model a partir do resultado obtido no banco de dados
	 * 
	 * @param rst
	 * @return CityModel
	 * @throws SQLException
	 */
	private CityModel createModelFromResultSet(ResultSet rst) throws SQLException {
		CityModel model = new CityModel();

		model.setId(rst.getInt("id_cidade"));
		model.setName(rst.getString("cidade"));
		model.setState(rst.getString("estado"));
		model.setCountry(rst.getString("pais"));

		return model;
	}
}

