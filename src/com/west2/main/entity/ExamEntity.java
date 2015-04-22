package com.west2.main.entity;

import com.west2.main.utils.InfoUtils;

public class ExamEntity {
	private String teacher;
	private String courseName;
	private String examPlace;
	private String examTime;
	public String getExamTime() {
		return examTime;
	}
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		while(!InfoUtils.hasChCharacter(courseName.charAt(0) + "")){
			courseName = courseName.substring(1);
		}
		this.courseName = courseName;
	}
	public String getExamPlace() {
		return examPlace;
	}
	public void setExamPlace(String examPlace) {
		this.examPlace = examPlace;
	}
	
}
