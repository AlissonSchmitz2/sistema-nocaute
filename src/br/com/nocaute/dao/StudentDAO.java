package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.model.StudentModel;

public class StudentDAO extends AbstractDAO<StudentModel> {
	private static final String TABLE_NAME = "alunos";
	
	private String columnId = "codigo_aluno";
	
	private String defaultOrderBy = "aluno ASC";
	
	private String[] columnsToInsert = new String[] {
		"codigo_aluno",
		"aluno",
		"data_nascimento",
		"sexo",
		"telefone",
		"celular",
		"email",
		"observacao",
		"endereco",
		"numero",
		"complemento",
		"bairro",
		"id_cidade",
		"cep"
	};
	
	private String[] defaultValuesToInsert = new String[] {
		"DEFAULT"
	};
	
	private String[] columnsToUpdate = new String[] {
		"aluno",
		"data_nascimento",
		"sexo",
		"telefone",
		"celular",
		"email",
		"observacao",
		"endereco",
		"numero",
		"complemento",
		"bairro",
		"id_cidade",
		"cep"
	};
	
	Connection connection;
	
	public StudentDAO(Connection connection) throws SQLException {
		this.connection = connection;
	}
		
	@Override
	public List<StudentModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		List<StudentModel> studentsList = new ArrayList<StudentModel>();
		
		ResultSet rst = pst.executeQuery();
		
		while (rst.next()) {
			StudentModel model = createModelFromResultSet(rst);
			
			studentsList.add(model);
		}
		
		return studentsList;
	}

	@Override
	public StudentModel findById(Integer id) throws SQLException {
		StudentModel model = null;
		
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
	public StudentModel insert(StudentModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);
		
		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		pst.clearParameters();
		
		setParam(pst, 1, model.getName());
		setParam(pst, 2, model.getBirthDate());
		setParam(pst, 3, model.getGenre());
		setParam(pst, 4, model.getTelephone());
		setParam(pst, 5, model.getMobilePhone());
		setParam(pst, 6, model.getEmail());
		setParam(pst, 7, model.getObservation());
		setParam(pst, 8, model.getAddress());
		setParam(pst, 9, model.getNumber());
		setParam(pst, 10, model.getAddressComplement());
		setParam(pst, 11, model.getNeighborhood());
		setParam(pst, 12, model.getCityId());
		setParam(pst, 13, model.getPostalCode());
		
		int result = pst.executeUpdate();
        if (result > 0) {
			connection.commit();
			
			ResultSet rs = pst.getGeneratedKeys();
	        if(rs.next()) {
	            int lastInsertedCode = rs.getInt(columnId);
	            
	            // Antes de retornar, seta o código ao objeto aluno
	            model.setCode(lastInsertedCode);
	            
	            return model;
	        }
		}
        
        return null;
	}

	@Override
	public boolean update(StudentModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, model.getName());
		setParam(pst, 2, model.getBirthDate());
		setParam(pst, 3, model.getGenre());
		setParam(pst, 4, model.getTelephone());
		setParam(pst, 5, model.getMobilePhone());
		setParam(pst, 6, model.getEmail());
		setParam(pst, 7, model.getObservation());
		setParam(pst, 8, model.getAddress());
		setParam(pst, 9, model.getNumber());
		setParam(pst, 10, model.getAddressComplement());
		setParam(pst, 11, model.getNeighborhood());
		setParam(pst, 12, model.getCityId());
		setParam(pst, 13, model.getPostalCode());
		
		// Identificador WHERE
		setParam(pst, 14, model.getCode());
		
		int result = pst.executeUpdate();
        if (result > 0) {
			connection.commit();
			
			return true;
        }
        
        return false;
	}

	@Override
	public boolean delete(StudentModel model) throws SQLException {
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
	 * @param rst
	 * @return StudentModel
	 * @throws SQLException
	 */
	private StudentModel createModelFromResultSet(ResultSet rst) throws SQLException {
		StudentModel model = new StudentModel();
		
		model.setCode(rst.getInt("codigo_aluno"));
		model.setName(rst.getString("aluno"));
		model.setBirthDate(rst.getDate("data_nascimento"));
		model.setGenre(rst.getString("sexo").charAt(0));
		model.setTelephone(rst.getString("telefone"));
		model.setMobilePhone(rst.getString("celular"));
		model.setEmail(rst.getString("email"));
		model.setObservation(rst.getString("observacao"));
		model.setAddress(rst.getString("endereco"));
		model.setNumber(rst.getString("numero"));
		model.setAddressComplement(rst.getString("complemento"));
		model.setNeighborhood(rst.getString("bairro"));
		model.setCityId(rst.getInt("id_cidade"));
		model.setPostalCode(rst.getString("cep"));
		
		return model;
	}
}
