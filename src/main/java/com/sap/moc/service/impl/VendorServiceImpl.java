/**
 * 
 */
package com.sap.moc.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.moc.dao.IContractDAO;
import com.sap.moc.dao.IDistrictDAO;
import com.sap.moc.dao.IVendorDAO;
import com.sap.moc.dao.IVendorLineDAO;
import com.sap.moc.entity.Contract;
import com.sap.moc.entity.District;
import com.sap.moc.entity.Vendor;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.service.IVendorService;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Pager;
import com.sap.moc.vo.QRCode;
import com.sap.moc.vo.VendorWithContract;

/**
 * @author I074184
 *
 */
@Service
public class VendorServiceImpl implements IVendorService {

	@Autowired
	private IVendorDAO vendorDAO;
	@Autowired
	private IVendorLineDAO vendorLineDAO;
	@Autowired
	private IContractDAO contractDAO;
	@Autowired
	private IDistrictDAO districtDAO;

	// -----------------------------for
	// vendor----------------------------------------------------
	/*
	 * @fuzzy query by page using the information in ve, and orders
	 */
	@Override
	public Pager<Vendor> getVendorByPage(Vendor ve, Pager<Vendor> page, Ordering orders) {

		// get vendor by page
		return vendorDAO.queryVendorByPage(ve, page, orders);
	}

	/*
	 * @get all the vendor
	 */
	@Override
	public List<Vendor> getAllVendor() {
		return vendorDAO.getAll();
	}

	/*
	 * @fuzzy query using the information in ve
	 */
	@Override
	public List<Vendor> getVendor(Vendor ve) {
		return vendorDAO.queryVendor(ve);
	}

	/*
	 * @get all active vendor
	 */
	@Override
	public List<Vendor> getActiveVendors() {
		List<Vendor> vendorList = vendorDAO.getActiveVendors();
		return vendorList;
	}
	
	@Override
	public List<Vendor> getInactiveVendors() {
		List<Vendor> vendorList = vendorDAO.getInactiveVendors();
		return vendorList;
	}


	@Override
	public List<Vendor> getVendorsWithStatus() {
		List<Vendor> list = getActiveVendors();
		for (Vendor vendor : list) {
			vendor.setActiveStatus("Active");
		}
		List<Vendor> inactiveList = getInactiveVendors();
		for (Vendor vendor : inactiveList) {
			vendor.setActiveStatus("Inactive");
			list.add(vendor);
		}
		
		return list;
	}

	/*
	 * @get all active vendor in pager
	 */

	@Override
	public Pager<Vendor> getActiveVendorPager(Pager<Vendor> pager) {
		if (pager.getPageCount() == 0) {
			pager.setPageCount(20);
		}
		if (pager.getCurrentPage() <= 0) {
			pager.setCurrentPage(1);
		}
		List<Vendor> list = getActiveVendors();
		int count = list.size();
		if (count == 0) {
			return pager;
		} else {
			int totalPageCount = count % pager.getPageCount() + 1;
			pager.setPageCount(totalPageCount);
			pager.setTotalCount(count);
			int begIndex = pager.getPageLine() * (pager.getCurrentPage() - 1) + 1;
			int endIndex = pager.getPageLine() * pager.getCurrentPage();
			List<Vendor> pageList = list.subList(begIndex, endIndex);
			pager.setEntryList(pageList);
		}
		return pager;
	}

	// for Vendor C, R, U, D
	@Override
	public boolean saveVendor(Vendor ve) {
		return vendorDAO.create(ve);
	}

	@Override
	public Vendor getVendorByKey(int id) {
		return vendorDAO.findByKey(id);
	}

	@Override
	public boolean updateVendor(Vendor ve) {
		return vendorDAO.update(ve);
	}

	@Override
	public boolean deleteVendor(Vendor ve) {
		return vendorDAO.delete(ve);
	}
    
	private Map<String, String> validateVendorWithContract(VendorWithContract vendorWithContract, String operation){
		Map<String, String> map = new HashMap<String, String>();
		if("update".equals(operation)){
			if(vendorDAO.findByKey(vendorWithContract.getId()) == null ){
				map.put("error", "Invalid vendor ID");
				return map;
			}
			else if(contractDAO.findByKey(vendorWithContract.getContractId()) == null){
				map.put("error", "Invalid contract ID");
				return map;
			}
		}
		if(districtDAO.findByKey(vendorWithContract.getBusinessDistrictId()) == null){
			map.put("error", "Invalid business district");
			return map;
		}
		return map;
	}
	
