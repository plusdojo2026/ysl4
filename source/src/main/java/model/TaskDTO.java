package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * タスク情報を入れるDTO.
 * Tasksテーブルの値と画面表示用の値を持つ.
 */
public class TaskDTO implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** タスクID */
    private int taskId;

    /** タスク名 */
    private String taskName;

    /** 案件ID */
    private int projectId;
    
    /** 案件コード */
    private int projectCode;

    /** 案件名 */
    private String projectName;

    /** 担当者ID */
    private int managerId;

    /** 担当者名 */
    private String managerName;

    /** 開始日 */
    private String startDate;

    /** 期限日 */
    private String dueDate;

    /** 見積工数 */
    private float estimatedManhours;

    /** 実績工数 */
    private float actualManhours;

    /** 進捗率 */
    private int progress;

    /** ステータス */
    private String status;

    /** 優先度 */
    private String priority;

    /** 説明 */
    private String description;

    /** 作成日時 */
    private String createdAt;

    /** 更新日時 */
    private String updatedAt;

    /** 期限超過フラグ */
    private boolean overdue;

    /** タスク詳細で表示する工数ログ一覧 */
    private List<WorkLogDTO> workLogList = new ArrayList<>();

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
    
    public int getProjectCode() {
    	return projectCode;
    }
    
    public void setProjectCode(int projectCode) {
    	this.projectCode = projectCode;
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

    public boolean isOverdue() {
        return overdue;
    }

    public boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }

    public List<WorkLogDTO> getWorkLogList() {
        return workLogList;
    }

    public void setWorkLogList(List<WorkLogDTO> workLogList) {
        this.workLogList = workLogList;
    }
}