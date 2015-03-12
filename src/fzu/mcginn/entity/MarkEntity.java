package fzu.mcginn.entity;

public class MarkEntity {
	private String courseName;
	private String score;
	private String gradePoint;
	private String gradeCredit;
	
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
		score = Score;
	}
	public String getGradePoint() {
		return gradePoint;
	}
	public void setGradePoint(String gradePoint) {
		this.gradePoint = gradePoint;
	}
	public String getGradeCredit() {
		return gradeCredit;
	}
	public void setGradeCredit(String gradeCredit) {
		this.gradeCredit = gradeCredit;
	}
	
	
	
}