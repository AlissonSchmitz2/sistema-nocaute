package br.com.nocaute.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.nocaute.dao.contracts.Crud;
import br.com.nocaute.model.GraduationModel;
import br.com.nocaute.model.ModalityModel;
import br.com.nocaute.model.PlanModel;
import br.com.nocaute.model.RegistrationModalityModel;

public class RegistrationModalityDAO extends AbstractDAO<RegistrationModalityModel> implements Crud<RegistrationModalityModel> {
	private static final String TABLE_NAME = "matriculas_modalidades";
	
	private String columnId = "id_matricula_modalidade";
	
	private String defaultOrderBy = "data_inicio";

	private String[] columnsToInsert = new String[] {
		"id_matricula_modalidade",
		"codigo_matricula",
		"id_graduacao",
		"id_modalidade",
		"id_plano",
		"data_inicio",
		"data_fim"
	};
	
	private String[] defaultValuesToInsert = new String[] {
		"DEFAULT"
	};
	
	private String[] columnsToUpdate = new String[] {
		"codigo_matricula",
		"id_graduacao",
		"id_modalidade",
		"id_plano",
		"data_inicio",
		"data_fim"	
	};
	
	Connection connection;
	
	public RegistrationModalityDAO(Connection connection) throws SQLException {
		this.connection = connection;
		
		this.connection.setAutoCommit(false);
	}

	public List<RegistrationModalityModel> getByRegistrationCode(Integer registrationCode) throws SQLException {
		String query = "SELECT mm.*, m.modalidade, g.graduacao, p.plano" +
				" FROM matriculas_modalidades AS mm LEFT JOIN modalidades AS m ON mm.id_modalidade=m.id_modalidade" +
				" LEFT JOIN graduacoes AS g ON mm.id_graduacao=g.id_graduacao" +
				" LEFT JOIN planos AS p ON mm.id_plano=p.id_plano" +
				" WHERE mm.codigo_matricula=?";
		
		PreparedStatement pst = connection.prepareStatement(query);
		
		setParam(pst, 1, registrationCode);
		
		List<RegistrationModalityModel> modalitiesList = new ArrayList<RegistrationModalityModel>();

		ResultSet relationshipRst = pst.executeQuery();

		while (relationshipRst.next()) {
			RegistrationModalityModel registrationModality = new RegistrationModalityModel();
			registrationModality.setId(relationshipRst.getInt(columnId));
			registrationModality.setRegistrationCode(registrationCode);
			registrationModality.setModalityId(relationshipRst.getInt("id_modalidade"));
			registrationModality.setGraduationId(relationshipRst.getInt("id_graduacao"));
			registrationModality.setPlanId(relationshipRst.getInt("id_plano"));
			registrationModality.setStartDate(relationshipRst.getDate("data_inicio"));
			registrationModality.setFinishDate(relationshipRst.getDate("data_fim"));
			
			ModalityModel modality = new ModalityModel();
			modality.setModalityId(registrationModality.getModalityId());
			modality.setName(relationshipRst.getString("modalidade"));
			registrationModality.setModality(modality);
			
			GraduationModel graduation = new GraduationModel();
			graduation.setGraduationId(registrationModality.getGraduationId());
			graduation.setGraduationName(relationshipRst.getString("graduacao"));
			graduation.setModalityId(registrationModality.getModalityId());
			registrationModality.setGraduation(graduation);
			
			PlanModel plan = new PlanModel();
			plan.setPlanId(relationshipRst.getInt("id_plano"));
			plan.setName(relationshipRst.getString("plano"));
			//Por hora, não existe necessidade de setar todos os atributos
			registrationModality.setPlan(plan);

			modalitiesList.add(registrationModality);
		}
		
		return modalitiesList;
	}
	
	@Override
	public RegistrationModalityModel insert(RegistrationModalityModel model) throws SQLException {
		String query = getInsertQuery(TABLE_NAME, columnsToInsert, defaultValuesToInsert);
		
		PreparedStatement pst = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		pst.clearParameters();
		
		setParam(pst, 1, model.getRegistrationCode());
		setParam(pst, 2, model.getGraduationId());
		setParam(pst, 3, model.getModalityId());
		setParam(pst, 4, model.getPlanId());
		setParam(pst, 5, model.getStartDate());
		setParam(pst, 6, model.getFinishDate());
		
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
	
	@Override
	public boolean update(RegistrationModalityModel model) throws SQLException {
		/*String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getName());

		// Identificador WHERE
		setParam(pst, 2, model.getModalityId());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}*/

		return false;
	}
		
	@Override
	public boolean delete(RegistrationModalityModel model) throws SQLException {
		return false;
	}

	@Override
	public boolean deleteById(Integer id) throws SQLException {
		throw new SQLException("Não implementado");
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