	@Override
	public Map<String, String> saveVendorWithContract(VendorWithContract vendorWithContract) {
		Map<String, String> map = new HashMap<String, String>();
		map = validateVendorWithContract(vendorWithContract, "save");
		if (map.size() == 0){
			Vendor ve = new Vendor();
			ve.setAddress(vendorWithContract.getAddress());
			ve.setBusinessDistrict(districtDAO.findByKey(vendorWithContract.getBusinessDistrictId()));
			ve.setContactEmail(vendorWithContract.getContactEmail());
			ve.setContactName(vendorWithContract.getContactName());
			ve.setContactTelNO(vendorWithContract.getContactTelNO());
			ve.setDianpingID(vendorWithContract.getDianpingID());
			ve.setName(vendorWithContract.getName());
			ve.setReportPeriod(vendorWithContract.getReportPeriod());
			ve.setVendorType(vendorWithContract.getVendorType());
			ve.setWechatID(vendorWithContract.getWechatID());
			ve.setPromotion(vendorWithContract.getPromotion());
			
			Contract ct = new Contract();
			ct.setStatus(vendorWithContract.getStatus());
			ct.setBeginDate(vendorWithContract.getBeginDate());
			ct.setEndDate(vendorWithContract.getEndDate());
			ct.setComment(vendorWithContract.getComment());
			ct.setContract_no(vendorWithContract.getContract_no());
			
			if(vendorDAO.create(ve)){
				ct.setVendor(ve);
				if (contractDAO.create(ct)){
					map.put("result", "true");
					map.put("vendorId", String.valueOf(ve.getId()));
					map.put("contractId", String.valueOf(ct.getId()));
						
				}
				else{
					map.put("result", "false");
					map.put("contract_result", "false");
					map.put("error", "Fail to create Contract");
					}
			}
			else{
				map.put("result", "false");
				map.put("vendor_result", "false");
				map.put("error", "Fail to create Vendor");

			}
		}else{
			map.put("result", "false");
		}
		
		
		return map;
	}
	
	
	@Override
	public Map<String, String> updateVendorWithContract(VendorWithContract vendorWithContract) {
		Map<String, String> map = new HashMap<String, String>();
		map = validateVendorWithContract(vendorWithContract, "update");
		if(map.size() == 0){
			int id = vendorWithContract.getId();
			Vendor ve = vendorDAO.findByKey(id);
			ve.setAddress(vendorWithContract.getAddress());
			ve.setBusinessDistrict(districtDAO.findByKey(vendorWithContract.getBusinessDistrictId()));
			ve.setContactEmail(vendorWithContract.getContactEmail());
			ve.setContactName(vendorWithContract.getContactName());
			ve.setContactTelNO(vendorWithContract.getContactTelNO());
			ve.setDianpingID(vendorWithContract.getDianpingID());
			ve.setName(vendorWithContract.getName());
			ve.setReportPeriod(vendorWithContract.getReportPeriod());
			ve.setVendorType(vendorWithContract.getVendorType());
			ve.setWechatID(vendorWithContract.getWechatID());
			ve.setPromotion(vendorWithContract.getPromotion());
			
			Contract ct = contractDAO.findByKey(vendorWithContract.getContractId());
			ct.setBeginDate(vendorWithContract.getBeginDate());
			ct.setEndDate(vendorWithContract.getEndDate());
			ct.setComment(vendorWithContract.getComment());
			
			if(vendorDAO.update(ve)){
				if (contractDAO.update(ct)){
					map.put("result", "true");	
				}else{
					map.put("result", "false");
					map.put("contract_result", "false");
					map.put("error", "Fail to update Contract");
				}
			}else{
				map.put("result", "false");
				map.put("vendor_result", "false");
				map.put("error", "Fail to update vendor");
			}
		
		}else{
			map.put("result", "false");
		}
		return map;
	}
	
	
	// -----------------------------for vendor
	// line----------------------------------------------------

	@Override
	public List<VendorLine> getVendorLine(int vendorId) {
		return vendorDAO.findByKey(vendorId).getVendorLineList();
	}

	@Override
	public boolean saveVendorLine(VendorLine vl) {
		return vendorLineDAO.create(vl);
	}
	
	@Override
	public boolean saveVendorLineWOLineNo(VendorLine vl) {
		int lineNO = vendorLineDAO.getMaxVendorLineNo(vl.getVendor().getId());
		lineNO = lineNO + 1;
		vl.setLineNO(lineNO);
		return vendorLineDAO.create(vl);
	}
	
	@Override
	public boolean updateVendorLine(VendorLine vl) {
		return vendorLineDAO.update(vl);
	}

	@Override
	public boolean deleteVendorLine(VendorLine vl) {
		return vendorLineDAO.delete(vl);
	}

	@Override
	public VendorLine getVendorLinebyQRCode(QRCode code) {
		return vendorLineDAO.getLinebyQRCode(code);
	}
	


	// -----------------------------for
	// contract------------------------------------------------------

	@Override
	public Contract getContract(int vendorId) {
		
		List<Contract> ctList = getVendorByKey(vendorId).getContractList();
		Date now = CommonUtil.getCurrentTime();
		Contract returnCt = null;
		if (ctList == null || ctList.size() == 0) {
			return null;
		} else {

			for (Contract ct : ctList) {
				if (ct.getEndDate().after(now)) {
					returnCt = ct;
				}

			};
		
			if (returnCt == null){
				returnCt = ctList.get(0);
			}
			return returnCt;
		}
	}

	@Override
	public boolean saveContract(Contract contract) {
		return contractDAO.create(contract);
	}

	@Override
	public boolean updateContract(Contract contract) {
		return contractDAO.update(contract);
	}

	@Override
	public boolean deleteContract(Contract contract) {
		return contractDAO.delete(contract);
	}

	// -------------------------------for
	// district--------------------------------------------------
	@Override
	public List<District> getAllDistrict() {

		return districtDAO.getAll();
	}





}
