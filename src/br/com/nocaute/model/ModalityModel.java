package br.com.nocaute.model;

//TODO: Reestruturar a tabela 'modalidades' no banco de dados para que essa classe funcione corretamente.

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
