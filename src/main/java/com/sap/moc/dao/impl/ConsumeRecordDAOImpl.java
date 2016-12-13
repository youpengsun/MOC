package com.sap.moc.dao.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.sap.moc.dao.IConsumeRecordDAO;
import com.sap.moc.entity.ConsumeRecord;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.TrimUtil;
import com.sap.moc.vo.ConsumeInquiryCriteria;
import com.sap.moc.vo.QueryTime;

@Repository
public class ConsumeRecordDAOImpl extends GenericDAOImpl<ConsumeRecord, String> implements IConsumeRecordDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumeRecord> getTodayConsumeRecord(String employeeId, String status, String category) {

		java.util.Date now = CommonUtil.getCurrentTime();

		StringBuilder hql = new StringBuilder();
		hql.append("select rcd from ConsumeRecord rcd where Date(rcd.time) =:date ");

		if (TrimUtil.trimUtil(employeeId)) {
			hql.append("and rcd.employee.id = :id ");
		}
		if (TrimUtil.trimUtil(status)) {
			hql.append("and rcd.status = :status ");
		}
		if (TrimUtil.trimUtil(category)) {
			hql.append("and rcd.category = :category ");
		}
		Query query = currentSession().createQuery(hql.toString());

		if (TrimUtil.trimUtil(employeeId)) {
			query.setParameter("id", employeeId.trim());
		}
		if (TrimUtil.trimUtil(status)) {
			query.setParameter("status", status.trim());
		}
		if (TrimUtil.trimUtil(category)) {
			query.setParameter("category", category.trim());
		}
		query.setDate("date", now);

