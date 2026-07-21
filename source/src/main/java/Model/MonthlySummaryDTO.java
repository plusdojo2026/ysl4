package Model;

import java.util.ArrayList;
import java.util.List;

public class MonthlySummaryDTO {

    // 集計対象月をyyyy-MM形式で保持する
    private String targetMonth;

    // 画面で入力された月次予算工数を保持する
    private float monthlyBudgetManhours;

    // 対象月の実績工数合計を保持する
    private float monthlyTotalManhours;

    // 対象月に工数登録がある案件数を保持する
    private int monthlyProjectCount;

    // 対象月に稼働したメンバー数を保持する
    private int monthlyMemberCount;

    // 予算超過がある場合に1を保持する
    private int budgetAlertCount;

    // 月次予算に対する実績割合を保持する
    private int monthlyAchievementRate;

    // 案件別集計を保持する
    private List<ProjectSummaryDTO> projectSummaryList = new ArrayList<>();

    // メンバー別集計を保持する
    private List<MemberSummaryDTO> memberSummaryList = new ArrayList<>();

    // 工数明細を保持する
    private List<WorkLogDetailDTO> workLogDetailList = new ArrayList<>();

    public String getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(String targetMonth) {
        this.targetMonth = targetMonth;
    }

    public float getMonthlyBudgetManhours() {
        return monthlyBudgetManhours;
    }

    public void setMonthlyBudgetManhours(float monthlyBudgetManhours) {
        this.monthlyBudgetManhours = monthlyBudgetManhours;
    }

    public float getMonthlyTotalManhours() {
        return monthlyTotalManhours;
    }

    public void setMonthlyTotalManhours(float monthlyTotalManhours) {
        this.monthlyTotalManhours = monthlyTotalManhours;
    }

    public int getMonthlyProjectCount() {
        return monthlyProjectCount;
    }

    public void setMonthlyProjectCount(int monthlyProjectCount) {
        this.monthlyProjectCount = monthlyProjectCount;
    }

    public int getMonthlyMemberCount() {
        return monthlyMemberCount;
    }

    public void setMonthlyMemberCount(int monthlyMemberCount) {
        this.monthlyMemberCount = monthlyMemberCount;
    }

    public int getBudgetAlertCount() {
        return budgetAlertCount;
    }

    public void setBudgetAlertCount(int budgetAlertCount) {
        this.budgetAlertCount = budgetAlertCount;
    }

    public int getMonthlyAchievementRate() {
        return monthlyAchievementRate;
    }

    public void setMonthlyAchievementRate(int monthlyAchievementRate) {
        this.monthlyAchievementRate = monthlyAchievementRate;
    }

    public List<ProjectSummaryDTO> getProjectSummaryList() {
        return projectSummaryList;
    }

    public void setProjectSummaryList(List<ProjectSummaryDTO> projectSummaryList) {
        if (projectSummaryList == null) {
            this.projectSummaryList = new ArrayList<>();
            return;
        }
        this.projectSummaryList = projectSummaryList;
    }

    public List<MemberSummaryDTO> getMemberSummaryList() {
        return memberSummaryList;
    }

    public void setMemberSummaryList(List<MemberSummaryDTO> memberSummaryList) {
        if (memberSummaryList == null) {
            this.memberSummaryList = new ArrayList<>();
            return;
        }
        this.memberSummaryList = memberSummaryList;
    }

    public List<WorkLogDetailDTO> getWorkLogDetailList() {
        return workLogDetailList;
    }

    public void setWorkLogDetailList(List<WorkLogDetailDTO> workLogDetailList) {
        if (workLogDetailList == null) {
            this.workLogDetailList = new ArrayList<>();
            return;
        }
        this.workLogDetailList = workLogDetailList;
    }

    // static付きなのでMonthlySummaryDTOを作らなくても行データの型として使える
    public static class ProjectSummaryDTO {

        // 予算入力欄のnameに使う
        private int projectId;

        // 案件コードを表示する
        private String projectCode;

        // 案件名を表示する
        private String projectName;

        // 対象月の案件別実績工数を表示する
        private float actualManhours;

        // 画面で入力された案件別予算工数を表示する
        private float budgetManhours;

        // 予算に対する達成率を表示する
        private int achievementRate;

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

        public float getActualManhours() {
            return actualManhours;
        }

        public void setActualManhours(float actualManhours) {
            this.actualManhours = actualManhours;
        }

        public float getBudgetManhours() {
            return budgetManhours;
        }

        public void setBudgetManhours(float budgetManhours) {
            this.budgetManhours = budgetManhours;
        }

        public int getAchievementRate() {
            return achievementRate;
        }

        public void setAchievementRate(int achievementRate) {
            this.achievementRate = achievementRate;
        }
    }

    // static付きなのでメンバー別集計の行データとして使える
    public static class MemberSummaryDTO {

        // 担当者名を表示する
        private String memberName;

        // 担当者別の工数を表示する
        private float manhours;

        // 対象月に担当したタスク数を表示する
        private int taskCount;

        // 月間総工数に対する割合を表示する
        private int percentage;

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public float getManhours() {
            return manhours;
        }

        public void setManhours(float manhours) {
            this.manhours = manhours;
        }

        public int getTaskCount() {
            return taskCount;
        }

        public void setTaskCount(int taskCount) {
            this.taskCount = taskCount;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }
    }

    // static付きなので工数明細の行データとして使える
    public static class WorkLogDetailDTO {

        // 作業日をyyyy-MM-dd文字列で表示する
        private String workDate;

        // 案件名を表示する
        private String projectName;

        // タスク名を表示する
        private String taskName;

        // 担当者名を表示する
        private String userName;

        // 工数を表示する
        private float manhours;

        // 作業内容を表示する
        private String jobContents;

        public String getWorkDate() {
            return workDate;
        }

        public void setWorkDate(String workDate) {
            this.workDate = workDate;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public float getManhours() {
            return manhours;
        }

        public void setManhours(float manhours) {
            this.manhours = manhours;
        }

        public String getJobContents() {
            return jobContents;
        }

        public void setJobContents(String jobContents) {
            this.jobContents = jobContents;
        }
    }
}