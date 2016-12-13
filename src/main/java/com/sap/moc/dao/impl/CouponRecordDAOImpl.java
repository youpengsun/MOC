package com.sap.moc.dao.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sap.moc.dao.ICouponRecordDAO;
import com.sap.moc.entity.CouponRecord;
import com.sap.moc.utils.TrimUtil;
import com.sap.moc.vo.CouponInquiryCriteria;

@Repository
public class CouponRecordDAOImpl extends GenericDAOImpl<CouponRecord, String> implements ICouponRecordDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<CouponRecord> findByInquiryCriteria(CouponInquiryCriteria criteria) throws ParseException {

		criteria.initializeBegdaTime();
		criteria.initializeEnddaTime();
		
		Criteria crt= currentSession().createCriteria(CouponRecord.class, "cpr");
		crt.createAlias("cpr.employee", "employee");
		crt.createAlias("cpr.department", "department");
		
		crt.add(Restrictions.ge("cpr.registerDate", criteria.getBegda()));
		crt.add(Restrictions.le("cpr.registerDate", criteria.getEndda()));
		
		if (TrimUtil.trimUtil(criteria.getEmployeeID())) {
			crt.add(Restrictions.like("employee.id", "%" + criteria.getEmployeeID().trim() + "%"));
		}
		
		if (TrimUtil.trimUtil(criteria.getCosterCenterID())) {
			crt.add(Restrictions.eq("department.id", criteria.getCosterCenterID().trim()));
		}
		
		if (TrimUtil.trimUtil(criteria.getFirstName())) {
			crt.add(Restrictions.like("employee.firstName", "%" + criteria.getFirstName().trim() + "%"));
		}
		
		if (TrimUtil.trimUtil(criteria.getLastName())) {
			crt.add(Restrictions.like("employee.lastName", "%" + criteria.getLastName().trim() + "%"));
		}
		
		if (TrimUtil.trimUtil(criteria.getType())){
			crt.add(Restrictions.eq("cpr.type", criteria.getType().trim()));
		}
		
		List<CouponRecord> result = new ArrayList<>();
		result = crt.list();
		return result;
	}
	
}
