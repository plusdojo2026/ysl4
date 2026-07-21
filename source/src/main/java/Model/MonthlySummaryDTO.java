package Model;

import java.util.ArrayList;
import java.util.List;

public class MonthlySummaryDTO {

    // 集計対象月をyyyy-MM形式で保持する
    private String targetMonth;

    // 画面で手入力された月次予算工数を保持する
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
    private List<WorkLogDTO> monthlyWorkLogList = new ArrayList<>();

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

    public List<WorkLogDTO> getMonthlyWorkLogList() {
        return monthlyWorkLogList;
    }

    public void setMonthlyWorkLogList(List<WorkLogDTO> monthlyWorkLogList) {
        if (monthlyWorkLogList == null) {
            this.monthlyWorkLogList = new ArrayList<>();
            return;
        }
        this.monthlyWorkLogList = monthlyWorkLogList;
    }

    public static class ProjectSummaryDTO {

        // static内部クラスなのでMonthlySummaryDTOの中で案件別行をまとめられる
        // ファイルを増やさずに案件別集計の1行を表す

        private int projectId;
        private String projectCode;
        private String projectName;
        private float actualManhours;
        private float budgetManhours;
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

    public static class MemberSummaryDTO {

        // static内部クラスなのでMonthlySummaryDTOの中でメンバー別行をまとめられる
        // ファイルを増やさずにメンバー別集計の1行を表す

        private String memberName;
        private float manhours;
        private int taskCount;
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
}