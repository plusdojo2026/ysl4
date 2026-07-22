package model;

import java.io.Serializable;

public class MemberSummaryDTO implements Serializable {
	private float manHours;//工数
	private int userId;//ユーザーID
	private String userName;//ユーザー名
	private int achivementRate;//達成率
	private double estimatedManhours;//見積工数
	private double actualManhours;//実績工数
	private int taskCount;//タスク数
	
	//ゲッターとセッター
	public float getManHours() {
		return manHours;
	}
	
	public void setManHours(float manHours) {
		this.manHours = manHours;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public int getAchivementRate() {
		return achivementRate;
	}
	
	public void setAchivementRate(int achivementRate) {
		this.achivementRate = achivementRate;
	}
	
	public double getEstimatedManhours() {
		return estimatedManhours;
	}
	
	public void setEstimatedManhours(double estimatedManhours) {
		this.estimatedManhours = estimatedManhours;
	}
	
	public double getActualManhours() {
		return actualManhours;
	}
	
	public void setActualManhours(double actualManhours) {
		this.actualManhours = actualManhours;
	}
	
	public int getTaskCount() {
		return taskCount;
	}
	
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
}