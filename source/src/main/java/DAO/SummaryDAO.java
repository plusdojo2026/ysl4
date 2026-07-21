package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.DashboardDTO;
import Model.MonthlySummaryDTO;

public class SummaryDAO {

    // Serviceから渡されたDB接続を保持する
    private final Connection conn;

    public SummaryDAO(Connection conn) {
        this.conn = conn;
    }

    public DashboardDTO selectDashboardByUserId(int userId, String targetMonth) throws SQLException {

        DashboardDTO dto = new DashboardDTO();

        // ホーム画面のカード表示に使う数値を取得する
        dto.setInProgressProjectCount(selectInt("SELECT COUNT(*) FROM Projects WHERE status = '進行中'"));
        dto.setAssignedTaskCount(selectInt(
                "SELECT COUNT(*) FROM Tasks WHERE manager_id = ? AND status <> '完了'",
                userId));
        dto.setOverdueTaskCount(selectInt(
                "SELECT COUNT(*) FROM Tasks WHERE manager_id = ? AND status <> '完了' AND due_date < CURDATE()",
                userId));
        dto.setMonthlyMyManhours(selectFloat(
                "SELECT COALESCE(SUM(man_hours), 0) FROM WorkLogs WHERE user_id = ? AND DATE_FORMAT(work_date, '%Y-%m') = ?",
                userId, targetMonth));
        dto.setMyTaskProgressRate(selectInt(
                "SELECT COALESCE(ROUND(AVG(progress)), 0) FROM Tasks WHERE manager_id = ?",
                userId));
        dto.setAllTaskProgressRate(selectInt(
                "SELECT COALESCE(ROUND(AVG(progress)), 0) FROM Tasks"));

        // 月次予算は月次集計画面で入力するためホームでは0を入れる
        dto.setMonthlyWorkAchievementRate(0);

        // 追加機能用の予定表示
        // dto.setTodayScheduleCount(0);

        // ホーム画面の一覧表示を取得する
        dto.setAssignedTaskList(selectAssignedTaskList(userId));
        dto.setInProgressProjectList(selectInProgressProjectList());

        return dto;
    }

