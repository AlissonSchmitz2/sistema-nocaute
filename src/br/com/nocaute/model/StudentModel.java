package br.com.nocaute.model;

import java.util.Date;

public class StudentModel extends AbstractModel {
	private Integer codigo_aluno;
	private String aluno;
	private Date data_nascimento;
	private char sexo;
	private String telefone;
	private String celular;
	private String email;
	private String observacao;
	private String endereco;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String estado;
	private String pais;
	private String cep;
	
	/**
	 * codigo_aluno
	 */
	public Integer getCode() {
		return codigo_aluno;
	}
	/**
	 * codigo_aluno
	 * @param code
	 */
	public void setCode(Integer code) {
		this.codigo_aluno = code;
	}
	
	/**
	 * aluno
	 */
	public String getName() {
		return aluno;
	}
	/**
	 * aluno
	 * @param name
	 */
	public void setName(String name) {
		this.aluno = name;
	}
	
	/**
	 * data_nascimento
	 */
	public Date getBirthDate() {
		return data_nascimento;
	}
	/**
	 * data_nascimento
	 * @param birthDate
	 */
	public void setBirthDate(Date birthDate) {
		this.data_nascimento = birthDate;
	}
	
	/**
	 * sexo
	 */
	public char getGenre() {
		return sexo;
	}
	/**
	 * sexo
	 * @param genre
	 */
	public void setGenre(char genre) {
		this.sexo = genre;
	}
	
	/**
	 * telefone
	 */
	public String getTelephone() {
		return telefone;
	}
	/**
	 * telefone
	 * @param telephone
	 */
	public void setTelephone(String telephone) {
		this.telefone = telephone;
	}
	
	/**
	 * celular
	 */
	public String getMobilePhone() {
		return celular;
	}
	/**
	 * celular
	 * @param mobilePhone
	 */
	public void setMobilePhone(String mobilePhone) {
		this.celular = mobilePhone;
	}
	
	/**
	 * email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * observacao
	 */
	public String getObservation() {
		return observacao;
	}
	/**
	 * observacao
	 * @param observation
	 */
	public void setObservation(String observation) {
		this.observacao = observation;
	}
	
	/**
	 * endereco
	 */
	public String getAddress() {
		return endereco;
	}
	/**
	 * endereco
	 * @param address
	 */
	public void setAddress(String address) {
		this.endereco = address;
	}
	
	/**
	 * numero
	 */
	public String getNumber() {
		return numero;
	}
	/**
	 * numero
	 * @param number
	 */
	public void setNumber(String number) {
		this.numero = number;
	}
	
	/**
	 * complemento
	 */
	public String getAddressComplement() {
		return complemento;
	}
	/**
	 * complemento
	 * @param complement
	 */
	public void setAddressComplement(String complement) {
		this.complemento = complement;
	}
	
	/**
	 * bairro
	 */
	public String getNeighborhood() {
		return bairro;
	}
	/**
	 * bairro
	 * @param neighborhood
	 */
	public void setNeighborhood(String neighborhood) {
		this.bairro = neighborhood;
	}
	
	/**
	 * cidade
	 */
	public String getCity() {
		return cidade;
	}
	/**
	 * cidade
	 * @param city
	 */
	public void setCity(String city) {
		this.cidade = city;
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
	
	/**
	 * cep
	 */
	public String getPostalCode() {
		return cep;
	}
	/**
	 * cep
	 * @param postalCode
	 */
	public void setPostalCode(String postalCode) {
		this.cep = postalCode;
	}
}
