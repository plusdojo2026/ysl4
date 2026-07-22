package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.MonthlySummaryDTO;
import Model.WorkLogDTO;

public class SummaryDAO {

    // Serviceから渡されたDB接続を保持する
    private final Connection conn;

    public SummaryDAO(Connection conn) {
        this.conn = conn;
    }

    public float selectMonthlyTotal(String targetMonth) throws SQLException {

        // 指定月の工数合計だけを返す
        String sql = "SELECT COALESCE(SUM(man_hours), 0) "
                + "FROM WorkLogs "
                + "WHERE DATE_FORMAT(work_date, '%Y-%m') = ?";

        return selectFloat(sql, targetMonth);
    }

    public int countMonthlyProjects(String targetMonth) throws SQLException {

        // 指定月に工数登録がある案件数を返す
        String sql = "SELECT COUNT(DISTINCT t.project_id) "
                + "FROM WorkLogs wl "
                + "INNER JOIN Tasks t ON wl.task_id = t.task_id "
                + "WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ?";

        return selectInt(sql, targetMonth);
    }

    public int countMonthlyMembers(String targetMonth) throws SQLException {

        // 指定月に作業したメンバー数を返す
        String sql = "SELECT COUNT(DISTINCT user_id) "
                + "FROM WorkLogs "
                + "WHERE DATE_FORMAT(work_date, '%Y-%m') = ?";

        return selectInt(sql, targetMonth);
    }

    public List<MonthlySummaryDTO.ProjectSummaryDTO> selectProjectSummary(String targetMonth) throws SQLException {

        // 案件別の実績工数を返す
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

                    // DBの1行を案件別集計DTOへ詰める
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

        // メンバー別の工数と担当タスク数を返す
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

                    // DBの1行をメンバー別集計DTOへ詰める
                    dto.setMemberName(rs.getString("member_name"));
                    dto.setManhours(rs.getFloat("manhours"));
                    dto.setTaskCount(rs.getInt("task_count"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<WorkLogDTO> selectMonthlyWorkLogs(String targetMonth) throws SQLException {

        // 月次集計の明細とCSV出力で使う工数ログ一覧を返す
        List<WorkLogDTO> list = new ArrayList<>();

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
                    WorkLogDTO dto = new WorkLogDTO();

                    // DBの1行を工数ログDTOへ詰める
                    dto.setWorkDate(rs.getString("work_date"));
                    dto.setProjectName(rs.getString("project_name"));
                    dto.setTaskName(rs.getString("task_name"));
                    dto.setUserName(rs.getString("user_name"));
                    dto.setManHours(rs.getFloat("man_hours"));
                    dto.setJobContents(rs.getString("job_contents"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    private int selectInt(String sql, Object... params) throws SQLException {

        // 件数や平均値などintで返すSQLに使う
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

        // 工数合計などfloatで返すSQLに使う
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

        // SQLの?へ値を入れる処理をDAO内部で共通化する
        if (params == null) {
            return;
        }

        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
    }
}