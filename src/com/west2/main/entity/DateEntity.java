package com.west2.main.entity;

public class DateEntity {
	private Integer year;
	private Integer month;
	private Integer day;
	private Integer currentWeek;
	private Integer schoolYear;
	private Integer term;
	private Integer weekDay;
	
	public Integer getCurrentWeek() {
		return currentWeek;
	}

	public void setCurrentWeek(Integer currentWeek) {
		this.currentWeek = currentWeek;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getSchoolYear() {
		return schoolYear;
	}

	public void setSchoolYear(Integer schoolYear) {
		this.schoolYear = schoolYear;
	}

	public Integer getTerm() {
		return term;
	}
	
	public Integer getweekDay() {
		return weekDay;
	}

	public void setweekDay(Integer weekDay) {
		this.weekDay = weekDay;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}	
	
}
