package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ダッシュボード画面に表示する情報をまとめるDTO
 * 件数カードと一覧表示用のListを保持する
 */
public class DashboardDTO implements Serializable {

    /** シリアライズ用ID */
    private static final long serialVersionUID = 1L;

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

    /**
     * 進行中案件数を取得する
     * @return 進行中案件数
     */
    public int getInProgressProjectCount() {
        return inProgressProjectCount;
    }

    /**
     * 進行中案件数を設定する
     * @param inProgressProjectCount 進行中案件数
     */
    public void setInProgressProjectCount(int inProgressProjectCount) {
        this.inProgressProjectCount = inProgressProjectCount;
    }

    /**
     * 担当タスク数を取得する
     * @return 担当タスク数
     */
    public int getAssignedTaskCount() {
        return assignedTaskCount;
    }

    /**
     * 担当タスク数を設定する
     * @param assignedTaskCount 担当タスク数
     */
    public void setAssignedTaskCount(int assignedTaskCount) {
        this.assignedTaskCount = assignedTaskCount;
    }

    /**
     * 期限超過タスク数を取得する
     * @return 期限超過タスク数
     */
    public int getOverdueTaskCount() {
        return overdueTaskCount;
    }

    /**
     * 期限超過タスク数を設定する
     * @param overdueTaskCount 期限超過タスク数
     */
    public void setOverdueTaskCount(int overdueTaskCount) {
        this.overdueTaskCount = overdueTaskCount;
    }

    /**
     * 進行中案件一覧を取得する
     * @return 進行中案件一覧
     */
    public List<ProjectsDTO> getInProgressProjectList() {
        return inProgressProjectList;
    }

    /**
     * 進行中案件一覧を設定する
     * nullの場合は空Listを設定する
     * @param inProgressProjectList 進行中案件一覧
     */
    public void setInProgressProjectList(List<ProjectsDTO> inProgressProjectList) {
        if (inProgressProjectList == null) {
            this.inProgressProjectList = new ArrayList<>();
            return;
        }

        this.inProgressProjectList = inProgressProjectList;
    }

    /**
     * 担当タスク一覧を取得する
     * @return 担当タスク一覧
     */
    public List<TaskDTO> getAssignedTaskList() {
        return assignedTaskList;
    }

    /**
     * 担当タスク一覧を設定する
     * nullの場合は空Listを設定する
     * @param assignedTaskList 担当タスク一覧
     */
    public void setAssignedTaskList(List<TaskDTO> assignedTaskList) {
        if (assignedTaskList == null) {
            this.assignedTaskList = new ArrayList<>();
            return;
        }

        this.assignedTaskList = assignedTaskList;
    }

    /**
     * クラス図のmyTaskCount表記に対応する
     * @return 担当タスク数
     */
    public int getMyTaskCount() {
        return assignedTaskCount;
    }

    /**
     * クラス図のmyTaskCount表記に対応する
     * @param myTaskCount 担当タスク数
     */
    public void setMyTaskCount(int myTaskCount) {
        this.assignedTaskCount = myTaskCount;
    }

    /**
     * クラス図のactiveProjectCount表記に対応する
     * @return 進行中案件数
     */
    public int getActiveProjectCount() {
        return inProgressProjectCount;
    }

    /**
     * クラス図のactiveProjectCount表記に対応する
     * @param activeProjectCount 進行中案件数
     */
    public void setActiveProjectCount(int activeProjectCount) {
        this.inProgressProjectCount = activeProjectCount;
    }
}