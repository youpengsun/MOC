package com.sap.moc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", length = 8)
	private int id;
	
	@Column(name = "USERNAME", length = 20, unique = true)
	private String username;

	@Column(name = "SALT", length = 44)
	private String salt;

	@Column(name = "PASSWORD", length = 44)
	private String password;
	
	@Column(name = "USER_GROUP", length = 10)
	private String userGroup;
	
	@Column(name ="FIRST_NAME", length = 40)
	private String firstName;
	
	@Column(name ="LAST_NAME", length = 40)
	private String lastName;
	
	@Column(name ="MOBILE", length = 20)
	private String mobile;
	
	@Column(name = "EMAIL", length = 60)
	private String email;

	@Column(name = "COMMENT", length = 100)
	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserGroup() {
		return userGroup;
	}
	
	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
}
