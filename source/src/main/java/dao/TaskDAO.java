package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.TaskDTO;

/**
 * Tasksテーブルを操作するDAO
 * タスクの取得、登録、更新、削除を担当する
 */
public class TaskDAO {

    /** DB接続 */
    private final Connection conn;

    /** 完了ステータス */
    private static final String STATUS_COMPLETED = "完了";

    /** 一覧表示順 */
    private static final String ORDER_BY_DUE_DATE_ASC =
            " ORDER BY t.due_date ASC, t.c_at DESC";

    /**
     * DB接続を受け取る
     * Serviceで作成したConnectionを使ってSQLを実行する
     * @param conn DB接続
     */
    public TaskDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * タスクを全件取得する
     * クラス図のselectAllに対応する
     * @return タスク一覧
     * @throws SQLException SQLエラー
     */
    public List<TaskDTO> selectAll() throws SQLException {

        List<TaskDTO> taskList = new ArrayList<>();

        String sql = baseSelectSql()
                + ORDER_BY_DUE_DATE_ASC;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // 取得結果を1行ずつDTOへ変換する
            while (rs.next()) {
                taskList.add(setToTaskDTO(rs));
            }
        }

        return taskList;
    }

    /**
     * 案件IDに紐づくタスクを取得する
     * クラス図のselectByProjectIdに対応する
     * @param projectId 案件ID
     * @return タスク一覧
     * @throws SQLException SQLエラー
     */
    public List<TaskDTO> selectByProjectId(int projectId) throws SQLException {

        List<TaskDTO> taskList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE t.project_id = ?"
                + ORDER_BY_DUE_DATE_ASC;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 案件IDを条件に設定する
            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {

                // 案件に紐づくタスクを一覧に詰める
                while (rs.next()) {
                    taskList.add(setToTaskDTO(rs));
                }
            }
        }

        return taskList;
    }

    /**
     * 検索条件に合うタスクを取得する
     * 新しい検索条件クラスは作らずTaskDTOを条件入れとして使う
     * @param condition 検索条件を入れたTaskDTO
     * @return 検索後のタスク一覧
     * @throws SQLException SQLエラー
     */
    public List<TaskDTO> search(TaskDTO condition) throws SQLException {

        List<TaskDTO> taskList = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(baseSelectSql());
        List<String> conditions = new ArrayList<>();

        // タスク名がある場合はキーワード検索に使う
        if (condition != null && hasText(condition.getTaskName())) {
            conditions.add("(t.task_name LIKE ? OR p.project_name LIKE ? OR t.description LIKE ?)");
            String keyword = "%" + condition.getTaskName() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        // 案件IDがある場合は条件に追加する
        if (condition != null && condition.getProjectId() > 0) {
            conditions.add("t.project_id = ?");
            params.add(condition.getProjectId());
        }

        // ステータスがある場合は条件に追加する
        if (condition != null && hasText(condition.getStatus())) {
            conditions.add("t.status = ?");
            params.add(condition.getStatus());
        }

        // 担当者IDがある場合は条件に追加する
        if (condition != null && condition.getManagerId() > 0) {
            conditions.add("t.manager_id = ?");
            params.add(condition.getManagerId());
        }

        // 条件がある場合だけWHERE句を追加する
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", conditions));
        }

        // 表示順を追加する
        sql.append(ORDER_BY_DUE_DATE_ASC);

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // 検索条件を順番に設定する
            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {

                // 検索結果を一覧に詰める
                while (rs.next()) {
                    taskList.add(setToTaskDTO(rs));
                }
            }
        }

        return taskList;
    }

    /**
     * 案件内の全タスク数を取得する
     * 案件進捗表示で使う
     * @param projectId 案件ID
     * @return 全タスク数
     * @throws SQLException SQLエラー
     */
    public int countAllByProjectId(int projectId) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS task_count "
                + "FROM Tasks "
                + "WHERE project_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 案件IDを条件に設定する
            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {

                // 件数を返す
                if (rs.next()) {
                    return rs.getInt("task_count");
                }
            }
        }

        return 0;
    }

    /**
     * 案件内の完了タスク数を取得する
     * 案件進捗表示で使う
     * @param projectId 案件ID
     * @return 完了タスク数
     * @throws SQLException SQLエラー
     */
    public int countCompletedByProjectId(int projectId) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS completed_task_count "
                + "FROM Tasks "
                + "WHERE project_id = ? "
                + "AND status = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 案件IDを条件に設定する
            ps.setInt(1, projectId);

            // 完了ステータスを条件に設定する
            ps.setString(2, STATUS_COMPLETED);

            try (ResultSet rs = ps.executeQuery()) {

                // 件数を返す
                if (rs.next()) {
                    return rs.getInt("completed_task_count");
                }
            }
        }

        return 0;
    }

    /**
     * 指定ユーザーの未完了担当タスク数を取得する
     * DashboardServiceで使う
     * @param userId ユーザーID
     * @return 担当タスク数
     * @throws SQLException SQLエラー
     */
    public int countAssignedTasks(int userId) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS assigned_task_count "
                + "FROM Tasks "
                + "WHERE manager_id = ? "
                + "AND status <> ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // ユーザーIDを条件に設定する
            ps.setInt(1, userId);

            // 完了以外を条件に設定する
            ps.setString(2, STATUS_COMPLETED);

            try (ResultSet rs = ps.executeQuery()) {

                // 件数を返す
                if (rs.next()) {
                    return rs.getInt("assigned_task_count");
                }
            }
        }

        return 0;
    }

    /**
     * 指定ユーザーの期限超過タスク数を取得する
     * DashboardServiceで使う
     * @param userId ユーザーID
     * @return 期限超過タスク数
     * @throws SQLException SQLエラー
     */
    public int countOverdueTasks(int userId) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS overdue_task_count "
                + "FROM Tasks "
                + "WHERE manager_id = ? "
                + "AND status <> ? "
                + "AND due_date < CURRENT_DATE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // ユーザーIDを条件に設定する
            ps.setInt(1, userId);

            // 完了以外を条件に設定する
            ps.setString(2, STATUS_COMPLETED);

            try (ResultSet rs = ps.executeQuery()) {

                // 件数を返す
                if (rs.next()) {
                    return rs.getInt("overdue_task_count");
                }
            }
        }

        return 0;
    }

    /**
     * タスクIDで1件取得する
     * クラス図のfindByIdに対応する
     * @param taskId タスクID
     * @return タスクDTO
     * @throws SQLException SQLエラー
     */
    public TaskDTO findById(int taskId) throws SQLException {

        TaskDTO taskDto = null;

        String sql = baseSelectSql()
                + " WHERE t.task_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // タスクIDを条件に設定する
            ps.setInt(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {

                // 取得できた場合だけDTOに変換する
                if (rs.next()) {
                    taskDto = setToTaskDTO(rs);
                }
            }
        }

        return taskDto;
    }

    /**
     * 担当者IDに紐づく未完了タスクを取得する
     * DashboardServiceで使う
     * @param managerId 担当者ID
     * @return 担当タスク一覧
     * @throws SQLException SQLエラー
     */
    public List<TaskDTO> selectByManagerId(int managerId) throws SQLException {

        List<TaskDTO> taskList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE t.manager_id = ?"
                + " AND t.status <> ?"
                + ORDER_BY_DUE_DATE_ASC;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 担当者IDを条件に設定する
            ps.setInt(1, managerId);

            // 完了以外を条件に設定する
            ps.setString(2, STATUS_COMPLETED);

            try (ResultSet rs = ps.executeQuery()) {

                // 担当タスクを一覧に詰める
                while (rs.next()) {
                    taskList.add(setToTaskDTO(rs));
                }
            }
        }

        return taskList;
    }

    /**
     * 担当者IDに紐づく期限超過タスクを取得する
     * DashboardServiceで使う
     * @param managerId 担当者ID
     * @return 期限超過タスク一覧
     * @throws SQLException SQLエラー
     */
    public List<TaskDTO> selectOverdueByManagerId(int managerId) throws SQLException {

        List<TaskDTO> taskList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE t.manager_id = ?"
                + " AND t.status <> ?"
                + " AND t.due_date < CURRENT_DATE"
                + ORDER_BY_DUE_DATE_ASC;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 担当者IDを条件に設定する
            ps.setInt(1, managerId);

            // 完了以外を条件に設定する
            ps.setString(2, STATUS_COMPLETED);

            try (ResultSet rs = ps.executeQuery()) {

                // 期限超過タスクを一覧に詰める
                while (rs.next()) {
                    TaskDTO taskDto = setToTaskDTO(rs);
                    taskDto.setOverdue(true);
                    taskList.add(taskDto);
                }
            }
        }

        return taskList;
    }

    /**
     * タスクを登録する
     * クラス図のinsertに対応する
     * @param taskDto 登録するタスクDTO
     * @return 登録件数
     * @throws SQLException SQLエラー
     */
    public int insert(TaskDTO taskDto) throws SQLException {

        String sql =
                "INSERT INTO Tasks ("
                + "task_name, "
                + "project_id, "
                + "manager_id, "
                + "start_date, "
                + "due_date, "
                + "estimated_manhours, "
                + "progress, "
                + "status, "
                + "priority, "
                + "description"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // タスク名を設定する
            ps.setString(1, taskDto.getTaskName());

            // 案件IDを設定する
            ps.setInt(2, taskDto.getProjectId());

            // 担当者IDを設定する
            setNullableInt(ps, 3, taskDto.getManagerId());

            // 開始日を設定する
            ps.setDate(4, toSqlDate(taskDto.getStartDate()));

            // 期限日を設定する
            ps.setDate(5, toSqlDate(taskDto.getDueDate()));

            // 見積工数を設定する
            ps.setFloat(6, taskDto.getEstimatedManhours());

            // 進捗率を設定する
            ps.setInt(7, taskDto.getProgress());

            // ステータスを設定する
            ps.setString(8, taskDto.getStatus());

            // 優先度を設定する
            ps.setString(9, taskDto.getPriority());

            // 説明を設定する
            ps.setString(10, taskDto.getDescription());

            // 登録件数を返す
            return ps.executeUpdate();
        }
    }

    /**
     * タスクを更新する
     * クラス図のupdateに対応する
     * @param taskDto 更新するタスクDTO
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int update(TaskDTO taskDto) throws SQLException {

        String sql =
                "UPDATE Tasks SET "
                + "task_name = ?, "
                + "project_id = ?, "
                + "manager_id = ?, "
                + "start_date = ?, "
                + "due_date = ?, "
                + "estimated_manhours = ?, "
                + "progress = ?, "
                + "status = ?, "
                + "priority = ?, "
                + "description = ?, "
                + "u_at = CURRENT_TIMESTAMP "
                + "WHERE task_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // タスク名を設定する
            ps.setString(1, taskDto.getTaskName());

            // 案件IDを設定する
            ps.setInt(2, taskDto.getProjectId());

            // 担当者IDを設定する
            setNullableInt(ps, 3, taskDto.getManagerId());

            // 開始日を設定する
            ps.setDate(4, toSqlDate(taskDto.getStartDate()));

            // 期限日を設定する
            ps.setDate(5, toSqlDate(taskDto.getDueDate()));

            // 見積工数を設定する
            ps.setFloat(6, taskDto.getEstimatedManhours());

            // 進捗率を設定する
            ps.setInt(7, taskDto.getProgress());

            // ステータスを設定する
            ps.setString(8, taskDto.getStatus());

            // 優先度を設定する
            ps.setString(9, taskDto.getPriority());

            // 説明を設定する
            ps.setString(10, taskDto.getDescription());

            // タスクIDを設定する
            ps.setInt(11, taskDto.getTaskId());

            // 更新件数を返す
            return ps.executeUpdate();
        }
    }

    /**
     * タスクの状態を変更する
     * 完了なら進捗率100、未着手なら0に補正する
     * @param taskId タスクID
     * @param status ステータス
     * @param progress 進捗率
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int changeStatus(int taskId, String status, int progress) throws SQLException {

        String sql =
                "UPDATE Tasks SET "
                + "status = ?, "
                + "progress = ?, "
                + "u_at = CURRENT_TIMESTAMP "
                + "WHERE task_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // ステータスを設定する
            ps.setString(1, status);

            // 進捗率を設定する
            ps.setInt(2, progress);

            // タスクIDを設定する
            ps.setInt(3, taskId);

            // 更新件数を返す
            return ps.executeUpdate();
        }
    }

    /**
     * タスクを削除する
     * 関連工数ログはService側で先に削除する
     * @param taskId タスクID
     * @return 削除件数
     * @throws SQLException SQLエラー
     */
    public int delete(int taskId) throws SQLException {

        String sql =
                "DELETE FROM Tasks "
                + "WHERE task_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // タスクIDを条件に設定する
            ps.setInt(1, taskId);

            // 削除件数を返す
            return ps.executeUpdate();
        }
    }

    /**
     * タスク取得用の共通SQLを作る
     * SELECT系メソッドの取得項目を統一する
     * @return 共通SELECT文
     */
    private String baseSelectSql() {

        return "SELECT "
                + "t.task_id, "
                + "t.task_name, "
                + "t.project_id, "
                + "p.project_name, "
                + "t.manager_id, "
                + "u.name AS manager_name, "
                + "DATE_FORMAT(t.start_date, '%Y-%m-%d') AS start_date, "
                + "DATE_FORMAT(t.due_date, '%Y-%m-%d') AS due_date, "
                + "t.estimated_manhours, "
                + "COALESCE(( "
                + "    SELECT SUM(wl.man_hours) "
                + "    FROM WorkLogs wl "
                + "    WHERE wl.task_id = t.task_id "
                + "), 0) AS actual_manhours, "
                + "t.progress, "
                + "t.status, "
                + "t.priority, "
                + "t.description, "
                + "DATE_FORMAT(t.c_at, '%Y-%m-%d %H:%i:%s') AS c_at, "
                + "DATE_FORMAT(t.u_at, '%Y-%m-%d %H:%i:%s') AS u_at, "
                + "CASE "
                + "    WHEN t.status <> '" + STATUS_COMPLETED + "' "
                + "    AND t.due_date < CURRENT_DATE "
                + "    THEN 1 "
                + "    ELSE 0 "
                + "END AS overdue "
                + "FROM Tasks t "
                + "INNER JOIN Projects p ON t.project_id = p.project_id "
                + "LEFT JOIN Users u ON t.manager_id = u.user_id";
    }

    /**
     * ResultSetの1行をTaskDTOへ変換する
     * クラス図のsetToTaskDTOに対応する
     * @param rs SQL取得結果
     * @return タスクDTO
     * @throws SQLException SQLエラー
     */
    private TaskDTO setToTaskDTO(ResultSet rs) throws SQLException {

        TaskDTO taskDto = new TaskDTO();

        taskDto.setTaskId(rs.getInt("task_id"));
        taskDto.setTaskName(rs.getString("task_name"));
        taskDto.setProjectId(rs.getInt("project_id"));
        taskDto.setProjectName(rs.getString("project_name"));
        taskDto.setManagerId(rs.getInt("manager_id"));
        taskDto.setManagerName(rs.getString("manager_name"));
        taskDto.setStartDate(rs.getString("start_date"));
        taskDto.setDueDate(rs.getString("due_date"));
        taskDto.setEstimatedManhours(rs.getFloat("estimated_manhours"));
        taskDto.setActualManhours(rs.getFloat("actual_manhours"));
        taskDto.setProgress(rs.getInt("progress"));
        taskDto.setStatus(rs.getString("status"));
        taskDto.setPriority(rs.getString("priority"));
        taskDto.setDescription(rs.getString("description"));
        taskDto.setCreatedAt(rs.getString("c_at"));
        taskDto.setUpdatedAt(rs.getString("u_at"));
        taskDto.setOverdue(rs.getBoolean("overdue"));

        return taskDto;
    }

    /**
     * Listに入れた検索条件をPreparedStatementへ設定する
     * Mapは使わずListの順番で管理する
     * @param ps SQL実行準備オブジェクト
     * @param params 設定値一覧
     * @throws SQLException SQLエラー
     */
    private void setParameters(PreparedStatement ps, List<Object> params) throws SQLException {

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    /**
     * int値をNULL許可列へ設定する
     * 0以下の場合はNULLを入れる
     * @param ps SQL実行準備オブジェクト
     * @param index ?の位置
     * @param value 設定値
     * @throws SQLException SQLエラー
     */
    private void setNullableInt(PreparedStatement ps, int index, int value) throws SQLException {

        if (value <= 0) {
            ps.setNull(index, Types.INTEGER);
            return;
        }

        ps.setInt(index, value);
    }

    /**
     * Stringの日付をSQL用Dateへ変換する
     * 未入力ならnullを返す
     * @param value 日付文字列
     * @return SQL用Date
     */
    private Date toSqlDate(String value) {

        if (!hasText(value)) {
            return null;
        }

        return Date.valueOf(value);
    }

    /**
     * 文字列が入力されているか確認する
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}