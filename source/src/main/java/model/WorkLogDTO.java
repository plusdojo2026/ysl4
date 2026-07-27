package model;

import java.io.Serializable;

/**
 * 工数ログ情報を保持するDTO
 * WorkLogsテーブルの値と画面表示用の値を受け渡す
 */
public class WorkLogDTO implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** 工数ログID */
    private int workLogsId;

    /** タスクID */
    private int taskId;

    /** タスク名 */
    private String taskName;

    /** 案件ID */
    private int projectId;

    /** 案件名 */
    private String projectName;

    /** 作業者ID */
    private int userId;

    /** 作業者名 */
    private String userName;

    /** 作業日 */
    private String workDate;

    /** 工数 */
    private float manHours;

    /** 作業内容 */
    private String jobContents;

    /** 作成日時 */
    private String createdAt;

    /** 更新日時 */
    private String updatedAt;

    public int getWorkLogsId() {
        return workLogsId;
    }

    public void setWorkLogsId(int workLogsId) {
        this.workLogsId = workLogsId;
    }

    public int getWorkLogId() {
        return workLogsId;
    }

    public void setWorkLogId(int workLogId) {
        this.workLogsId = workLogId;
    }

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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}