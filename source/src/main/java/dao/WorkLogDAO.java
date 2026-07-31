package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.WorkLogDTO;

/**
 * WorkLogsテーブルを操作するDAO
 * 工数ログの取得、登録、削除、集計を担当する
 */
public class WorkLogDAO {

    /** DB接続 */
    private final Connection conn;

    /** 工数ログID列名 */
    private static final String WORK_LOG_ID_COLUMN = "work_logs_id";

    /** 最新工数ログ表示件数 */
    private static final int LATEST_LIMIT = 10;

    /** 工数ログ表示順 */
    private static final String ORDER_BY_WORK_DATE_DESC =
            " ORDER BY wl.work_date DESC, wl.c_at DESC";

    /**
     * DB接続を受け取る。
     *
     * @param conn DB接続
     */
    public WorkLogDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * 工数ログを全件取得する。
     *
     * @return 工数ログ一覧
     * @throws SQLException SQLエラー
     */
    public ArrayList<WorkLogDTO> selectAll() throws SQLException {

        ArrayList<WorkLogDTO> workLogList = new ArrayList<>();

        String sql = baseSelectSql()
                + ORDER_BY_WORK_DATE_DESC;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                workLogList.add(setToWorkLogDTO(rs));
            }
        }

        return workLogList;
    }

    /**
     * タスクIDに紐づく工数ログを取得する。
     *
     * @param taskId タスクID
     * @return 工数ログ一覧
     * @throws SQLException SQLエラー
     */
    public List<WorkLogDTO> selectByTaskId(int taskId) throws SQLException {

        List<WorkLogDTO> workLogList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE wl.task_id = ?"
                + ORDER_BY_WORK_DATE_DESC;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    workLogList.add(setToWorkLogDTO(rs));
                }
            }
        }

        return workLogList;
    }

    /**
     * 案件IDに紐づく最新工数ログを取得する。
     *
     * @param projectId 案件ID
     * @return 最新工数ログ一覧
     * @throws SQLException SQLエラー
     */
    public List<WorkLogDTO> selectLatestByProjectId(int projectId) throws SQLException {

        List<WorkLogDTO> workLogList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE t.project_id = ?"
                + ORDER_BY_WORK_DATE_DESC
                + " LIMIT ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);
            ps.setInt(2, LATEST_LIMIT);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    workLogList.add(setToWorkLogDTO(rs));
                }
            }
        }

        return workLogList;
    }

    /**
     * 対象月の工数ログを取得する。
     *
     * @param targetMonth 対象月 yyyy-MM
     * @return 工数ログ一覧
     * @throws SQLException SQLエラー
     */
    public List<WorkLogDTO> selectByMonth(String targetMonth) throws SQLException {

        List<WorkLogDTO> workLogList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE DATE_FORMAT(wl.work_date, '%Y-%m') = ?"
                + ORDER_BY_WORK_DATE_DESC;

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
     * 工数ログを登録する。
     *
     * @param workLogDto 登録する工数ログDTO
     * @return 登録件数
     * @throws SQLException SQLエラー
     */
    public int workLogInsert(WorkLogDTO workLogDto) throws SQLException {

        String sql =
                "INSERT INTO WorkLogs ("
                + "task_id, "
                + "user_id, "
                + "work_date, "
                + "man_hours, "
                + "job_contents"
                + ") VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, workLogDto.getTaskId());
            ps.setInt(2, workLogDto.getUserId());
            ps.setDate(3, toSqlDate(workLogDto.getWorkDate()));
            ps.setFloat(4, workLogDto.getManHours());
            ps.setString(5, workLogDto.getJobContents());

            return ps.executeUpdate();
        }
    }

    /**
     * 工数ログを削除する。
     *
     * @param workLogsId 工数ログID
     * @return 削除件数
     * @throws SQLException SQLエラー
     */
    public int workLogDelete(int workLogsId) throws SQLException {

        String sql =
                "DELETE FROM WorkLogs "
                + "WHERE " + WORK_LOG_ID_COLUMN + " = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, workLogsId);

            return ps.executeUpdate();
        }
    }

    /**
     * タスクIDに紐づく工数ログをまとめて削除する。
     *
     * @param taskId タスクID
     * @return 削除件数
     * @throws SQLException SQLエラー
     */
    public int deleteByTaskId(int taskId) throws SQLException {

        String sql =
                "DELETE FROM WorkLogs "
                + "WHERE task_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);

            return ps.executeUpdate();
        }
    }

    /**
     * タスクIDに紐づく工数合計を取得する。
     *
     * @param taskId タスクID
     * @return 工数合計
     * @throws SQLException SQLエラー
     */
    public Float sumByTaskId(int taskId) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(man_hours), 0) AS total_man_hours "
                + "FROM WorkLogs "
                + "WHERE task_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("total_man_hours");
                }
            }
        }

        return 0f;
    }

    /**
     * 案件IDに紐づく工数合計を取得する。
     *
     * @param projectId 案件ID
     * @return 工数合計
     * @throws SQLException SQLエラー
     */
    public Float sumByProjectId(int projectId) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(wl.man_hours), 0) AS total_man_hours "
                + "FROM WorkLogs wl "
                + "INNER JOIN Tasks t ON wl.task_id = t.task_id "
                + "WHERE t.project_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("total_man_hours");
                }
            }
        }

        return 0f;
    }
    
    /**

    ◦ 指定ユーザーの当月工数合計を取得する.
    ◦ @param userId ユーザーID.
    ◦ @return 当月工数合計.
    ◦ @throws SQLException SQL例外.
     */
    public float sumThisMonthByUserId(int userId) throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(man_hours), 0) AS total_manhours "
                + "FROM WorkLogs "
                + "WHERE user_id = ? "
                + "AND DATE_FORMAT(work_date, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m')";

        try (PreparedStatement pStmt = conn.prepareStatement(sql)) {

            pStmt.setInt(1, userId);

            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getFloat("total_manhours");
                }
            }
        }

        return 0f;
    }


    /**
     * 工数ログ取得用の共通SQLを作る。
     *
     * @return 共通SELECT文
     */
    private String baseSelectSql() {

        return "SELECT "
                + "wl." + WORK_LOG_ID_COLUMN + " AS work_logs_id, "
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
                + "INNER JOIN Users u ON wl.user_id = u.user_id";
    }

    /**
     * ResultSetの1行をWorkLogDTOへ変換する。
     *
     * @param rs SQL取得結果
     * @return 工数ログDTO
     * @throws SQLException SQLエラー
     */
    private WorkLogDTO setToWorkLogDTO(ResultSet rs) throws SQLException {

        WorkLogDTO workLogDto = new WorkLogDTO();

        workLogDto.setWorkLogsId(rs.getInt("work_logs_id"));
        workLogDto.setTaskId(rs.getInt("task_id"));
        workLogDto.setTaskName(rs.getString("task_name"));
        workLogDto.setProjectId(rs.getInt("project_id"));
        workLogDto.setProjectName(rs.getString("project_name"));
        workLogDto.setUserId(rs.getInt("user_id"));
        workLogDto.setUserName(rs.getString("user_name"));
        workLogDto.setWorkDate(rs.getString("work_date"));
        workLogDto.setManHours(rs.getFloat("man_hours"));
        workLogDto.setJobContents(rs.getString("job_contents"));
        workLogDto.setCreatedAt(rs.getString("c_at"));
        workLogDto.setUpdatedAt(rs.getString("u_at"));

        return workLogDto;
    }

    /**
     * Stringの日付をSQL用Dateへ変換する。
     *
     * @param value 日付文字列
     * @return SQL用Date
     */
    private Date toSqlDate(String value) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return Date.valueOf(value);
    }
}