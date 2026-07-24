package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ProjectsDAO;
import dao.TaskDAO;
import model.DashboardDTO;
import model.ProjectsDTO;
import model.TaskDTO;

/**
 * ダッシュボード画面の業務処理を担当するService
 * 件数カードと一覧表示に必要な情報を取得する
 */
public class DashboardService extends DBAccess {

    /**
     * ダッシュボード表示用の件数と一覧をまとめて取得する
     * @param userId ログインユーザーID
     * @return ダッシュボード表示用DTO
     */
    public DashboardDTO getDashboard(int userId) {

        // 戻り値用DTOを作成する
        DashboardDTO dashboardDto = new DashboardDTO();

        // ユーザーIDが不正な場合は空DTOを返す
        if (userId <= 0) {
            return dashboardDto;
        }

        // 進行中案件数を設定する
        dashboardDto.setInProgressProjectCount(countInProgressProjects());

        // 担当タスク数を設定する
        dashboardDto.setAssignedTaskCount(countAssignedTasks(userId));

        // 期限超過タスク数を設定する
        dashboardDto.setOverdueTaskCount(countOverdueTasks(userId));

        // 進行中案件一覧を設定する
        dashboardDto.setInProgressProjectList(selectInProgressProjects());

        // 担当タスク一覧を設定する
        dashboardDto.setAssignedTaskList(selectAssignedTasks(userId));

        return dashboardDto;
    }

    /**
     * 進行中案件数を取得する
     * @return 進行中案件数
     */
    public int countInProgressProjects() {

        int count = 0;

        try {
            // DB接続を開始する
            access();

            // DAOから進行中案件数を取得する
            ProjectsDAO projectDao = new ProjectsDAO(conn);
            count = projectDao.countInProgressProjects();

            // 参照処理の完了としてcommitする
            commit();

        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();

        } finally {
            // DB接続を閉じる
            close();
        }

        return count;
    }

    /**
     * ログインユーザーの担当タスク数を取得する
     * @param userId ログインユーザーID
     * @return 担当タスク数
     */
    public int countAssignedTasks(int userId) {

        // ユーザーIDが不正な場合は0を返す
        if (userId <= 0) {
            return 0;
        }

        int count = 0;

        try {
            // DB接続を開始する
            access();

            // DAOから担当タスク数を取得する
            TaskDAO taskDao = new TaskDAO(conn);
            count = taskDao.countAssignedTasks(userId);

            // 参照処理の完了としてcommitする
            commit();

        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();

        } finally {
            // DB接続を閉じる
            close();
        }

        return count;
    }

    /**
     * ログインユーザーの期限超過タスク数を取得する
     * @param userId ログインユーザーID
     * @return 期限超過タスク数
     */
    public int countOverdueTasks(int userId) {

        // ユーザーIDが不正な場合は0を返す
        if (userId <= 0) {
            return 0;
        }

        int count = 0;

        try {
            // DB接続を開始する
            access();

            // DAOから期限超過タスク数を取得する
            TaskDAO taskDao = new TaskDAO(conn);
            count = taskDao.countOverdueTasks(userId);

            // 参照処理の完了としてcommitする
            commit();

        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();

        } finally {
            // DB接続を閉じる
            close();
        }

        return count;
    }

    /**
     * 進行中案件一覧を取得する
     * @return 進行中案件一覧
     */
    public List<ProjectsDTO> selectInProgressProjects() {

        List<ProjectsDTO> projectList = new ArrayList<>();

        try {
            // DB接続を開始する
            access();

            // DAOから進行中案件一覧を取得する
            ProjectsDAO projectDao = new ProjectsDAO(conn);
            projectList = projectDao.selectInProgressProjects();

            // 参照処理の完了としてcommitする
            commit();

        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();

        } finally {
            // DB接続を閉じる
            close();
        }

        return projectList;
    }

    /**
     * ログインユーザーの担当タスク一覧を取得する
     * @param userId ログインユーザーID
     * @return 担当タスク一覧
     */
    public List<TaskDTO> selectAssignedTasks(int userId) {

        List<TaskDTO> taskList = new ArrayList<>();

        // ユーザーIDが不正な場合は空Listを返す
        if (userId <= 0) {
            return taskList;
        }

        try {
            // DB接続を開始する
            access();

            // DAOから担当タスク一覧を取得する
            TaskDAO taskDao = new TaskDAO(conn);
            taskList = taskDao.selectAssignedTasks(userId);

            // 参照処理の完了としてcommitする
            commit();

        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();

        } finally {
            // DB接続を閉じる
            close();
        }

        return taskList;
    }
}