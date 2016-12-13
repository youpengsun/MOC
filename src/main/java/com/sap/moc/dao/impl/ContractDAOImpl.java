package com.sap.moc.dao.impl;

import java.sql.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sap.moc.dao.IContractDAO;
import com.sap.moc.entity.Contract;

@Repository
public class ContractDAOImpl extends GenericDAOImpl<Contract, Integer> implements IContractDAO {

	@Override
	public List<Contract> getCurrentContracts(int vendorId, Date date) {

		String hql = "from Contract c where c.vendor.id = :vendorid and c.beginDate <= :currentDate and c.endDate >= :currentDate";

		Query query = currentSession().createQuery(hql);
		query.setParameter("vendorid", vendorId);
		query.setParameter("currentDate", date);

		@SuppressWarnings("unchecked")
		List<Contract> contracts = query.list();
		return contracts;
	}

}
