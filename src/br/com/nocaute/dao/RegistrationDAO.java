package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.dao.contracts.Searchable;
import br.com.nocaute.model.RegistrationModel;
import br.com.nocaute.model.StudentModel;
import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;
import br.com.nocaute.pojos.RegistrationModality;

public class RegistrationDAO extends AbstractCrudDAO<RegistrationModel> implements Searchable<RegistrationModel> {

private static final String TABLE_NAME = "matriculas";
	
	private String columnId = "codigo_matricula";
	
	private String defaultOrderBy = "codigo_matricula ASC";
	
	private String[] columnsToInsert = new String[] {
		"codigo_matricula",
		"codigo_aluno",
		"data_matricula",
		"dia_vencimento",
		"data_encerramento"
	};
	
	private String[] defaultValuesToInsert = new String[] {
		"DEFAULT"
	};
	
	private String[] columnsToUpdate = new String[] {
		"data_matricula",
		"dia_vencimento",
		"data_encerramento"		
	};
	
	Connection connection;
	
	public RegistrationDAO(Connection connection) throws SQLException {
		this.connection = connection;
		
		this.connection.setAutoCommit(false);
	}

	@Override
	public List<RegistrationModel> selectAll() throws SQLException {
		String query = getSelectAllQuery(TABLE_NAME, "*", defaultOrderBy);

		PreparedStatement pst = connection.prepareStatement(query);

		List<RegistrationModel> registrationsList = new ArrayList<RegistrationModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			RegistrationModel model = createModelFromResultSet(rst);

			registrationsList.add(model);
		}

