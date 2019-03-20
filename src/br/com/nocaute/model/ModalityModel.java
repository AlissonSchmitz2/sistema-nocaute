package br.com.nocaute.model;

public class ModalityModel extends AbstractModel {

	private Integer id_modalidade;
	private String nome_modalidade;

	/**
	 * nome_modalidade
	 */
	public String getModalityName() {
		return nome_modalidade;
	}

	/**
	 * nome_modalidade
	 * 
	 * @param modalityName
	 */
	public void setModalityName(String modalityName) {
		this.nome_modalidade = modalityName;
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

}
