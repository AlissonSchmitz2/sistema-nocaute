package br.com.nocaute.model;

public class ModalityModel extends AbstractModel {

	private Integer id_modalidade;
	private String nome_modalidade;

	/**
	 * nome_modalidade
	 */
	public String getName() {
		return nome_modalidade;
	}

	/**
	 * nome_modalidade
	 * 
	 * @param modalityName
	 */
	public void setName(String name) {
		this.nome_modalidade = name;
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
