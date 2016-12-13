package com.sap.moc.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sap.moc.dao.IVendorLineDAO;
import com.sap.moc.entity.VendorLine;
import com.sap.moc.vo.QRCode;

@Repository
public class VendorLineDAOImpl extends GenericDAOImpl<VendorLine, Integer> implements IVendorLineDAO {

	@Override
	public VendorLine getLinebyQRCode(QRCode code) {

		String hql = "from VendorLine vl where vendor.id = :vendorId and lineNO = :lineNo ";
		Query query = currentSession().createQuery(hql);
		query.setParameter("vendorId", code.getVendorId());
		query.setParameter("lineNo", code.getVendorLineNo());

		@SuppressWarnings("unchecked")
		List<VendorLine> lines = query.list();
		if (lines.size() > 0) {
			return lines.get(0);
		} else {
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public int getMaxVendorLineNo(int vendorId) {
		int lineNO = 0;
		String hql = "select  max( vl.lineNO ) from VendorLine vl where vl.vendor.id = :vendorId";
		Query query = currentSession().createQuery(hql);
		query.setParameter("vendorId", vendorId);
		if(query.uniqueResult() != null){
			String number = query.uniqueResult().toString();
			try{
				lineNO = Integer.parseInt(number);
			}
			catch(NumberFormatException e){
				int no = 0;
				String hqlTemp = "from VendorLine vl where vl.vendor.id = :vendorId";
				Query queryTemp = currentSession().createQuery(hqlTemp);
				queryTemp.setParameter("vendorId", vendorId);
				List<VendorLine> lines = query.list();
				for(int i = 0; i < lines.size(); i++ ){	
					if(no<lines.get(i).getLineNO()){
						no = lines.get(i).getLineNO();
					}
				}
				lineNO = no;
			}
		}
		
		return lineNO;
	}

}
