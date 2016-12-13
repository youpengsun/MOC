package com.sap.moc.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 
@Entity
@Table(name = "DEPARTMENT")
@JsonIgnoreProperties(value="employees")
public class Department {

	@Id 
	@Column(name="ID", length=11)
	private String id;
	
	@Column(name="NAME", length=40)
	private String name;
	
	@OneToMany(mappedBy = "department")
	private Set<Employee> employees;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	public Department() {
		super();
	}

	public Department(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
//	public String getTrimId() {
//		return this.id.replaceAll("^0+", "");
//	}

}
