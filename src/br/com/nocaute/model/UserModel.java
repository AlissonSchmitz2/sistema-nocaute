package br.com.nocaute.model;

public class UserModel extends AbstractModel{
	
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
	 * @param profile
	 */
	public void setProfile(String profile) {
		this.perfil = profile;
	}
}
