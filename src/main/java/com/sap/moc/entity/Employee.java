package com.sap.moc.entity;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.sap.moc.vo.Language;

@Entity
@Table(name = "EMPLOYEE")
public class Employee {


	@Id
	@Column(name = "ID", length = 8)
	private String id;

	@Column(name = "LAST_NAME", length = 40)
	private String lastName;

	@Column(name = "FIRST_NAME", length = 40)
	private String firstName;

	@Column(name = "EMAIL", length = 60)
	private String email;

	@Column(name = "TEL_NO", length = 30)
	private String telNo;

	@Column(name = "WECHAT_ID", length = 40)
	private String wechatId;

	@Column(name = "STATUS", length = 2)
	private String status;

	@Column(name = "LOCATION", length = 45)
	private String location;

	@Column(name = "PERSONAL_AREA", length = 40)
	private String personalArea;

	@Column(name = "PERSONAL_SUBAREA", length = 40)
	private String personalSubarea;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, nullable = true, name = "LANGUAGE")
	private Language language = Language.ENGLISH;

	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID")
	private Department department;

	@Column(name = "COMMENT", length = 100)
	private String comment;

	@Transient
	private String formattedName;

	public String getFormattedName() {
		return formattedName;
	}

	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPersonalArea() {
		return personalArea;
	}

	public void setPersonalArea(String personalArea) {
		this.personalArea = personalArea;
	}

	public String getPersonalSubarea() {
		return personalSubarea;
	}

	public void setPersonalSubarea(String personalSubarea) {
		this.personalSubarea = personalSubarea;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Locale getLanguageKey() {
		switch (this.language) {
		case CHINESE:
			return Locale.CHINESE;
		case ENGLISH:
			return Locale.ENGLISH;
		default:
			return Locale.CHINESE;

		}
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
