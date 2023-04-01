package com.plagiarismserver.springrest.model;

public class Plagiarism {

	private String studentName;
	private String studentID;
	private String assignmentID;
	private String courseID;
	private String questionID;
	private int questionNum;
	private String questionInfo;
	private String languageName;
	private String testCases;
	private String answer;
	private Plagiarism[] otherSubmissions;

	public Plagiarism() {
	}

	public Plagiarism(String studentName, String studentID, String assignmentID, String courseID, String questionID,
			int questionNum, String questionInfo, String languageName, String testCases, String answer, Plagiarism[] otherSubmissions) {
		
		this.studentName = studentName;
		this.studentID = studentID;
		this.assignmentID = assignmentID;
		this.courseID = courseID;
		this.questionID = questionID;
		this.questionNum = questionNum;
		this.questionInfo = questionInfo;
		this.languageName = languageName;
		this.testCases = testCases;
		this.answer = answer;
		this.otherSubmissions = otherSubmissions;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getAssignmentID() {
		return assignmentID;
	}

	public void setAssignmentID(String assignmentID) {
		this.assignmentID = assignmentID;
	}

	public String getCourseID() {
		return courseID;
	}

	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}

	public String getQuestionID() {
		return questionID;
	}

	public void setQuestionID(String questionID) {
		this.questionID = questionID;
	}

	public int getQuestionNum() {
		return questionNum;
	}

	public void setQuestionNum(int questionNum) {
		this.questionNum = questionNum;
	}

	public String getQuestionInfo() {
		return questionInfo;
	}

	public void setQuestionInfo(String questionInfo) {
		this.questionInfo = questionInfo;
	}

	public String getLanguageName() {
		return languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Plagiarism[] getOtherSubmissions() {
		return otherSubmissions;
	}

	public void setOtherSubmissions(Plagiarism[] otherSubmissions) {
		this.otherSubmissions = otherSubmissions;
	}

	@Override
	public String toString() {
		return "Submission {" + "\n\tid : " + studentID + ", \n\tname : '" + studentName + '\'' + ", \n\tcourseID : '"
				+ courseID + ", \n\tassignmentID : '" + assignmentID + ", \n\tquestionID : '" + questionID
				+ ", \n\tquestionNum : '" + questionNum + ", \n\tquestionInfo : '" + questionInfo + ", \n\tanswer : '"
				+ answer + '}';
	}

	public String getTestCases() {
		return testCases;
	}

	public void setTestCases(String testCases) {
		this.testCases = testCases;
	}

}
