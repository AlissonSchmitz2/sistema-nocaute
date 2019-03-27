package br.com.nocaute.model;

import java.util.Date;

public class RegistrationModalityModel extends AbstractModel {
	private Integer codigo_matricula;
	private Integer id_modalidade;
	private Integer id_graduacao;
	private Integer id_plano;
	private Date data_inicio;
	private Date data_fim;
	
	/**
	 * Relacionamentos
	 */
	private ModalityModel modality;
	private GraduationModel graduation;
	private PlanModel plan;
	
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
	 * Outros atributos
	 */
	public Integer getRegistrationCode() {
		return codigo_matricula;
	}
	public void setRegistrationCode(Integer registration_code) {
		this.codigo_matricula = registration_code;
	}
	
	
	/**
	 * @return id_modalidade
	 */
	public Integer getModalityId() {
		return id_modalidade;
	}
	/**
	 * id_modalidade
	 * @param modalityId
	 */
	public void setModalityId(Integer modalityId) {
		this.id_modalidade = modalityId;
	}
	
	/**
	 * @return id_graduacao
	 */
	public Integer getGraduationId() {
		return id_graduacao;
	}
	/**
	 * id_graduacao
	 * @param graduationId
	 */
	public void setGraduationId(Integer graduationId) {
		this.id_graduacao = graduationId;
	}
	
	/**
	 * @return id_plano
	 */
	public Integer getPlanId() {
		return id_plano;
	}
	/**
	 * id_plano
	 * @param graduationId
	 */
	public void setPlanId(Integer planId) {
		this.id_plano = planId;
	}
	
	/**
	 * @return data_inicio
	 */
	public Date getStartDate() {
		return data_inicio;
	}
	/**
	 * data_inicio
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.data_inicio = startDate;
	}
	
	/**
	 * @return data_fim
	 */
	public Date getFinishDate() {
		return data_fim;
	}
	/**
	 * data_fim
	 * @param finishDate
	 */
	public void setFinishDate(Date finishDate) {
		this.data_fim = finishDate;
	}
}
