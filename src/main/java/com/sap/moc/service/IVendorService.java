package com.sap.moc.service;

import java.util.List;
import java.util.Map;

import com.sap.moc.entity.Contract;
import com.sap.moc.entity.District;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.vo.Pager;
import com.sap.moc.vo.QRCode;
import com.sap.moc.vo.VendorWithContract;
import com.sap.moc.vo.Ordering;


public interface IVendorService {

	
	//-------------------------- for Vendor------------------------------------
	public Pager<Vendor> getVendorByPage(Vendor ve, Pager<Vendor> page , Ordering orders);
	
	//for create , update, delete
	public boolean saveVendor(Vendor ve);
	public boolean updateVendor(Vendor ve);
	public boolean deleteVendor(Vendor ve);
	
	// for query
	public List<Vendor> getAllVendor();
	public Vendor getVendorByKey(int id);
	public List<Vendor> getActiveVendors();
	public List<Vendor> getInactiveVendors();
	public List<Vendor> getVendorsWithStatus();
	public Pager<Vendor> getActiveVendorPager(Pager<Vendor> pager);
	
	// for fuzzy query
	public List<Vendor> getVendor(Vendor ve);
	
	
	// for save or update vendor with contract
	public Map<String, String> saveVendorWithContract(VendorWithContract vendorwithContract);
	public Map<String, String> updateVendorWithContract(VendorWithContract vendorwithContract);

		
	
	//-------------------------- for Vendor Line----------------------------------
	public List<VendorLine> getVendorLine(int vendorId);
	public boolean saveVendorLine(VendorLine vl);
	public boolean saveVendorLineWOLineNo(VendorLine vl);
	public boolean updateVendorLine(VendorLine vl);
	public boolean deleteVendorLine(VendorLine vl);
	public VendorLine getVendorLinebyQRCode(QRCode code);

	//-------------------------- for contract--------------------------------------
	public Contract getContract(int vendorId);
	public boolean saveContract(Contract contract);
	public boolean updateContract(Contract contract);
	public boolean deleteContract(Contract contract);
	
	//-----------------------------for district------------------------------------
	public List<District> getAllDistrict();
}
