package br.com.nocaute.pojos;

import java.util.Date;

public class RegistrationModality {
	private Integer id;
	private Integer registration_code;
	private Modality modality;
	private Graduation graduation;
	private Plan plan;
	private Date startDate;
	private Date finishDate;
	private boolean deleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRegistrationCode() {
		return registration_code;
	}

	public void setRegistrationCode(Integer registration_code) {
		this.registration_code = registration_code;
	}

	public Modality getModality() {
		return modality;
	}

	public void setModality(Modality modality) {
		this.modality = modality;
	}

	public Graduation getGraduation() {
		return graduation;
	}

	public void setGraduation(Graduation graduation) {
		this.graduation = graduation;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
