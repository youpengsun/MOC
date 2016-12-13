package com.sap.moc.dao.impl;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.sap.moc.dao.IConsumeCalendarDAO;
import com.sap.moc.entity.ConsumeCalendar;
import com.sap.moc.utils.CommonUtil;

@Repository
public class ConsumeCalendarDAOImpl extends GenericDAOImpl<ConsumeCalendar, String> implements IConsumeCalendarDAO {

	private Map<String, ConsumeCalendar> CalendarMap;

	@Override
	public ConsumeCalendar getTodayCalendar() {
		return getTodayCalendarInMemory();
	}

	@Override
	public void refreshCalendar() {
		CalendarMap = readFromDB();
	}

	public void setCalendarMap(Map<String, ConsumeCalendar> calendarMap) {
		CalendarMap = calendarMap;
	}

	private ConsumeCalendar getTodayCalendarInMemory() {

		String todayKey = new Date(CommonUtil.getCurrentTime().getTime()).toString();
		ConsumeCalendar result = getCalendarMap().get(todayKey);
		return result;
	}

	@SuppressWarnings("unused")
	private ConsumeCalendar getTodayCalendarInDB() {
		
		Calendar cal = CommonUtil.getCurrentCalendar();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date today = new Date(cal.getTime().getTime());
		
		String hql = "from ConsumeCalendar c where c.date = :today ";
		Query query = currentSession().createQuery(hql);
		query.setParameter("today", today);

		@SuppressWarnings("unchecked")
		List<ConsumeCalendar> dates = query.list();

		ConsumeCalendar result = dates.size() > 0 ? dates.get(0) : null;
		return result;
	}

	private Map<String, ConsumeCalendar> getCalendarMap() {

		if (CalendarMap == null) {

			CalendarMap = readFromDB();
		}
		return CalendarMap;
	}

	private Map<String, ConsumeCalendar> readFromDB() {
		Map<String, ConsumeCalendar> map = new HashMap<>();
		List<ConsumeCalendar> list = super.getAll();
		for (ConsumeCalendar consumeCalendar : list) {
			String dateKey = consumeCalendar.getDate().toString();
			// Avoid multiple Input for one Date
			if (!map.containsKey(dateKey)) {
				map.put(dateKey, consumeCalendar);
			}
		}
		return map;

	}
}
