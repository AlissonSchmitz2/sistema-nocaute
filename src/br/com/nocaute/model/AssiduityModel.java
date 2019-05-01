package br.com.nocaute.model;

import java.sql.Timestamp;

public class AssiduityModel extends AbstractModel{
	private int codigo_assiduidade;
	private int codigo_matricula;
	private Timestamp data_entrada;
	
	/**
	 * @return the codigo_assiduidade
	 */
	public int getAssiduityCode() {
		return codigo_assiduidade;
	}
	
	/**
	 * @param codigo_assiduidade the codigo_assiduidade to set
	 */
	public void setAssiduityCode(int assiduityCode) {
		this.codigo_assiduidade = assiduityCode;
	}
	
	/**
	 * @return the codigo_matricula
	 */
	public int getRegistrationCode() {
		return codigo_matricula;
	}
	
	/**
	 * @param codigo_matricula the codigo_matricula to set
	 */
	public void setRegistrationCode(int registrationCode) {
		this.codigo_matricula = registrationCode;
	}
	
	/**
	 * @return the data_entrada
	 */
	public Timestamp getInputDate() {
		return data_entrada;
	}
	
	/**
	 * @param data_entrada the data_entrada to set
	 */
	public void setInputDate(Timestamp inputDate) {
		this.data_entrada = inputDate;
	}
	
	
}
