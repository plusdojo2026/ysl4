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

    // 今月の自分の登録工数を表示する
    private float monthlyMyManhours;

    // 自分のタスク進捗率を表示する
    private int myTaskProgressRate;

    // 全タスクの平均進捗率を表示する
    private int allTaskProgressRate;

    // ホーム画面では月次予算を入力しないため初期値0で使う
    private int monthlyWorkAchievementRate;

    // 進行中案件一覧を表示する
    private List<ProjectsDTO> inProgressProjectList = new ArrayList<>();

    // 自分の担当タスク一覧を表示する
    private List<TaskDTO> assignedTaskList = new ArrayList<>();

    // 予定件数(追加機能)
    // private int todayScheduleCount;

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

    public List<ProjectsDTO> getInProgressProjectList() {
        return inProgressProjectList;
    }

    public void setInProgressProjectList(List<ProjectsDTO> inProgressProjectList) {
        if (inProgressProjectList == null) {
            this.inProgressProjectList = new ArrayList<>();
            return;
        }
        this.inProgressProjectList = inProgressProjectList;
    }

    public List<TaskDTO> getAssignedTaskList() {
        return assignedTaskList;
    }

    public void setAssignedTaskList(List<TaskDTO> assignedTaskList) {
        if (assignedTaskList == null) {
            this.assignedTaskList = new ArrayList<>();
            return;
        }
        this.assignedTaskList = assignedTaskList;
    }

    // 追加機能用の予定件数getter
    // public int getTodayScheduleCount() {
    //     return todayScheduleCount;
    // }

    // 追加機能用の予定件数setter
    // public void setTodayScheduleCount(int todayScheduleCount) {
    //     this.todayScheduleCount = todayScheduleCount;
    // }
}