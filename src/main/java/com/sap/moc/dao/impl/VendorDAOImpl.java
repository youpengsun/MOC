package com.sap.moc.dao.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sap.moc.dao.IVendorDAO;
import com.sap.moc.entity.Vendor;
import com.sap.moc.utils.CommonUtil;
import com.sap.moc.utils.ConstantUtil;
import com.sap.moc.utils.TrimUtil;
import com.sap.moc.vo.Ordering;
import com.sap.moc.vo.Ordering.sort;
import com.sap.moc.vo.Pager;

@Repository
public class VendorDAOImpl extends GenericDAOImpl<Vendor, Integer> implements IVendorDAO {

	@Override
	public Pager<Vendor> queryVendorByPage(Vendor ve, Pager<Vendor> page, Ordering orders) {
		return searchByCriteria(page.getCurrentPage(), page.getPageLine(), generateCriterions(ve),
				generateOrder(orders));
	}

	@Override
	public List<Vendor> queryVendor(Vendor ve) {
		return searchByCriteria(generateCriterions(ve), new ArrayList<org.hibernate.criterion.Order>());
	}

	@Override
	public List<Criterion> generateCriterions(Vendor ve) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		if (ve.getId() != 0) {
			criterions.add(Restrictions.like("id", "%" + ve.getId() + "%"));
		}
		if (TrimUtil.trimUtil(ve.getName())) {
			criterions.add(Restrictions.like("name", "%" + ve.getName().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ve.getContactName())) {
			criterions.add(Restrictions.like("contractName", "%" + ve.getContactName().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ve.getContactTelNO())) {
			criterions.add(Restrictions.like("contractTelNo", "%" + ve.getContactTelNO().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ve.getContactEmail())) {
			criterions.add(Restrictions.like("contractEmail", "%" + ve.getContactEmail().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ve.getAddress())) {
			criterions.add(Restrictions.like("address", "%" + ve.getAddress().trim() + "%"));
		}
		if (TrimUtil.trimUtil(ve.getWechatID())) {
			criterions.add(Restrictions.like("wechatID", "%" + ve.getWechatID() + "%"));
		}
		if (TrimUtil.trimUtil(ve.getDianpingID())) {
			criterions.add(Restrictions.like("dianpingID", "%" + ve.getDianpingID() + "%"));
		}

		return criterions;
	}

	@Override
	public List<Order> generateOrder(Ordering ordering) {
		List<Order> orderList = new ArrayList<Order>();
		Map<String, sort> criteria = ordering.getOrderCriteria();
		if (criteria.size() > 0) {
			Set<String> set = criteria.keySet();
			for (String key : set) {
				if (criteria.get(key) == sort.ASCENDING) {
					orderList.add(Order.asc(key));
				} else if (criteria.get(key) == sort.DESCENDING) {
					orderList.add(Order.desc(key));
				}
			}
		}
		return orderList;
	}

	@Override
	public boolean checkExistsByName(String vendorName) {

		List<Vendor> list = findVendorbyName(vendorName);

		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public List<Vendor> findVendorbyName(String vendorName) {
		String HQL = "FROM Vendor v where v.name = :vendorName";
		Query query = currentSession().createQuery(HQL);
		query.setParameter("vendorName", vendorName);

		@SuppressWarnings("unchecked")
		List<Vendor> list = query.list();

		return list;
	}

	@Override
	public List<Vendor> getActiveVendors() {
		return getVendorsByStatus(ConstantUtil.vendorStatus.ACTIVE);
	}

	@Override
	public List<Vendor> getInactiveVendors() {
		return getVendorsByStatus(ConstantUtil.vendorStatus.INACTIVE);
	}

	private List<Vendor> getVendorsByStatus(ConstantUtil.vendorStatus status) {
		// current_date()
		Date today = new Date(CommonUtil.getCurrentTime().getTime());
		// StringBuilder hql = new StringBuilder("select v from Vendor v join
		// v.contractList c where ");
		StringBuilder hql = new StringBuilder();
		String hql_active = "select v from Vendor v join v.contractList c where c.beginDate <= :today and c.endDate >= :today ";
		String hql_inactive = "select v from Vendor v where v not in ";
		
		if (status.equals(ConstantUtil.vendorStatus.ACTIVE)) {
			hql.append(hql_active);
		} else {
			hql.append(hql_inactive);
			hql.append("(");
			hql.append(hql_active);
			hql.append(")");
		}

		hql.append(" order by v.id");
		Query query = currentSession().createQuery(hql.toString());
		query.setParameter("today", today);
		@SuppressWarnings("unchecked")
		List<Vendor> list = query.list();

		return list;
	}

}
