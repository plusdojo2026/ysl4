package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 月次集計画面の集計結果を保持するDTO
 * サマリーカード、案件別集計、メンバー別集計、工数明細をまとめる
 */
public class MonthlySummaryDTO implements Serializable {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** 対象月 */
    private String targetMonth;

    /** 作業日 */
    private String workDate;

    /** 月次総工数 */
    private float monthlyTotalManHours;

    /** 工数登録がある案件数 */
    private int projectCount;

    /** 稼働メンバー数 */
    private int activeMemberCount;

    /** 超過案件数 */
    private int overrunProjectCount;

    /** 総工数 */
    private float totalManHours;

    /** 案件別集計一覧 */
    private List<ProjectSummaryDTO> projectSummaryList = new ArrayList<>();

    /** メンバー別集計一覧 */
    private List<MemberSummaryDTO> memberSummaryList = new ArrayList<>();

    /** 月次工数明細一覧 */
    private List<WorkLogDTO> monthlyWorkLogList = new ArrayList<>();

    public String getTargetMonth() {
        return targetMonth;
    }

    public void setTargetMonth(String targetMonth) {
        this.targetMonth = targetMonth;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public float getMonthlyTotalManHours() {
        return monthlyTotalManHours;
    }

    public void setMonthlyTotalManHours(float monthlyTotalManHours) {
        this.monthlyTotalManHours = monthlyTotalManHours;
    }

    public float getMonthlyTotalManhours() {
        return monthlyTotalManHours;
    }

    public void setMonthlyTotalManhours(float monthlyTotalManhours) {
        this.monthlyTotalManHours = monthlyTotalManhours;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }

    public int getActiveMemberCount() {
        return activeMemberCount;
    }

    public void setActiveMemberCount(int activeMemberCount) {
        this.activeMemberCount = activeMemberCount;
    }

    public int getOverrunProjectCount() {
        return overrunProjectCount;
    }

    public void setOverrunProjectCount(int overrunProjectCount) {
        this.overrunProjectCount = overrunProjectCount;
    }

    public float getTotalManHours() {
        return totalManHours;
    }

    public void setTotalManHours(float totalManHours) {
        this.totalManHours = totalManHours;
    }

    public List<ProjectSummaryDTO> getProjectSummaryList() {
        return projectSummaryList;
    }

    public void setProjectSummaryList(List<ProjectSummaryDTO> projectSummaryList) {
        this.projectSummaryList = projectSummaryList;
    }

    public List<MemberSummaryDTO> getMemberSummaryList() {
        return memberSummaryList;
    }

    public void setMemberSummaryList(List<MemberSummaryDTO> memberSummaryList) {
        this.memberSummaryList = memberSummaryList;
    }

    public List<WorkLogDTO> getMonthlyWorkLogList() {
        return monthlyWorkLogList;
    }

    public void setMonthlyWorkLogList(List<WorkLogDTO> monthlyWorkLogList) {
        this.monthlyWorkLogList = monthlyWorkLogList;
    }
}