package model;


import java.sql.Timestamp;

public class ProjectsDTO {

	public ProjectsDTO() {}
	
	public ProjectsDTO(int projectId, String projectCode, String projectName, String customerName, int createMemberId,
			int projectManagerId, String startDate, String dueDate, int estimatedManhours, int actualManhours, String description, String status, String priority,
			Timestamp cAt, Timestamp uAt) {
		super();
		this.projectId = projectId;
		this.projectCode = projectCode;
		this.projectName = projectName;
		this.customerName = customerName;
		this.createMemberId = createMemberId;
		this.projectManagerId = projectManagerId;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.estimatedManhours = estimatedManhours;
		this.actualManhours =actualManhours;
		this.description = description;
		this.status = status;
		this.priority = priority;
		this.cAt = cAt;
		this.uAt = uAt;
	}
	
	private int projectId;
	private String projectCode;
	private String projectName;
	private String customerName;
	private int createMemberId;
	private int projectManagerId;
	private String startDate;
	private String dueDate;
	private String description;
	private int estimatedManhours;
	private int actualManhours;
	private String status;
	private String priority;
	private Timestamp cAt;
	private Timestamp uAt;
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getCreateMemberId() {
		return createMemberId;
	}
	public void setCreateMemberId(int createMemberId) {
		this.createMemberId = createMemberId;
	}
	public int getProjectManagerId() {
		return projectManagerId;
	}
	public void setProjectManagerId(int projectManagerId) {
		this.projectManagerId = projectManagerId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public int getEstimatedManhours() {
		return estimatedManhours;
	}
	public void setEstimatedManhours(int estimatedManhours) {
		this.estimatedManhours = estimatedManhours;
	}
	public int getActualManhours() {
		return actualManhours;
	}
	public void setActualManhours(int actualManhours) {
		this.actualManhours= actualManhours;
	}	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Timestamp getcAt() {
		return cAt;
	}
	public void setcAt(Timestamp cAt) {
		this.cAt = cAt;
	}
	public Timestamp getuAt() {
		return uAt;
	}
	public void setuAt(Timestamp uAt) {
		this.uAt = uAt;
	}
	
	
	
	
	
}