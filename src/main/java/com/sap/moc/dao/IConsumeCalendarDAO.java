package com.sap.moc.dao;

import com.sap.moc.entity.ConsumeCalendar;

public interface IConsumeCalendarDAO extends IGenericDAO<ConsumeCalendar, String>{
	
	public ConsumeCalendar getTodayCalendar();
	
	public void refreshCalendar();

}
