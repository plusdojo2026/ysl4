package model;

import java.io.Serializable;

/**
 * 月次集計のメンバー別集計行を保持するDTO
 */
public class MemberSummaryDTO implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** 工数 */
    private float manHours;

    /** ユーザーID */
    private int userId;

    /** ユーザー名 */
    private String userName;

    /** 達成率 */
    private double achievementRate;

    /** 見積工数 */
    private float estimatedManhours;

    /** 実績工数 */
    private float actualManHours;

    /** タスク数 */
    private int taskCount;

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

    public String getName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.userName = name;
    }

    public double getAchievementRate() {
        return achievementRate;
    }

    public void setAchievementRate(double achievementRate) {
        this.achievementRate = achievementRate;
    }

    public double getAchivementRate() {
        return achievementRate;
    }

    public void setAchivementRate(double achivementRate) {
        this.achievementRate = achivementRate;
    }

    public float getEstimatedManhours() {
        return estimatedManhours;
    }

    public void setEstimatedManhours(float estimatedManhours) {
        this.estimatedManhours = estimatedManhours;
    }

    public float getActualManHours() {
        return actualManHours;
    }

    public void setActualManHours(float actualManHours) {
        this.actualManHours = actualManHours;
    }

    public float getActualManhours() {
        return actualManHours;
    }

    public void setActualManhours(float actualManhours) {
        this.actualManHours = actualManhours;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }
}