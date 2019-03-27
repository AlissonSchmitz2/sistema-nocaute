package br.com.nocaute.model;

import java.util.Date;

import br.com.nocaute.pojos.Graduation;
import br.com.nocaute.pojos.Modality;
import br.com.nocaute.pojos.Plan;

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
	private Modality modality;
	private Graduation graduation;
	private Plan plan;
	
	public Modality getModality() {
		return modality;
	}
	public void setModality(Modality modality) {
		this.modality = modality;
	}
	
	public Graduation getGraduation() {
		return graduation;
	}
	public void setGraduation(Graduation graduation) {
		this.graduation = graduation;
	}
	
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	
	/**
	 * Outros atributos
	 */
	public Integer getRegistration_code() {
		return codigo_matricula;
	}
	public void setRegistration_code(Integer registration_code) {
		this.codigo_matricula = registration_code;
	}
	
	
	/**
	 * @return id_modalidade
	 */
	public Integer getId_modalidade() {
		return id_modalidade;
	}
	/**
	 * id_modalidade
	 * @param modalityId
	 */
	public void setId_modalidade(Integer modalityId) {
		this.id_modalidade = modalityId;
	}
	
	/**
	 * @return id_graduacao
	 */
	public Integer getId_graduacao() {
		return id_graduacao;
	}
	/**
	 * id_graduacao
	 * @param graduationId
	 */
	public void setId_graduacao(Integer graduationId) {
		this.id_graduacao = graduationId;
	}
	
	/**
	 * @return id_plano
	 */
	public Integer getId_plano() {
		return id_plano;
	}
	/**
	 * id_plano
	 * @param graduationId
	 */
	public void setId_plano(Integer planId) {
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
