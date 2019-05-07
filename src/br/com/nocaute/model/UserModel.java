package br.com.nocaute.model;

public class UserModel extends AbstractModel {
	private static final String REGISTER = "Cadastral";
	private static final String ENROLL = "Matricular";
	private static final String FINANCIAL = "Financeiro";
	private static final String COMPLETE = "Completo";

	private Integer id;
	private String usuario;
	private String senha;
	private String perfil;

	/**
	 * id
	 */
	public Integer getCode() {
		return id;
	}

	/**
	 * id
	 * 
	 * @param code
	 */
	public void setCode(Integer code) {
		this.id = code;
	}

	/**
	 * usuario
	 */
	public String getUser() {
		return usuario;
	}

	/**
	 * usuario
	 * 
	 * @param user
	 */
	public void setUser(String user) {
		this.usuario = user;
	}

	/**
	 * senha
	 */
	public String getPassword() {
		return senha;
	}

	/**
	 * senha
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.senha = password;
	}

	/**
	 * perfil
	 */
	public String getProfile() {
		return perfil;
	}

	/**
	 * perfil
	 * 
	 * @param profile
	 */
	public void setProfile(String profile) {
		this.perfil = profile;
	}

	public boolean hasProfileRegister() {
		return REGISTER.equals(getProfile());
	}

	public boolean hasProfileEnroll() {
		return ENROLL.equals(getProfile());
	}

	public boolean hasProfileFinancial() {
		return FINANCIAL.equals(getProfile());
	}

	public boolean hasProfileComplete() {
		return COMPLETE.equals(getProfile());
	}
}
