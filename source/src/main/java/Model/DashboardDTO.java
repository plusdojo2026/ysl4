package Model;

import java.util.ArrayList;
import java.util.List;

public class DashboardDTO {

    // 進行中案件数を表示する
    private int inProgressProjectCount;

    // 自分の担当タスク数を表示する
    private int assignedTaskCount;

    // 期限超過タスク数を表示する
    private int overdueTaskCount;

    // 今月の自分の工数を表示する
    private float monthlyMyManhours;

    // 自分のタスク進捗率を表示する
    private int myTaskProgressRate;

    // プロジェクト全体の進捗率を表示する
    private int allTaskProgressRate;

    // 今月の工数達成率を表示する
    private int monthlyWorkAchievementRate;

    // 今日の予定件数を表示する(追加機能)
//    private int todayScheduleCount;

    // 自分のタスク一覧を表示する
    private List<AssignedTaskDTO> assignedTaskList = new ArrayList<>();

    // 進行中案件一覧を表示する
    private List<InProgressProjectDTO> inProgressProjectList = new ArrayList<>();

    public int getInProgressProjectCount() {
        return inProgressProjectCount;
    }

    public void setInProgressProjectCount(int inProgressProjectCount) {
        this.inProgressProjectCount = inProgressProjectCount;
    }

    public int getAssignedTaskCount() {
        return assignedTaskCount;
    }

    public void setAssignedTaskCount(int assignedTaskCount) {
        this.assignedTaskCount = assignedTaskCount;
    }

    public int getOverdueTaskCount() {
        return overdueTaskCount;
    }

    public void setOverdueTaskCount(int overdueTaskCount) {
        this.overdueTaskCount = overdueTaskCount;
    }

    public float getMonthlyMyManhours() {
        return monthlyMyManhours;
    }

    public void setMonthlyMyManhours(float monthlyMyManhours) {
        this.monthlyMyManhours = monthlyMyManhours;
    }

    public int getMyTaskProgressRate() {
        return myTaskProgressRate;
    }

    public void setMyTaskProgressRate(int myTaskProgressRate) {
        this.myTaskProgressRate = myTaskProgressRate;
    }

    public int getAllTaskProgressRate() {
        return allTaskProgressRate;
    }

    public void setAllTaskProgressRate(int allTaskProgressRate) {
        this.allTaskProgressRate = allTaskProgressRate;
    }

    public int getMonthlyWorkAchievementRate() {
        return monthlyWorkAchievementRate;
    }

    public void setMonthlyWorkAchievementRate(int monthlyWorkAchievementRate) {
        this.monthlyWorkAchievementRate = monthlyWorkAchievementRate;
    }

    //(追加機能)
//    public int getTodayScheduleCount() {
//        return todayScheduleCount;
//    }
//
//    public void setTodayScheduleCount(int todayScheduleCount) {
//        this.todayScheduleCount = todayScheduleCount;
//    }

    public List<AssignedTaskDTO> getAssignedTaskList() {
        return assignedTaskList;
    }

    public void setAssignedTaskList(List<AssignedTaskDTO> assignedTaskList) {
        if (assignedTaskList == null) {
            this.assignedTaskList = new ArrayList<>();
            return;
        }
        this.assignedTaskList = assignedTaskList;
    }

    public List<InProgressProjectDTO> getInProgressProjectList() {
        return inProgressProjectList;
    }

    public void setInProgressProjectList(List<InProgressProjectDTO> inProgressProjectList) {
        if (inProgressProjectList == null) {
            this.inProgressProjectList = new ArrayList<>();
            return;
        }
        this.inProgressProjectList = inProgressProjectList;
    }

    public static class AssignedTaskDTO {

        // タスク詳細への遷移で使う
        private int taskId;

        // タスク名を表示する
        private String taskName;

        // 案件名を表示する
        private String projectName;

        // 期限を表示する
        private String dueDate;

        // 優先度を表示する
        private String priority;

        // 進捗率を表示する
        private int progress;

        // ステータスを表示する
        private String status;

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

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
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
    }

    public static class InProgressProjectDTO {

        // 案件詳細への遷移で使う
        private int projectId;

        // 案件名を表示する
        private String projectName;

        // 進捗率を表示する
        private int progress;

        // ステータスを表示する
        private String status;

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
    }
}