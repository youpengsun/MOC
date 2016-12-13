package com.sap.moc.entity;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "DISTRICT")
@JsonIgnoreProperties("vendors")
public class District {
	
	@Id
	@Column(name="ID",length = 2)
	private String id;


	@Column(name="DESCRIPTION", length=60)
	private String description;

	@OneToMany(mappedBy = "businessDistrict")
	private Set<Vendor> vendors;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Set<Vendor> getVendors() {
		return vendors;
	}


	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}
	

	
}
