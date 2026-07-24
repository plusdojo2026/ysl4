package service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import dao.ProjectsDAO;
import dao.TaskDAO;
import dao.WorkLogDAO;
import model.DashboardDTO;
import model.ProjectsDTO;
import model.TaskDTO;
import model.WorkLogDTO;

/**
 * ホーム画面の業務処理を担当するService
 * ログインユーザーに紐づくタスク、案件、工数情報をまとめる
 */
public class DashboardService extends DBAccess {

    /** ホーム画面に表示する最新工数ログ件数 */
    private static final int RECENT_WORK_LOG_LIMIT = 5;

    /**
     * ホーム画面表示用データを取得する
     * 担当タスク、期限超過タスク、進行中案件、今月工数、最新工数ログをまとめる
     * @param userId ログインユーザーID
     * @return ホーム画面表示用DTO
     */
    public DashboardDTO getDashboard(int userId) {

        // 戻り値用DTOを作成する
        DashboardDTO dashboardDto = new DashboardDTO();

        // ユーザーIDが不正な場合は空DTOを返す
        if (userId <= 0) {
            return dashboardDto;
        }

        try {
            // DB接続を開始する
            access();

            // DAOを作成する
            TaskDAO taskDao = new TaskDAO(conn);
            ProjectsDAO projectsDao = new ProjectsDAO(conn);
            WorkLogDAO workLogDao = new WorkLogDAO(conn);

            // ログインユーザーの未完了担当タスクを取得する
            List<TaskDTO> assignedTaskList = taskDao.selectByManagerId(userId);

            // ログインユーザーの期限超過タスクを取得する
            List<TaskDTO> overdueTaskList = taskDao.selectOverdueByManagerId(userId);

            // 進行中案件を取得する
            List<ProjectsDTO> inProgressProjectList = projectsDao.selectInProgress();

            // ログインユーザーの工数ログを取得する
            List<WorkLogDTO> workLogList = workLogDao.selectByUserId(userId);

            // 最新工数ログを作成する
            List<WorkLogDTO> recentWorkLogList = createRecentWorkLogList(workLogList);

            // 今月の合計工数を計算する
            float monthlyManHours = calculateMonthlyManHours(workLogList);

            // 今月の工数ログ件数を計算する
            int monthlyWorkLogCount = countMonthlyWorkLogs(workLogList);

            // 担当タスク一覧を設定する
            dashboardDto.setAssignedTaskList(assignedTaskList);

            // 期限超過タスク一覧を設定する
            dashboardDto.setOverdueTaskList(overdueTaskList);

            // 進行中案件一覧を設定する
            dashboardDto.setInProgressProjectList(inProgressProjectList);

            // 最新工数ログ一覧を設定する
            dashboardDto.setRecentWorkLogList(recentWorkLogList);

            // 今月の合計工数を設定する
            dashboardDto.setMonthlyMyManhours(monthlyManHours);

            // 担当タスク件数を設定する
            dashboardDto.setAssignedTaskCount(assignedTaskList.size());

            // 期限超過タスク件数を設定する
            dashboardDto.setOverdueTaskCount(overdueTaskList.size());

            // 進行中案件件数を設定する
            dashboardDto.setInProgressProjectCount(inProgressProjectList.size());

            // 今月の工数ログ件数を設定する
            dashboardDto.setMonthlyWorkLogCount(monthlyWorkLogCount);

            /*
             * 自身の予定を取得(追加機能)
             */
            // dashboardDto.setTodayScheduleList(todayScheduleList);

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

        return dashboardDto;
    }

    /**
     * 工数ログ一覧から最新表示分だけ取り出す
     * WorkLogDAO側で新しい順に取得している前提
     * @param workLogList 工数ログ一覧
     * @return 最新工数ログ一覧
     */
    private List<WorkLogDTO> createRecentWorkLogList(List<WorkLogDTO> workLogList) {

        // 表示用一覧を作成する
        List<WorkLogDTO> recentWorkLogList = new ArrayList<>();

        // nullの場合は空Listを返す
        if (workLogList == null) {
            return recentWorkLogList;
        }

        // 表示件数を超えない範囲で一覧に追加する
        for (int i = 0; i < workLogList.size() && i < RECENT_WORK_LOG_LIMIT; i++) {
            recentWorkLogList.add(workLogList.get(i));
        }

        return recentWorkLogList;
    }

    /**
     * 工数ログ一覧から今月の合計工数を計算する
     * workDateはyyyy-MM-dd形式のString前提
     * @param workLogList 工数ログ一覧
     * @return 今月の合計工数
     */
    private float calculateMonthlyManHours(List<WorkLogDTO> workLogList) {

        // nullの場合は0を返す
        if (workLogList == null) {
            return 0;
        }

        // 今月を取得する
        YearMonth currentMonth = YearMonth.now();

        // 合計工数を初期化する
        float totalManHours = 0;

        // 工数ログを1件ずつ確認する
        for (WorkLogDTO workLogDto : workLogList) {

            // 今月以外の工数ログは集計しない
            if (!isSameMonth(workLogDto.getWorkDate(), currentMonth)) {
                continue;
            }

            // 今月分の工数を加算する
            totalManHours += workLogDto.getManHours();
        }

        return totalManHours;
    }

    /**
     * 工数ログ一覧から今月の登録件数を数える
     * @param workLogList 工数ログ一覧
     * @return 今月の工数ログ件数
     */
    private int countMonthlyWorkLogs(List<WorkLogDTO> workLogList) {

        // nullの場合は0を返す
        if (workLogList == null) {
            return 0;
        }

        // 今月を取得する
        YearMonth currentMonth = YearMonth.now();

        // 件数を初期化する
        int count = 0;

        // 工数ログを1件ずつ確認する
        for (WorkLogDTO workLogDto : workLogList) {

            // 今月の工数ログだけ件数に含める
            if (isSameMonth(workLogDto.getWorkDate(), currentMonth)) {
                count++;
            }
        }

        return count;
    }

    /**
     * 指定日付が対象月と同じ月か確認する
     * 日付が不正な場合はfalseを返す
     * @param workDate 作業日
     * @param targetMonth 対象月
     * @return 同じ月ならtrue
     */
    private boolean isSameMonth(String workDate, YearMonth targetMonth) {

        // 未入力の場合は対象外にする
        if (!hasText(workDate)) {
            return false;
        }

        try {
            // LocalDateへ変換する
            LocalDate date = LocalDate.parse(workDate);

            // 年月だけを比較する
            return YearMonth.from(date).equals(targetMonth);

        } catch (RuntimeException e) {
            // 日付形式が不正な場合は対象外にする
            return false;
        }
    }

    /**
     * 文字列が入力されているか確認する
     * nullと空文字を未入力として扱う
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}