package br.com.nocaute.model;

public class CityModel extends AbstractModel {
	private Integer id_cidade;
	private String cidade;
	private String estado;
	private String pais;
	
	/**
	 * id_cidade
	 */
	public Integer getId() {
		return id_cidade;
	}
	/**
	 * id_cidade
	 * @param code
	 */
	public void setId(Integer id_cidade) {
		
		this.id_cidade = id_cidade;
	}
	
	/**
	 * cidade
	 */
	public String getName() {
		return cidade;
	}
	/**
	 * cidade
	 * @param name
	 */
	public void setName(String name) {
		this.cidade = name;
	}
	
	/**
	 * estado
	 */
	public String getState() {
		return estado;
	}
	/**
	 * estado
	 * @param state
	 */
	public void setState(String state) {
		this.estado = state;
	}
	
	/**
	 * pais
	 */
	public String getCountry() {
		return pais;
	}
	/**
	 * pais
	 * @param country
	 */
	public void setCountry(String country) {
		this.pais = country;
	}
}
