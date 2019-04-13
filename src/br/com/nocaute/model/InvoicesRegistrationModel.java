package br.com.nocaute.model;

import java.util.Date;
import java.util.List;

public class InvoicesRegistrationModel extends AbstractModel {

	private Integer codigo_matricula;
	private Date data_vencimento;
	private float valor;
	private Date data_pagamento;
	private Date data_cancelamento;
	
	// Auxiliares
	private boolean matricula_cancelada;
	private boolean destacar_valor;
	private List<String> descricao_desconto;

	/**
	 * codigo_matricula
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
	 * data_vencimento
	 */
	public Date getDueDate() {
		return data_vencimento;
	}

	/**
	 * data_vencimento
	 * 
	 * @param dueDate
	 */
	public void setDueDate(Date dueDate) {
		this.data_vencimento = dueDate;
	}

	/**
	 * valor
	 */
	public float getValue() {
		return valor;
	}

	/**
	 * valor
	 * 
	 * @param Value
	 */
	public void setValue(float value) {
		this.valor = value;
	}

	/**
	 * data_pagamento
	 */
	public Date getPaymentDate() {
		return data_pagamento;
	}

	/**
	 * data_pagamento
	 * 
	 * @param paymentDate
	 */
	public void setPaymentDate(Date paymentDate) {
		this.data_pagamento = paymentDate;
	}

	/**
	 * data_cancelamento
	 */
	public Date getCancellationDate() {
		return data_cancelamento;
	}

	/**
	 * data_cancelamento
	 * 
	 * @param cancellationDate
	 */
	public void setCancellationDate(Date cancellationDate) {
		this.data_cancelamento = cancellationDate;
	}

	/**
	 * matricula_cancelada
	 */
	public boolean isRegistrationFinished() {
		return matricula_cancelada;
	}

	/**
	 * matricula_cancelada
	 * 
	 * @param registrationFinish
	 */
	public void setRegistrationFinish(boolean registrationFinish) {
		this.matricula_cancelada = registrationFinish;
	}

	/**
	 * destacar_valor
	 */
	public boolean isHighlightValue() {
		return destacar_valor;
	}

	/**
	 * destacar_valor
	 * 
	 * @param highlightValue
	 */
	public void setHighlightValue(boolean highlightValue) {
		this.destacar_valor = highlightValue;
	}

	/**
	 * descricao_desconto
	 */
	public List<String> getDiscountDescription() {
		return descricao_desconto;
	}

	/**
	 * descricao_desconto
	 * 
	 * @param discountDescription
	 */
	public void setDiscountDescription(List<String> discountDescription) {
		this.descricao_desconto = discountDescription;
	}

}
