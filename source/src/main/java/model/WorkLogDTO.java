package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WorkLogDTO {

	private int workLogsId;
	private int taskId;
	private int userId;
	private String userName;
	private String workDate;
	private float manHours;
	private String jobContents;
	private Timestamp cAt;
	private Timestamp uAt;
	// 進行中案件一覧を表示する
	private List<projectWorkLogDTO> ProjectWorkLogList = new ArrayList<>();

	public WorkLogDTO(int workLogsId, int taskId, int userId, String userName,  String workDate, float manHours, String jobContents,
			Timestamp cAt, Timestamp uAt) {
		super();
		this.workLogsId = workLogsId;
		this.taskId = taskId;
		this.userId = userId;
		this.userName = userName;
		this.workDate = workDate;
		this.manHours = manHours;
		this.jobContents = jobContents;
		this.cAt = cAt;
		this.uAt = uAt;
	}
	
	//コンストラクタ
	public WorkLogDTO() {
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

	public String setUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
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

	public static class projectWorkLogDTO {
		//工数の作業日
		private String workDate;
		//タスク名
		private String taskName;
		//タスク担当者名
		private String personInCharge;
		//タスクの工数の合計
		private String workLogSum;
		//タスクの説明
		private String description;

		public String getWorkDateString() {
			return workDate;
		}

		public void setWorkDateString(String workDateString) {
			this.workDate = workDateString;
		}

		public String getTaskNameString() {
			return taskName;
		}

		public void setTaskNameString(String taskNameString) {
			this.taskName = taskNameString;
		}

		public String getPersonInCharge() {
			return personInCharge;
		}

		public void setPersonInCharge(String personInCharge) {
			this.personInCharge = personInCharge;
		}

		public String getWorkLogSum() {
			return workLogSum;
		}

		public void setWorkLogSum(String workLogSum) {
			this.workLogSum = workLogSum;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}
}