		return (List<ConsumeRecord>) query.list();

	}

	@Override
	public List<ConsumeRecord> getConsumeRecordsByVendor(int vendorID, QueryTime time) {

		StringBuilder hql = new StringBuilder();
		hql.append("from ConsumeRecord c where c.vendorLine.vendor.id = :vendorid and c.status = :status ");

		if (time.getYear() != 0) {
			hql.append(" and year(time) = ");
			hql.append(time.getYear());
			if (time.getMonth() != 0) {
				hql.append(" and month(time) = ");
				hql.append(time.getMonth());

				if (time.getDay() != 0) {
					hql.append(" and day(time) = ");
					hql.append(time.getDay());
				}
			}
		}
		System.out.println(hql.toString());
		Query query = currentSession().createQuery(hql.toString());
		query.setParameter("vendorid", vendorID);
		query.setParameter("status", ConstantUtil.CONSUME_STATUS_SUCCESS);
		@SuppressWarnings("unchecked")
		List<ConsumeRecord> records = query.list();
		return records;
	}

	@Override
	public List<ConsumeRecord> getConsumeRecordsByVendor(int vendorID, Date beginDate, Date endDate) {

		// category : lunch/supper, depending on the vendor type --- No Need
		StringBuilder hql = new StringBuilder();
		hql.append(
				"from ConsumeRecord c where c.vendorLine.vendor.id = :vendorid and Date(c.time) between :beginDate and :endDate");
		hql.append(" and c.status = :status ");
		Query query = currentSession().createQuery(hql.toString());
		query.setParameter("vendorid", vendorID);
		query.setParameter("beginDate", beginDate);
		query.setParameter("endDate", endDate);
		query.setParameter("status", ConstantUtil.CONSUME_STATUS_SUCCESS);

		@SuppressWarnings("unchecked")
		List<ConsumeRecord> records = query.list();
		return records;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumeRecord> getRecordsByVendorPeriod(int vendor_line_id, String status, Timestamp from,
			Timestamp to) {
		Criteria crt = currentSession().createCriteria(ConsumeRecord.class, "cr");
		crt.createAlias("cr.employee", "employee"); // Lazy init
		crt.createAlias("cr.vendorLine", "line");
		crt.add(Restrictions.ge("cr.time", from));
		crt.add(Restrictions.le("cr.time", to));
		crt.add(Restrictions.eq("cr.status", status.trim()));
		crt.add(Restrictions.eq("line.id", vendor_line_id));
		List<ConsumeRecord> res = crt.list();
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumeRecord> findByInquiryCriteria(ConsumeInquiryCriteria criteria) throws ParseException {

		// Initialize the time as 00:00:00 for Begda and 23:59:59 for Endda
		criteria.initializeBegdaTime();
		criteria.initializeEnddaTime();

		Criteria crt = currentSession().createCriteria(ConsumeRecord.class, "cr");
		crt.createAlias("cr.employee", "employee");
		crt.createAlias("employee.department", "department");
		crt.createAlias("cr.vendorLine", "vendorLine");
		crt.createAlias("vendorLine.vendor", "vendor");

		crt.add(Restrictions.ge("cr.time", criteria.getBegda()));
		crt.add(Restrictions.le("cr.time", criteria.getEndda()));

		if (TrimUtil.trimUtil(criteria.getCategoryKey())) {
			crt.add(Restrictions.eq("cr.category", criteria.getCategoryKey().trim()));
		}
		if (TrimUtil.trimUtil(criteria.getCosterCenterID())) {
			crt.add(Restrictions.eq("department.id", criteria.getCosterCenterID().trim()));
		}
		if (TrimUtil.trimUtil(criteria.getVendorID())) {
			crt.add(Restrictions.eq("vendor.id", Integer.parseInt(criteria.getVendorID().trim())));
		}
		if (TrimUtil.trimUtil(criteria.getEmployeeID())) {
			crt.add(Restrictions.like("employee.id", "%" + criteria.getEmployeeID().trim() + "%"));
		}
		if (TrimUtil.trimUtil(criteria.getFirstName())) {
			crt.add(Restrictions.like("employee.firstName", "%" + criteria.getFirstName().trim() + "%"));
		}
		if (TrimUtil.trimUtil(criteria.getLastName())) {
			crt.add(Restrictions.like("employee.lastName", "%" + criteria.getLastName().trim() + "%"));
		}
		if (TrimUtil.trimUtil(criteria.getStatusKey())) {
			crt.add(Restrictions.eq("cr.status", criteria.getStatusKey().trim()));
		}
		if (TrimUtil.trimUtil(criteria.getTransactionCode())) {
			crt.add(Restrictions.like("cr.transactionCode", "%" + criteria.getTransactionCode() + "%"));
		}

		return crt.list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List getAnalysisResource(QueryTime begmo, QueryTime endmo) throws ParseException {

		Calendar calendar = CommonUtil.getCurrentCalendar();
		calendar.set(begmo.getYear(), begmo.getMonth() - 1, 1);
		Date begda = new Date(calendar.getTime().getTime());
		calendar.set(endmo.getYear(), endmo.getMonth() - 1,
				CommonUtil.getLastDayOfMonth(endmo.getYear(), endmo.getMonth()));
		Date endda = new Date(calendar.getTime().getTime());

		StringBuilder hql = new StringBuilder();
		hql.append(
				"select cr.vendorLine.vendor.id,cr.vendorLine.vendor.name, year(time), month(time),count(cr) from ConsumeRecord cr ");
		hql.append(" where cr.status = :status and date(cr.time) between :begda and :endda ");
		hql.append(" group by cr.vendorLine.vendor.id, year(time),month(time)");

		Query query = currentSession().createQuery(hql.toString());
		query.setParameter("status", ConstantUtil.CONSUME_STATUS_SUCCESS);
		query.setParameter("begda", begda);
		query.setParameter("endda", endda);
		return query.list();
	}

	@Override
	public int getVendorLineCountByDate(int vendorLineID, java.util.Date begda, java.util.Date endda, String status) {

		String HQL = "SELECT COUNT(*) FROM ConsumeRecord cr WHERE cr.vendorLine.id = :vendorLineID AND status = :status"
				+ " AND time >= :begda AND time <= :endda";
		Query query = currentSession().createQuery(HQL);

		query.setParameter("vendorLineID", vendorLineID);
		query.setParameter("status", status.trim());
		query.setParameter("begda", begda);
		query.setParameter("endda", endda);

		int count = ((Long) query.uniqueResult()).intValue();
		return count;

	}

}
