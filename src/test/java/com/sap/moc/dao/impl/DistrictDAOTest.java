package com.sap.moc.dao.impl;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.sap.moc.dao.IDistrictDAO;
import com.sap.moc.entity.District;
import com.sap.moc.test.BaseJunit4Test;

public class DistrictDAOTest extends BaseJunit4Test {
	@Autowired
	private IDistrictDAO dao;
	
	
	@Test
	public void testCreate()
	{
		District dt = new District();
		dt.setId("CT");
		dt.setDescription("长泰广场");
		dao.create(dt);
	}
	@Test
	public void testGetAll() {
		List<District> districts = dao.getAll();
		for(District dt: districts){
			if (dt != null) {
				System.out.println(dt.getId() + " " + dt.getDescription());
			}
		}
	}
}
