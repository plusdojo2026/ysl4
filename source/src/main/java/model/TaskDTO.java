package model;

import java.sql.Date;
import java.util.List;

public class TaskDTO {
	
	public TaskDTO() {
		
	}

	public TaskDTO(int taskId, String taskName, int projectId,String projectName, int managerId, String managerName, String startDate, String dueDate,
			float estimatedManhours, float actualManhours, int progress, String status, String priority, String description, List<WorkLogDTO> workLogs, 
			Date createdAt, Date updatedAt, boolean overdue) {

		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.projectId = projectId;
		this.projectName = projectName;
		this.managerId = managerId;
		this.managerName = managerName;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.estimatedManhours = estimatedManhours;
		this.actualManhours = actualManhours;
		this.progress = progress;
		this.status = status;
		this.priority = priority;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.overdue = overdue;
	}

	private int taskId; //タスクID
	private String taskName; //タスク名
	private int projectId; // 案件ID
	private String projectName; // 案件名
	private int managerId; //担当者ID
	private String managerName; // 担当者名
	private String startDate; // 開始日
	private String dueDate; //期限
	private float estimatedManhours; //見積工数
	private float actualManhours; //実績工数
	private int progress; //進捗率
	private String status; //ステータス
	private String priority; // 優先度
	private String description; //説明
	private List<WorkLogDTO> workLogs; //工数リスト
	private List<ProjectsDTO> projectList; //案件リスト
	private List<UserDTO> userList; //ユーザーリスト
	private Date createdAt; //作成日
	private Date updatedAt; //編集日
	private boolean overdue; //期限超過

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}
	
	public String getManagerName() {
		return managerName;
	}
	
	public void setManagerName(String managerName) {
		this.managerName = managerName;
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

	public float getEstimatedManhours() {
		return estimatedManhours;
	}

	public void setEstimatedManhours(float estimatedManhours) {
		this.estimatedManhours = estimatedManhours;
	}
	
	public float getActualManhours() {
		return actualManhours;
	}
	
	public void setActualManhours(float actualManhours) {
		this.actualManhours = actualManhours;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<WorkLogDTO> getWorkLogs() {
	    return workLogs;
	}

	public void setWorkLogs(List<WorkLogDTO> workLogs) {
	    this.workLogs = workLogs;
	}
	
	public List<ProjectsDTO> getProjectList() {
	    return projectList;
	}

	public void setProjectList(List<ProjectsDTO> projectList) {
	    this.projectList = projectList;
	}

	public List<UserDTO> getUserList() {
	    return userList;
	}

	public void setUserList(List<UserDTO> userList) {
	    this.userList = userList;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public boolean getOverdue() {
		return overdue;
	}
	
	public void setoverdue(boolean overdue) {
		this.overdue = overdue;
	}

}