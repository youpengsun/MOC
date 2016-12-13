package com.sap.moc.dao;


import java.sql.Date;
import java.util.List;

import com.sap.moc.entity.Contract;

public interface IContractDAO extends IGenericDAO<Contract, Integer >{
	public List<Contract> getCurrentContracts(int vendorId,Date date);
}
