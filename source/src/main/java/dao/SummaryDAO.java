package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.MemberSummaryDTO;
import model.ProjectSummaryDTO;
import model.WorkLogDTO;

/**
 * 月次集計用のDAO
 * 工数ログを基に月次合計、案件別、メンバー別、明細を取得
 */
public class SummaryDAO {

    /** DB接続 */
    private final Connection conn;

    /**
     * DB接続を受け取る
     *
     * @param conn DB接続
     */
    public SummaryDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * 指定月の総工数を取得
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 総工数
     * @throws SQLException SQLエラー
     */
    public Float selectMonthlyTotal(String targetMonth) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(man_hours), 0) AS monthly_total "
                + "FROM WorkLogs "
                + "WHERE DATE_FORMAT(work_date, '%Y-%m') = ?";

        return selectFloat(sql, targetMonth, "monthly_total");
    }

    /**
     * 指定月に工数登録がある案件数を取得
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 案件数
     * @throws SQLException SQLエラー
     */
    public int countMonthlyProjects(String targetMonth) throws SQLException {

        String sql =
                "SELECT COUNT(DISTINCT t.project_id) AS project_count "
                + "FROM WorkLogs wl "
                + "INNER JOIN Tasks t ON wl.task_id = t.task_id "
                + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ?";

        return selectInt(sql, targetMonth, "project_count");
    }

    /**
     * 指定月に工数登録したメンバー数を取得
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return メンバー数
     * @throws SQLException SQLエラー
     */
    public int countMonthlyMembers(String targetMonth) throws SQLException {

        String sql =
                "SELECT COUNT(DISTINCT user_id) AS member_count "
                + "FROM WorkLogs "
                + "WHERE DATE_FORMAT(work_date, '%Y-%m') = ?";

        return selectInt(sql, targetMonth, "member_count");
    }

    /**
     * 指定月の案件別工数集計を取得
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 案件別集計一覧
     * @throws SQLException SQLエラー
     */
    public List<ProjectSummaryDTO> selectProjectSummary(String targetMonth) throws SQLException {

        List<ProjectSummaryDTO> projectSummaryList = new ArrayList<>();

        String sql =
                "SELECT "
                + "p.project_code, "
                + "p.project_name, "
                + "p.estimated_manhours, "
                + "COALESCE(SUM(wl.man_hours), 0) AS actual_manhours "
                + "FROM Projects p "
                + "INNER JOIN Tasks t ON p.project_id = t.project_id "
                + "INNER JOIN WorkLogs wl ON t.task_id = wl.task_id "
                + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ? "
                + "GROUP BY p.project_id, p.project_code, p.project_name, p.estimated_manhours "
                + "ORDER BY p.project_code ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, targetMonth);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProjectSummaryDTO dto = new ProjectSummaryDTO();

                    dto.setProjectCode(rs.getString("project_code"));
                    dto.setProjectName(rs.getString("project_name"));
                    dto.setEstimatedManhours(rs.getFloat("estimated_manhours"));
                    dto.setActualManhours(rs.getFloat("actual_manhours"));

                    projectSummaryList.add(dto);
                }
            }
        }

        return projectSummaryList;
    }

    /**
     * 指定月のメンバー別工数集計を取得
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return メンバー別集計一覧
     * @throws SQLException SQLエラー
     */
    public List<MemberSummaryDTO> selectMemberSummary(String targetMonth) throws SQLException {

        List<MemberSummaryDTO> memberSummaryList = new ArrayList<>();

        String sql =
                "SELECT "
                + "u.user_id, "
                + "u.name AS user_name, "
                + "COALESCE(SUM(wl.man_hours), 0) AS man_hours, "
                + "COUNT(DISTINCT wl.task_id) AS task_count, "
                + "COALESCE(( "
                + "    SELECT SUM(t2.estimated_manhours) "
                + "    FROM Tasks t2 "
                + "    WHERE t2.manager_id = u.user_id "
                + "), 0) AS estimated_manhours "
                + "FROM Users u "
                + "INNER JOIN WorkLogs wl ON u.user_id = wl.user_id "
                + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ? "
                + "GROUP BY u.user_id, u.name "
                + "ORDER BY u.name ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, targetMonth);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MemberSummaryDTO dto = new MemberSummaryDTO();

                    dto.setUserId(rs.getInt("user_id"));
                    dto.setUserName(rs.getString("user_name"));
                    dto.setManHours(rs.getFloat("man_hours"));
                    dto.setActualManHours(rs.getFloat("man_hours"));
                    dto.setEstimatedManhours(rs.getFloat("estimated_manhours"));
                    dto.setTaskCount(rs.getInt("task_count"));

                    memberSummaryList.add(dto);
                }
            }
        }

        return memberSummaryList;
    }

    /**
     * 指定月の工数明細を取得
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 工数明細一覧
     * @throws SQLException SQLエラー
     */
    public List<WorkLogDTO> selectMonthlyWorkLogs(String targetMonth) throws SQLException {

        List<WorkLogDTO> workLogList = new ArrayList<>();

        String sql =
                "SELECT "
                + "wl.work_logs_id AS work_logs_id, "
                + "wl.task_id, "
                + "t.task_name, "
                + "t.project_id, "
                + "p.project_name, "
                + "wl.user_id, "
                + "u.name AS user_name, "
                + "DATE_FORMAT(wl.work_date, '%Y-%m-%d') AS work_date, "
                + "wl.man_hours, "
                + "wl.job_contents, "
                + "DATE_FORMAT(wl.c_at, '%Y-%m-%d %H:%i:%s') AS c_at, "
                + "DATE_FORMAT(wl.u_at, '%Y-%m-%d %H:%i:%s') AS u_at "
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
                    workLogList.add(setToWorkLogDTO(rs));
                }
            }
        }

        return workLogList;
    }

    /**
     * Floatの1項目を取得
     *
     * @param sql SQL
     * @param targetMonth 対象月
     * @param columnName 取得列名
     * @return 取得値
     * @throws SQLException SQLエラー
     */
    private Float selectFloat(
            String sql,
            String targetMonth,
            String columnName) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, targetMonth);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat(columnName);
                }
            }
        }

        return 0f;
    }

    /**
     * intの1項目を取得する。
     *
     * @param sql SQL
     * @param targetMonth 対象月
     * @param columnName 取得列名
     * @return 取得値
     * @throws SQLException SQLエラー
     */
    private int selectInt(
            String sql,
            String targetMonth,
            String columnName) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, targetMonth);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(columnName);
                }
            }
        }

        return 0;
    }

    /**
     * ResultSetの1行をWorkLogDTOへ変換
     *
     * @param rs SQL取得結果
     * @return 工数ログDTO
     * @throws SQLException SQLエラー
     */
    private WorkLogDTO setToWorkLogDTO(ResultSet rs) throws SQLException {

        WorkLogDTO dto = new WorkLogDTO();

        dto.setWorkLogsId(rs.getInt("work_logs_id"));
        dto.setTaskId(rs.getInt("task_id"));
        dto.setTaskName(rs.getString("task_name"));
        dto.setProjectId(rs.getInt("project_id"));
        dto.setProjectName(rs.getString("project_name"));
        dto.setUserId(rs.getInt("user_id"));
        dto.setUserName(rs.getString("user_name"));
        dto.setWorkDate(rs.getString("work_date"));
        dto.setManHours(rs.getFloat("man_hours"));
        dto.setJobContents(rs.getString("job_contents"));
        dto.setCreatedAt(rs.getString("c_at"));
        dto.setUpdatedAt(rs.getString("u_at"));

        return dto;
    }
}