		return registrationsList;
	}
	
	@Override
	public List<RegistrationModel> search(String word) throws SQLException {
		String query = "";
		PreparedStatement pst = null;

		try {
			int code = Integer.parseInt(word);

			query = "SELECT r.*, a.aluno FROM " + TABLE_NAME
					+ " AS r LEFT JOIN alunos AS a ON r.codigo_aluno=a.codigo_aluno WHERE a.aluno ILIKE ? OR r.codigo_matricula=? ORDER BY a.aluno"
	;
			pst = connection.prepareStatement(query);

			setParam(pst, 2, code);

		} catch (NumberFormatException e) {
			query = "SELECT r.*, a.aluno FROM " + TABLE_NAME
					+ " AS r LEFT JOIN alunos AS a ON r.codigo_aluno=a.codigo_aluno WHERE a.aluno ILIKE ? ORDER BY a.aluno";
			pst = connection.prepareStatement(query);
		}

		setParam(pst, 1, "%" + word + "%");

		List<RegistrationModel> registrationsList = new ArrayList<RegistrationModel>();

		ResultSet rst = pst.executeQuery();

		while (rst.next()) {
			RegistrationModel model = createModelFromResultSet(rst);
			
			if (Integer.valueOf(rst.getInt("codigo_aluno")) != null) {
				StudentModel student = new StudentModel();
				//Para o propósito de listagem, somente código e nome do aluno são suficientes
				student.setCode(Integer.valueOf(rst.getInt("codigo_aluno")));
				student.setName(rst.getString("aluno"));
				
				model.setStudent(student);
			}

			registrationsList.add(model);
		}

		return registrationsList;
	}
	
	@Override
	public RegistrationModel findById(Integer id) throws SQLException {
		RegistrationModel model = null;

		String query = getFindByQuery(TABLE_NAME, columnId, "*", defaultOrderBy);
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		if (rst.next()) {
			model = createModelFromResultSet(rst);
		}

		return model;
	}
	
	public RegistrationModel findByIdWithRelationships(Integer id) throws SQLException {
		RegistrationModel model = null;

		String query = "SELECT r.*, a.aluno FROM " + TABLE_NAME
				+ " AS r LEFT JOIN alunos AS a ON r.codigo_aluno=a.codigo_aluno WHERE r." + columnId + "=?";
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);
		ResultSet rst = pst.executeQuery();

		if (rst.next()) {
			model = createModelFromResultSet(rst);
			
			//Adiciona relacionamento Aluno
			if (Integer.valueOf(rst.getInt("codigo_aluno")) != null) {
				StudentModel student = new StudentModel();
				//Para o propósito de listagem, somente código e nome do aluno são suficientes
				student.setCode(Integer.valueOf(rst.getInt("codigo_aluno")));
				student.setName(rst.getString("aluno"));
				
				model.setStudent(student);
			}
			
			//Adiciona relacionamento Modalidades
			String relationshipQuery = "SELECT mm.*, m.modalidade, g.graduacao, p.plano" +
			" FROM matriculas_modalidades AS mm LEFT JOIN modalidades AS m ON mm.id_modalidade=m.id_modalidade" +
			" LEFT JOIN graduacoes AS g ON mm.id_graduacao=g.id_graduacao" +
			" LEFT JOIN planos AS p ON mm.id_plano=p.id_plano" +
			" WHERE mm.codigo_matricula=?";
			
			PreparedStatement relationshipPst = connection.prepareStatement(relationshipQuery);
			
			setParam(relationshipPst, 1, id);

			List<RegistrationModality> modalitiesList = new ArrayList<RegistrationModality>();

			ResultSet relationshipRst = relationshipPst.executeQuery();

			while (relationshipRst.next()) {
				RegistrationModality modality = new RegistrationModality();
				modality.setRegistration_code(model.getRegistrationCode());
				modality.setModality(new Modality(relationshipRst.getInt("id_modalidade"), relationshipRst.getString("modalidade")));
				modality.setGraduation(new Graduation(relationshipRst.getInt("id_graduacao"), relationshipRst.getString("graduacao")));
				modality.setPlan(new Plan(relationshipRst.getInt("id_plano"), relationshipRst.getString("plano")));
				modality.setStartDate(relationshipRst.getDate("data_inicio"));
				modality.setFinishDate(relationshipRst.getDate("data_fim"));

				modalitiesList.add(modality);
			}
			
			model.setModalities(modalitiesList);
		}

		return model;
	}
	
	@Override
	public RegistrationModel insert(RegistrationModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);
		
		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		pst.clearParameters();
		
		setParam(pst, 1, model.getStudentCode());
		setParam(pst, 2, model.getRegistrationDate());
		setParam(pst, 3, model.getExpirationDay());
		setParam(pst, 4, model.getClosingDate());
		
		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			ResultSet rs = pst.getGeneratedKeys();
			if (rs.next()) {
				int lastInsertedCode = rs.getInt(columnId);
				
				// Seta o código ao objeto matrícula
				model.setRegistrationCode(lastInsertedCode);
				
				//Insere as modalidades
				insertRegistrationModalities(lastInsertedCode, model.getModalities());

				return model;
			}
		}

		return null;
	}
	
	private List<RegistrationModality> insertRegistrationModalities(Integer registrationCode, List<RegistrationModality> registrationModalities) throws SQLException {
		String query = getInsertQuery("matriculas_modalidades", new String[] {
			"codigo_matricula",
			"id_graduacao",
			"id_modalidade",
			"id_plano",
			"data_inicio",
			"data_fim"
		}, new String[]{});
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		registrationModalities.forEach(modality -> {
			try {
				pst.clearParameters();
				
				setParam(pst, 1, registrationCode);
				setParam(pst, 2, modality.getGraduation().getId());
				setParam(pst, 3, modality.getModality().getId());
				setParam(pst, 4, modality.getPlan().getId());
				setParam(pst, 5, modality.getStartDate());
				setParam(pst, 6, modality.getFinishDate());
				
				int result = pst.executeUpdate();
				if (result > 0) {
					connection.commit();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		
		return registrationModalities;
	}
	
	@Override
	public boolean update(RegistrationModel model) throws SQLException {
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getRegistrationDate());
		setParam(pst, 2, model.getExpirationDay());
		setParam(pst, 3, model.getClosingDate());

		// Identificador WHERE
		setParam(pst, 4, model.getRegistrationCode());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}
	
	@Override
	public boolean delete(RegistrationModel model) throws SQLException {
		//Remove modalidades antes de remover a matrícula
		deleteRegistrationModalities(model.getRegistrationCode(), model.getModalities());
		
		//Remove a matrícula
		return deleteById(model.getRegistrationCode());
	}
	
	private boolean deleteRegistrationModalities(Integer registrationCode, List<RegistrationModality> registrationModalities) throws SQLException {
		String query = getDeleteQuery("matriculas_modalidades", "codigo_matricula");
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, registrationCode);
		
		registrationModalities.forEach(modality -> {
			int result;
			try {
				result = pst.executeUpdate();
				if (result > 0) {
					connection.commit();
				}
			} catch (SQLException error) {
				error.printStackTrace();
			}
		});
		
		return true;
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
	 * @return RegistrationModel
	 * @throws SQLException
	 */
	private RegistrationModel createModelFromResultSet(ResultSet rst) throws SQLException {
		RegistrationModel model = new RegistrationModel();

		model.setRegistrationCode(rst.getInt("codigo_matricula"));
		model.setStudentCode(rst.getInt("codigo_aluno"));
		model.setRegistrationDate(rst.getDate("data_matricula"));
		model.setExpirationDay(rst.getInt("dia_vencimento"));
		model.setClosingDate(rst.getDate("data_encerramento"));

		return model;
	}
}
