package com.west2.main.entity;

import java.io.Serializable;

public class CourseEntity implements Serializable {
	private String name;
	private String place;
	private String teacherName;
	
	private int weekday;
	private int startWeek;
	private int endWeek;
	private int lesson;
	private int length;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPlace() {
		return place;
	}
	
	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getTeacherName() {
		return teacherName;
	}
	
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	public int getWeekday() {
		return weekday;
	}
	
	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}
	
	public int getLesson() {
		return lesson;
	}
	
	public void setLesson(int lesson) {
		this.lesson = lesson;
	}
	
	public int getStartWeek() {
		return startWeek;
	}
	
	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}
	
	public int getEndWeek() {
		return endWeek;
	}
	
	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
}
