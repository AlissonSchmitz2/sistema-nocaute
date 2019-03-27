package br.com.nocaute.model;

import java.util.Date;
import java.util.List;

import br.com.nocaute.pojos.RegistrationModality;

public class RegistrationModel extends AbstractModel {

	private Integer codigo_matricula;
	private Integer codigo_aluno;
	private Date data_matricula;
	private Integer dia_vencimento;
	private Date data_encerramento;

	/**
	 * Relacionamentos
	 */
	private StudentModel student;
	private List<RegistrationModality> modalities;
	
	/**
	 * @return StudentModel
	 */
	public StudentModel getStudent() {
		return student;
	}
	/**
	 * @param StudentModel student
	 */
	public void setStudent(StudentModel student) {
		this.student = student;
	}

	/**
	 * @return List<RegistrationModality>
	 */
	public List<RegistrationModality> getModalities() {
		return modalities;
	}
	/**
	 * @param List<RegistrationModality> modalities
	 */
	public void setModalities(List<RegistrationModality> modalities) {
		this.modalities = modalities;
	}
	
	/**
	 * Outros atributos
	 */
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
	 * codigo_aluno
	 */
	public Integer getStudentCode() {
		return codigo_aluno;
	}

	/**
	 * codigo_aluno
	 * 
	 * @param studentCode
	 */
	public void setStudentCode(Integer studentCode) {
		this.codigo_aluno = studentCode;
	}

	/**
	 * data_matricula
	 */
	public Date getRegistrationDate() {
		return data_matricula;
	}

	/**
	 * data_matricula
	 * 
	 * @param registrationDate
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.data_matricula = registrationDate;
	}

	/**
	 * dia_vencimento
	 */
	public Integer getExpirationDay() {
		return dia_vencimento;
	}

	/**
	 * dia_vencimento
	 * 
	 * @param expirationDay
	 */
	public void setExpirationDay(Integer expirationDay) {
		this.dia_vencimento = expirationDay;
	}

	/**
	 * data_encerramento
	 */
	public Date getClosingDate() {
		return data_encerramento;
	}

	/**
	 * data_encerramento
	 * 
	 * @param closingDate
	 */
	public void setClosingDate(Date closingDate) {
		this.data_encerramento = closingDate;
	}

}
