package br.com.nocaute.model;

//TODO: Reestruturar a tabela 'planos' no banco de dados para que essa classe funcione corretamente.
public class PlanModel extends AbstractModel {

	private Integer id_plano;
	private Integer id_modalidade;
	private String nome_plano;
	private float valor_mensal;

	/**
	 * id_plano
	 */
	public Integer getPlanId() {
		return id_plano;
	}

	/**
	 * id_plano
	 * 
	 * @param planId
	 */
	public void setPlanId(Integer planId) {
		this.id_plano = planId;
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
	 * nome_plano
	 */
	public String getPlanName() {
		return nome_plano;
	}

	/**
	 * nome_plano
	 * 
	 * @param planName
	 */
	public void setPlanName(String planName) {
		this.nome_plano = planName;
	}

	/**
	 * valor_mensal
	 */
	public float getMonthlyValue() {
		return valor_mensal;
	}

	/**
	 * valor_mensal
	 * 
	 * @param monthlyValue
	 */
	public void setMonthlyValue(float monthlyValue) {
		this.valor_mensal = monthlyValue;
	}

}
