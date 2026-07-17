package Model;

import java.sql.Date;
import java.sql.Timestamp;

public class WorkLogDTO {

	private int workLogsId;
	private int taskId;
	private int userId;
	private Date workDate;
	private float manHours;
	private String jobContents;
	private Timestamp cAt;
	private Timestamp uAt;

	public WorkLogDTO(int workLogsId, int taskId, int userId, Date workDate, float manHours, String jobContents,
			Timestamp cAt, Timestamp uAt) {
		super();
		this.workLogsId = workLogsId;
		this.taskId = taskId;
		this.userId = userId;
		this.workDate = workDate;
		this.manHours = manHours;
		this.jobContents = jobContents;
		this.cAt = cAt;
		this.uAt = uAt;
	}

	public int getWorkLogsId() {
		return workLogsId;
	}

	public void setWorkLogsId(int workLogsId) {
		this.workLogsId = workLogsId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public float getManHours() {
		return manHours;
	}

	public void setManHours(float manHours) {
		this.manHours = manHours;
	}

	public String getJobContents() {
		return jobContents;
	}

	public void setJobContents(String jobContents) {
		this.jobContents = jobContents;
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