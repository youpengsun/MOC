package com.sap.moc.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeCheckUtil {

	
	//Check current time is lunch time or dinner time
	public static String getCurrentCategory()
	{	
		String category = "";
		Calendar cal = CommonUtil.getCurrentCalendar();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		try {
			Date lunchB  = format.parse(ConstantReader.readTimeByKey("LUNCH_BEGIN"));
			Date lunchE  = format.parse(ConstantReader.readTimeByKey("LUNCH_END"));
			Date dinnerB = format.parse(ConstantReader.readTimeByKey("DINNER_BEGIN"));
			Date dinnerE = format.parse(ConstantReader.readTimeByKey("DINNER_END"));
			String str = format.format(cal.getTime());
			Date now = format.parse(str);
			if(now.compareTo(lunchB) >= 0 && now.compareTo(lunchE) <= 0){
				category = ConstantUtil.CONSUME_CATEGORY_LUNCH;
			}
			else if(now.compareTo(dinnerB) >= 0 && now.compareTo(dinnerE) <= 0){
				category = ConstantUtil.CONSUME_CATEGORY_DINNER;
			}
			
		} 
		catch (ParseException e) {
			
		}
		
		return category;
	}
	

}
