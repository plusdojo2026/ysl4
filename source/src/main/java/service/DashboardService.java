package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.ProjectsDAO;
import dao.TaskDAO;
import dao.WorkLogDAO;
import model.DashboardDTO;
import model.ProjectsDTO;
import model.TaskDTO;

public class DashboardService extends DBAccess {

    public DashboardDTO getDashboard(int userId) {

        // ホーム画面に表示する値をまとめて返す
        DashboardDTO dto = new DashboardDTO();

        if (userId <= 0) {
            return dto;
        }

        try {
            access();

            ProjectsDAO projectDao = new ProjectsDAO(conn);
            TaskDAO taskDao = new TaskDAO(conn);
            WorkLogDAO workLogDao = new WorkLogDAO(conn);

            // ホーム画面のカードに使う数値を設定する
            dto.setInProgressProjectCount(projectDao.countInProgressProjects());
            dto.setAssignedTaskCount(taskDao.countAssignedTasks(userId));
            dto.setOverdueTaskCount(taskDao.countOverdueTasks(userId));
            dto.setMonthlyMyManhours(workLogDao.sumByUserIdAndMonth(userId, currentMonth()));
            dto.setMyTaskProgressRate(taskDao.averageProgressByUserId(userId));
            dto.setAllTaskProgressRate(taskDao.averageProgressAll());

            // 月次予算は月次集計画面で入力するためホームでは0にする
            dto.setMonthlyWorkAchievementRate(0);

            // ホーム画面下部の一覧に使うデータを設定する
            dto.setInProgressProjectList(projectDao.selectInProgressProjects());
            dto.setAssignedTaskList(taskDao.selectAssignedTasks(userId));

            // 追加機能用の予定表示
            // dto.setTodayScheduleCount(scheduleDao.countTodaySchedules(userId));

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return dto;
    }

    public List<ProjectsDTO> selectInProgressProjects() {

        // クラス図準拠のpublicメソッド
        // 進行中案件一覧だけが必要な場合に使う
        List<ProjectsDTO> list = new ArrayList<>();

        try {
            access();
            ProjectsDAO dao = new ProjectsDAO(conn);
            list = dao.selectInProgressProjects();
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return list;
    }

    public List<TaskDTO> selectAssignedTasks(int userId) {

        // 自分の担当タスク一覧だけが必要な場合に使う
        List<TaskDTO> list = new ArrayList<>();

        if (userId <= 0) {
            return list;
        }

        try {
            access();
            TaskDAO dao = new TaskDAO(conn);
            list = dao.selectAssignedTasks(userId);
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return list;
    }

    private static String currentMonth() {

        // yyyy-MM形式にしてSQL検索条件へ渡す
        return LocalDate.now().toString().substring(0, 7);
    }
}