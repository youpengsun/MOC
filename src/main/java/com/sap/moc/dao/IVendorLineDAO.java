package com.sap.moc.dao;

import com.sap.moc.entity.VendorLine;
import com.sap.moc.vo.QRCode;

public interface IVendorLineDAO extends IGenericDAO<VendorLine, Integer>{
	public VendorLine getLinebyQRCode(QRCode code);
	public int getMaxVendorLineNo(int vendorId);
}
