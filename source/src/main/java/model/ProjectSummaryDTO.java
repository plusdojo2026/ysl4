package model;


public class ProjectSummaryDTO {
	
	public ProjectSummaryDTO(String projectCode, String projectName, int actualManhours, int estimatedManhours) {
		super();
		this.projectCode = projectCode;
		this.projectName = projectName;
		this.actualManhours = actualManhours;
		this.estimatedManhours = estimatedManhours;
	}
	private String projectCode;
	private String projectName;
	private int actualManhours;
	private int estimatedManhours;
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public int getActualManhours() {
		return actualManhours;
	}
	public void setActualManhours(int actualManhours) {
		this.actualManhours = actualManhours;
	}
	public int getEstimatedManhours() {
		return estimatedManhours;
	}
	public void setEstimatedManhours(int estimatedManhours) {
		this.estimatedManhours = estimatedManhours;
	}
}
