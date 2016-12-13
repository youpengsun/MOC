package com.sap.moc.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CONSUME_CALENDAR")
public class ConsumeCalendar {
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="ID")
	private int id;

	@Column(name = "DATE")
	private Date date;

	@Column(name = "MEAL_VALID")
	private boolean mealValid;
	
	@Column(name = "HOLIDAY_NAME")
	private String holidayName;

	@Column(name = "COMMENT")
	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isMealValid() {
		return mealValid;
	}

	public void setMealValid(boolean mealValid) {
		this.mealValid = mealValid;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}



}
