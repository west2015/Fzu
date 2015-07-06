package com.west2.main.entity;

import com.west2.main.utils.InfoUtils;

public class MarkEntity {
	private String term;
	private String courseName;
	private String score;
	private String gradePoint;
	private String gradeCredit;
	private String courseType;

	public String getCourseType() {
		return courseType;
	}
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getCourseName(){
		return courseName;
	}
	public String getScore(){
		return score;
	}
	public void setCourseName(String coursename){
		courseName = coursename;
	}
	public void setScore(String Score){
		this.score = Score.replace("³É¼¨", "");
	}
	public String getGradePoint() {
		return gradePoint;
	}
	public void setGradePoint(String gradePoint) {
		this.gradePoint = gradePoint.replace("³É¼¨", "");
	}
	public String getGradeCredit() {
		return gradeCredit;
	}
	public void setGradeCredit(String gradeCredit) {
		this.gradeCredit = gradeCredit;
	}
	
}