package com.sap.moc.vo;

public class QueryTime {

	private int year;

	private int month;

	private int day;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}

	public QueryTime() {
		super();
	}

	public QueryTime(int year, int month, int day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}
}
