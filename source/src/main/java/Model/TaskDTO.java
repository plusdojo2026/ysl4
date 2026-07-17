package Model;

import java.sql.Timestamp;
import java.util.Date;

public class TaskDTO {
	
	public TaskDTO() {
		
	}

	public TaskDTO(int taskId, String taskName, int projectId, int managerId, Date startDate, Date dueDate,
			int estimatedManhours, int progress, String status, String priority, String description,
			Timestamp createdAt, Timestamp updatedAt) {

		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.projectId = projectId;
		this.managerId = managerId;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.estimatedManhours = estimatedManhours;
		this.progress = progress;
		this.status = status;
		this.priority = priority;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	private int taskId;
	private String taskName;
	private int projectId;
	private int managerId;
	private Date startDate;
	private Date dueDate;
	private int estimatedManhours;
	private int progress;
	private String status;
	private String priority;
	private String description;
	private Timestamp createdAt;
	private Timestamp updatedAt;

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

	public int getManagerId() {
		return managerId;
	}

	public void setManagerId(int managerId) {
		this.managerId = managerId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public int getEstimatedManhours() {
		return estimatedManhours;
	}

	public void setEstimatedManhours(int estimatedManhours) {
		this.estimatedManhours = estimatedManhours;
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

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}