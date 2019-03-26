package br.com.nocaute.model;

public class GraduationModel extends AbstractModel {

	private Integer id_graduacao;
	private Integer id_modalidade;
	private String nome_graduacao;

	/**
	 * id_graduacao
	 */
	public Integer getGraduationId() {
		return id_graduacao;
	}

	/**
	 * id_graduacao
	 * 
	 * @param graduationId
	 */
	public void setGraduationId(Integer graduationId) {
		this.id_graduacao = graduationId;
	}

	/**
	 * id_modalidade
	 */
	public Integer getModalityId() {
		return id_modalidade;
	}

	/**
	 * id_modalidade
	 * 
	 * @param modalityId
	 */
	public void setModalityId(Integer modalityId) {
		this.id_modalidade = modalityId;
	}

	/**
	 * nome_graduacao
	 */
	public String getName() {
		return nome_graduacao;
	}

	/**
	 * nome_graduacao
	 * 
	 * @param graduationName
	 */
	public void setGraduationName(String graduationName) {
		this.nome_graduacao = graduationName;
	}

}
