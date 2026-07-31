package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**

◦ ダッシュボード画面に表示する情報をまとめる.
◦ 件数カードと一覧表示用のListを保持する.
 */
public class DashboardDTO implements Serializable {

    /** シリアライズ用ID */
    private static final long serialVersionUID = 1L;

    /** 今月工数 */
    private float thisMonthWorkHours;

    /** 進行中案件数 */
    private int inProgressProjectCount;

    /** 担当タスク数 */
    private int assignedTaskCount;

    /** 期限超過タスク数 */
    private int overdueTaskCount;

    /** 進行中案件一覧 */
    private List<ProjectsDTO> inProgressProjectList = new ArrayList<>();

    /** 担当タスク一覧 */
    private List<TaskDTO> assignedTaskList = new ArrayList<>();

    
    public float getThisMonthWorkHours() {
        return thisMonthWorkHours;
    }

    public void setThisMonthWorkHours(float thisMonthWorkHours) {
        this.thisMonthWorkHours = thisMonthWorkHours;
    }

    public float getMonthlyMyManhours() {
        return thisMonthWorkHours;
    }

    public void setMonthlyMyManhours(float monthlyMyManhours) {
        this.thisMonthWorkHours = monthlyMyManhours;
    }

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

    public int getMyTaskCount() {
        return assignedTaskCount;
    }

    public void setMyTaskCount(int myTaskCount) {
        this.assignedTaskCount = myTaskCount;
    }

    public int getActiveProjectCount() {
        return inProgressProjectCount;
    }

    public void setActiveProjectCount(int activeProjectCount) {
        this.inProgressProjectCount = activeProjectCount;
    }
}