    private List<DashboardDTO.AssignedTaskDTO> selectAssignedTaskList(int userId) throws SQLException {

        List<DashboardDTO.AssignedTaskDTO> list = new ArrayList<>();

        String sql = "SELECT "
                + "t.task_id, "
                + "t.task_name, "
                + "p.project_name, "
                + "DATE_FORMAT(t.due_date, '%Y-%m-%d') AS due_date, "
                + "t.priority, "
                + "t.progress, "
                + "t.status "
                + "FROM Tasks t "
                + "INNER JOIN Projects p ON t.project_id = p.project_id "
                + "WHERE t.manager_id = ? "
                + "AND t.status <> '完了' "
                + "ORDER BY t.due_date ASC, t.task_id DESC "
                + "LIMIT 5";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DashboardDTO.AssignedTaskDTO dto = new DashboardDTO.AssignedTaskDTO();

                    // ResultSetの値をDTOへ詰め替える
                    dto.setTaskId(rs.getInt("task_id"));
                    dto.setTaskName(rs.getString("task_name"));
                    dto.setProjectName(rs.getString("project_name"));
                    dto.setDueDate(rs.getString("due_date"));
                    dto.setPriority(rs.getString("priority"));
                    dto.setProgress(rs.getInt("progress"));
                    dto.setStatus(rs.getString("status"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    private List<DashboardDTO.InProgressProjectDTO> selectInProgressProjectList() throws SQLException {

        List<DashboardDTO.InProgressProjectDTO> list = new ArrayList<>();

        String sql = "SELECT "
                + "p.project_id, "
                + "p.project_name, "
                + "p.status, "
                + "COALESCE(ROUND(AVG(t.progress)), 0) AS progress "
                + "FROM Projects p "
                + "LEFT JOIN Tasks t ON p.project_id = t.project_id "
                + "WHERE p.status = '進行中' "
                + "GROUP BY p.project_id, p.project_name, p.status "
                + "ORDER BY p.u_at DESC, p.project_id DESC "
                + "LIMIT 5";

        try (PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                DashboardDTO.InProgressProjectDTO dto = new DashboardDTO.InProgressProjectDTO();

                // ResultSetの値をDTOへ詰め替える
                dto.setProjectId(rs.getInt("project_id"));
                dto.setProjectName(rs.getString("project_name"));
                dto.setStatus(rs.getString("status"));
                dto.setProgress(rs.getInt("progress"));

                list.add(dto);
            }
        }

        return list;
    }

    public MonthlySummaryDTO selectMonthlySummary(String targetMonth, float monthlyBudgetManhours)
            throws SQLException {

        MonthlySummaryDTO dto = new MonthlySummaryDTO();
        dto.setTargetMonth(targetMonth);
        dto.setMonthlyBudgetManhours(monthlyBudgetManhours);

        // 対象月の実績工数を取得する
        float total = selectFloat(
                "SELECT COALESCE(SUM(man_hours), 0) FROM WorkLogs WHERE DATE_FORMAT(work_date, '%Y-%m') = ?",
                targetMonth);

        dto.setMonthlyTotalManhours(total);
        dto.setMonthlyProjectCount(selectInt(
                "SELECT COUNT(DISTINCT t.project_id) "
                        + "FROM WorkLogs wl "
                        + "INNER JOIN Tasks t ON wl.task_id = t.task_id "
                        + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ?",
                targetMonth));
        dto.setMonthlyMemberCount(selectInt(
                "SELECT COUNT(DISTINCT user_id) FROM WorkLogs WHERE DATE_FORMAT(work_date, '%Y-%m') = ?",
                targetMonth));

        // 月次予算は画面入力値で判定する
        dto.setBudgetAlertCount(total > monthlyBudgetManhours && monthlyBudgetManhours > 0 ? 1 : 0);
        dto.setMonthlyAchievementRate(calcRate(total, monthlyBudgetManhours));

        return dto;
    }

    public List<MonthlySummaryDTO.ProjectSummaryDTO> selectProjectSummary(String targetMonth) throws SQLException {

        List<MonthlySummaryDTO.ProjectSummaryDTO> list = new ArrayList<>();

        String sql = "SELECT "
                + "p.project_id, "
                + "p.project_code, "
                + "p.project_name, "
                + "COALESCE(SUM(wl.man_hours), 0) AS actual_manhours "
                + "FROM Projects p "
                + "INNER JOIN Tasks t ON p.project_id = t.project_id "
                + "INNER JOIN WorkLogs wl ON t.task_id = wl.task_id "
                + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ? "
                + "GROUP BY p.project_id, p.project_code, p.project_name "
                + "ORDER BY p.project_code ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targetMonth);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonthlySummaryDTO.ProjectSummaryDTO dto = new MonthlySummaryDTO.ProjectSummaryDTO();

                    // 予算工数はDBから取らずServiceで画面入力値を入れる
                    dto.setProjectId(rs.getInt("project_id"));
                    dto.setProjectCode(rs.getString("project_code"));
                    dto.setProjectName(rs.getString("project_name"));
                    dto.setActualManhours(rs.getFloat("actual_manhours"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<MonthlySummaryDTO.MemberSummaryDTO> selectMemberSummary(String targetMonth) throws SQLException {

        List<MonthlySummaryDTO.MemberSummaryDTO> list = new ArrayList<>();

        String sql = "SELECT "
                + "u.name AS member_name, "
                + "COALESCE(SUM(wl.man_hours), 0) AS manhours, "
                + "COUNT(DISTINCT wl.task_id) AS task_count "
                + "FROM Users u "
                + "INNER JOIN WorkLogs wl ON u.user_id = wl.user_id "
                + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ? "
                + "GROUP BY u.user_id, u.name "
                + "ORDER BY manhours DESC, u.user_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targetMonth);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonthlySummaryDTO.MemberSummaryDTO dto = new MonthlySummaryDTO.MemberSummaryDTO();

                    // ResultSetの値をDTOへ詰め替える
                    dto.setMemberName(rs.getString("member_name"));
                    dto.setManhours(rs.getFloat("manhours"));
                    dto.setTaskCount(rs.getInt("task_count"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<MonthlySummaryDTO.WorkLogDetailDTO> selectMonthlyWorkLogs(String targetMonth) throws SQLException {

        List<MonthlySummaryDTO.WorkLogDetailDTO> list = new ArrayList<>();

        String sql = "SELECT "
                + "DATE_FORMAT(wl.work_date, '%Y-%m-%d') AS work_date, "
                + "p.project_name, "
                + "t.task_name, "
                + "u.name AS user_name, "
                + "wl.man_hours, "
                + "wl.job_contents "
                + "FROM WorkLogs wl "
                + "INNER JOIN Tasks t ON wl.task_id = t.task_id "
                + "INNER JOIN Projects p ON t.project_id = p.project_id "
                + "INNER JOIN Users u ON wl.user_id = u.user_id "
                + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ? "
                + "ORDER BY wl.work_date ASC, p.project_code ASC, t.task_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targetMonth);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonthlySummaryDTO.WorkLogDetailDTO dto = new MonthlySummaryDTO.WorkLogDetailDTO();

                    // 日付はStringとしてDTOへ入れる
                    dto.setWorkDate(rs.getString("work_date"));
                    dto.setProjectName(rs.getString("project_name"));
                    dto.setTaskName(rs.getString("task_name"));
                    dto.setUserName(rs.getString("user_name"));
                    dto.setManhours(rs.getFloat("man_hours"));
                    dto.setJobContents(rs.getString("job_contents"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    private int selectInt(String sql, Object... params) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    private float selectFloat(String sql, Object... params) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat(1);
                }
            }
        }

        return 0f;
    }

    private void setParams(PreparedStatement ps, Object... params) throws SQLException {

        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }

    private static int calcRate(float actual, float budget) {

        // staticなのでSummaryDAOを作らず計算だけ使える
        if (budget <= 0f) {
            return 0;
        }

        return Math.round((actual / budget) * 100f);
    }
}