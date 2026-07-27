package model;

import java.io.Serializable;

/**
 * 月次集計の案件別集計行を保持するDTO
 */
public class ProjectSummaryDTO implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** 案件コード */
    private String projectCode;

    /** 案件名 */
    private String projectName;

    /** 実績工数 */
    private float actualManhours;

    /** 見積工数 */
    private float estimatedManhours;

    /** 達成率 */
    private double achievementRate;

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

    public float getActualManhours() {
        return actualManhours;
    }

    public void setActualManhours(float actualManhours) {
        this.actualManhours = actualManhours;
    }

    public float getEstimatedManhours() {
        return estimatedManhours;
    }

    public void setEstimatedManhours(float estimatedManhours) {
        this.estimatedManhours = estimatedManhours;
    }

    public double getAchievementRate() {
        return achievementRate;
    }

    public void setAchievementRate(double achievementRate) {
        this.achievementRate = achievementRate;
    }
}