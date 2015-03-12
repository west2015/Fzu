package fzu.mcginn.entity;

public class DateEntity {
	private Integer year;
	private Integer month;
	private Integer day;
	private Integer currentWeek;
	private Integer schoolYear;
	private Integer term;
	private Integer weekDayNum;

	private String weekDay;
	
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
	
	public Integer getWeekDayNum() {
		return weekDayNum;
	}

	public void setWeekDayNum(Integer weekDayNum) {
		this.weekDayNum = weekDayNum;
	}

	public void setTerm(Integer term) {
		this.term = term;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
		if(weekDay.contains("一")){
			this.weekDayNum = 0;
		} else
		if(weekDay.contains("二")){
			this.weekDayNum = 1;
		} else
		if(weekDay.contains("三")){
			this.weekDayNum = 2;
		} else
		if(weekDay.contains("四")){
			this.weekDayNum = 3;
		} else
		if(weekDay.contains("五")){
			this.weekDayNum = 4;	
		} else
		if(weekDay.contains("六")){
			this.weekDayNum = 5;
		} else{
			this.weekDayNum = 6;
		}
	}
	
	
}
