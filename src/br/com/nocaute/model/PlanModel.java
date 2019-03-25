package br.com.nocaute.model;

import java.math.BigDecimal;

public class PlanModel extends AbstractModel {

	private Integer id_plano;
	private Integer id_modalidade;
	private String nome_plano;
	private BigDecimal valor_mensal;

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
	public BigDecimal getMonthlyValue() {
		return valor_mensal;
	}

	/**
	 * valor_mensal
	 * 
	 * @param monthlyValue
	 */
	public void setMonthlyValue(BigDecimal monthlyValue) {
		this.valor_mensal = monthlyValue;
	}

}
