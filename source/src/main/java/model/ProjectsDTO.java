package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**

◦ 案件情報を保持するDTO.
◦ Projectsテーブルの値と画面表示用の集計値を受け渡す.
 */
public class ProjectsDTO implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** 案件ID */
    private int projectId;

    /** 案件コード */
    private String projectCode;

    /** 案件名 */
    private String projectName;

    /** 顧客名 */
    private String customerName;

    /** 登録者ID */
    private int createMemberId;

    /** PMユーザーID */
    private int projectManagerId;

    /** PM名 */
    private String projectManagerName;

    /** 開始予定日 */
    private String startDate;

    /** 終了予定日 */
    private String dueDate;

    /** 予算工数 */
    private float estimatedManhours;

    /** 実績工数 */
    private float actualManhours;

    /** 説明 */
    private String description;

    /** ステータス */
    private String status;

    /** 優先度 */
    private String priority;

    /** タスク数 */
    private int taskCount;

    /** 完了タスク数 */
    private int completedTaskCount;

    /** 進捗率 */
    private float progressRate;

    /** 作成日時 */
    private String createdAt;

    /** 更新日時 */
    private String updatedAt;

    /** 案件詳細で表示するタスク一覧 */
    private List<TaskDTO> taskList = new ArrayList<>();

    /** 案件詳細で表示する最新工数ログ一覧 */
    private List<WorkLogDTO> latestWorkLogList = new ArrayList<>();

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

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
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

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(int completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public float getProgressRate() {
        return progressRate;
    }

    public void setProgressRate(float progressRate) {
        this.progressRate = progressRate;
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

    public List<TaskDTO> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskDTO> taskList) {
        this.taskList = taskList;
    }

    public List<WorkLogDTO> getLatestWorkLogList() {
        return latestWorkLogList;
    }

    public void setLatestWorkLogList(List<WorkLogDTO> latestWorkLogList) {
        this.latestWorkLogList = latestWorkLogList;
    }
}