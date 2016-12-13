package com.sap.moc.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.sap.moc.entity.Employee;

public class CommonUtil {
	public static Locale formatEmployeeName(Employee employee) {
		String nameLocale = "";
		Locale locale;
		// Build Name format base on language setting
		switch (employee.getLanguage()) {
		case CHINESE:
			locale = Locale.CHINESE;
			nameLocale = employee.getLastName() + " " + employee.getFirstName();
			break;
		case ENGLISH:
			locale = Locale.ENGLISH;
			nameLocale = employee.getFirstName() + " " + employee.getLastName();
			break;
		default:
			locale = I18nUtil.DEFAULT_LOCALE;
			nameLocale = employee.getFirstName() + " " + employee.getLastName();
			break;
		}
		employee.setFormattedName(nameLocale);
		return locale;
	}

	public static Date getCurrentTime() {

		String zone = ConstantReader.readByKey("TIME_ZONE");
		if (zone == null || zone.length() == 0) {
			zone = "GMT+08:00";
		}
		TimeZone timeZone = TimeZone.getTimeZone(zone);
		Calendar now = Calendar.getInstance(timeZone);
		return now.getTime();
	}

	public static Timestamp getCurrentTimestamp() {
		Timestamp time = new Timestamp(getCurrentTime().getTime());
		return time;
	}

	public static Calendar getCurrentCalendar() {

		String zone = ConstantReader.readByKey("TIME_ZONE");
		if (zone == null || zone.length() == 0 || "".equals(zone)) {
			zone = "GMT+08:00";
		}
		TimeZone timeZone = TimeZone.getTimeZone(zone);
		Calendar now = Calendar.getInstance(timeZone);
		return now;
	}

	public static int getMealPrice() {
		int mealPrice = 0;
		String price = ConstantReader.readByKey("MEAL_PRICE");
		if (price != null && !price.equals("")) {
			mealPrice = Integer.parseInt(price);
		}
		return mealPrice;
	}

	public static int getLastDayOfMonth(int year, int month) {
		
		Calendar cal = CommonUtil.getCurrentCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		// The last day of the month
		return cal.getActualMaximum(Calendar.DATE);
	}
}
