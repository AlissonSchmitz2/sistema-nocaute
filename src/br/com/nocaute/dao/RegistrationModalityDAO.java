package br.com.nocaute.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
				" WHERE mm.codigo_matricula=? ORDER BY mm." + defaultOrderBy;

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, registrationCode);

		List<RegistrationModalityModel> modalitiesList = new ArrayList<RegistrationModalityModel>();

		ResultSet relationshipRst = pst.executeQuery();

		while (relationshipRst.next()) {
			// Atributos do model
			RegistrationModalityModel registrationModality = createModelFromResultSet(relationshipRst);

			// Relacionamentos
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
			// Por hora, não existe necessidade de setar todos os atributos de planos pois é
			// só um relacionamento
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
		String query = getUpdateQuery(TABLE_NAME, columnId, columnsToUpdate);

		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, model.getRegistrationCode());
		setParam(pst, 2, model.getGraduationId());
		setParam(pst, 3, model.getModalityId());
		setParam(pst, 4, model.getPlanId());
		setParam(pst, 5, model.getStartDate());
		setParam(pst, 6, model.getFinishDate());

		// Identificador WHERE
		setParam(pst, 7, model.getId());

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	@Override
	public boolean delete(RegistrationModalityModel model) throws SQLException {
		return deleteById(model.getId());
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

	public boolean deleteByRegistrationCode(Integer id) throws SQLException {
		String query = getDeleteQuery(TABLE_NAME, "codigo_matricula");
		PreparedStatement pst = connection.prepareStatement(query);

		setParam(pst, 1, id);

		int result = pst.executeUpdate();
		if (result > 0) {
			connection.commit();

			return true;
		}

		return false;
	}

	public boolean sync(Integer registrationCode, List<RegistrationModalityModel> models) throws SQLException {
		// REMOVE todas as modalidades que não estão presentes na lista
		String deleteQuery = "DELETE FROM " + TABLE_NAME + " WHERE NOT (" + columnId + " = ANY (?))" +
				" AND codigo_matricula = ?";

		PreparedStatement deletePst = connection.prepareStatement(deleteQuery);

		List<String> existentIds = models.stream()
				.filter(model -> model.getId() != null)
				.map(model -> {
					return model.getId().toString();
				})
				.collect(Collectors.toList());

		deletePst.setString(1, String.join(",", existentIds));
		deletePst.setInt(2, registrationCode);

		Array parsedIds = connection.createArrayOf("integer", existentIds.toArray());
		deletePst.setArray(1, parsedIds);

		deletePst.executeUpdate();

		// UPDATE as modalidades já existentes
		models.stream()
				.filter(model -> model.getId() != null)
				.forEach(model -> {
					try {
						update(model);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});

		// INSERT as novas modalidades
		models.stream()
				.filter(model -> model.getId() == null)
				.forEach(model -> {
					try {
						insert(model);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});

		connection.commit();

		return true;
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

		model.setId(rst.getInt(columnId));
		model.setRegistrationCode(rst.getInt("codigo_matricula"));
		model.setModalityId(rst.getInt("id_modalidade"));
		model.setGraduationId(rst.getInt("id_graduacao"));
		model.setPlanId(rst.getInt("id_plano"));
		model.setStartDate(rst.getDate("data_inicio"));
		model.setFinishDate(rst.getDate("data_fim"));

		return model;
	}
}
