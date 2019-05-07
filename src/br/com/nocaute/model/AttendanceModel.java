package br.com.nocaute.model;

import java.util.Date;
import java.util.List;

public class AttendanceModel extends AbstractModel {

	private Integer codigo_matricula;
	private Date data_entrada;
	private Integer id_assiduidade;

	/**
	 * Relacionamentos
	 */
	private ModalityModel modality;
	private GraduationModel graduation;
	private PlanModel plan;
	private StudentModel student;
	private List<RegistrationModalityModel> modalities;

	/**
	 * @return StudentModel
	 */
	public StudentModel getStudent() {
		return student;
	}

	/**
	 * @param StudentModel student
	 */
	public void setStudent(StudentModel student) {
		this.student = student;
	}

	/**
	 * @return List<RegistrationModalityModel>
	 */
	public List<RegistrationModalityModel> getModalities() {
		return modalities;
	}

	/**
	 * @param List<RegistrationModalityModel> modalities
	 */
	public void setModalities(List<RegistrationModalityModel> modalities) {
		this.modalities = modalities;
	}

	public ModalityModel getModality() {
		return modality;
	}

	public void setModality(ModalityModel modality) {
		this.modality = modality;
	}

	public GraduationModel getGraduation() {
		return graduation;
	}

	public void setGraduation(GraduationModel graduation) {
		this.graduation = graduation;
	}

	public PlanModel getPlan() {
		return plan;
	}

	public void setPlan(PlanModel plan) {
		this.plan = plan;
	}

	/**
	 * @return id_assiduidade
	 */
	public Integer getId() {
		return id_assiduidade;
	}

	/**
	 * id_matricula_modalidade
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id_assiduidade = id;
	}

	/**
	 * @return codigo_matricula
	 */
	public Integer getRegistrationCode() {
		return codigo_matricula;
	}

	/**
	 * codigo_matricula
	 * 
	 * @param registrationCode
	 */
	public void setRegistrationCode(Integer registrationCode) {
		this.codigo_matricula = registrationCode;
	}

	/**
	 * @return data_entrada
	 */
	public Date getEnterDate() {
		return data_entrada;
	}

	/**
	 * data_inicio
	 * 
	 * @param startDate
	 */
	public void setEnterDate(Date enterDate) {
		this.data_entrada = enterDate;
	}

}
