package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import model.ProjectsDTO;

/**
 * Projectsテーブルを操作するDAO
 * 案件の取得、登録、更新、ステータス変更を担当する
 */
public class ProjectsDAO {

    /** DB接続 */
    private final Connection conn;

    /** 進行中 */
    private static final String STATUS_IN_PROGRESS = "進行中";

    /** 完了 */
    private static final String STATUS_COMPLETED = "完了";

    /** 一覧表示順 */
    private static final String ORDER_BY_CREATED_DESC =
            " ORDER BY p.c_at DESC";

    /**
     * DB接続を受け取る
     * Serviceで作成したConnectionを使ってSQLを実行する
     * @param conn DB接続
     */
    public ProjectsDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * 案件を全件取得する
     * クラス図のselectAllに対応する
     * @return 案件一覧
     * @throws SQLException SQLエラー
     */
    public List<ProjectsDTO> selectAll() throws SQLException {

        List<ProjectsDTO> projectList = new ArrayList<>();

        String sql = baseSelectSql()
                + ORDER_BY_CREATED_DESC;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // 取得結果を1行ずつDTOへ変換する
            while (rs.next()) {
                projectList.add(setToProjectDTO(rs));
            }
        }

        return projectList;
    }

    /**
     * 検索条件に合う案件を取得する
     * ProjectSearchConditionは作らずProjectDTOを条件入れとして使う
     * @param condition 検索条件を入れたProjectDTO
     * @return 検索後の案件一覧
     * @throws SQLException SQLエラー
     */
    public List<ProjectsDTO> search(ProjectsDTO condition) throws SQLException {

        List<ProjectsDTO> projectList = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(baseSelectSql());
        List<String> conditions = new ArrayList<>();

        // 案件名がある場合はキーワード検索に使う
        if (condition != null && hasText(condition.getProjectName())) {
            conditions.add("(p.project_code LIKE ? OR p.project_name LIKE ? OR p.customer_name LIKE ? OR p.description LIKE ?)");
            String keyword = "%" + condition.getProjectName() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }

        // ステータスがある場合は条件に追加する
        if (condition != null && hasText(condition.getStatus())) {
            conditions.add("p.status = ?");
            params.add(condition.getStatus());
        }

        // 優先度がある場合は条件に追加する
        if (condition != null && hasText(condition.getPriority())) {
            conditions.add("p.priority = ?");
            params.add(condition.getPriority());
        }

        // PMが指定されている場合は条件に追加する
        if (condition != null && condition.getProjectManagerId() > 0) {
            conditions.add("p.project_manager_id = ?");
            params.add(condition.getProjectManagerId());
        }

        // 条件がある場合だけWHERE句を追加する
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", conditions));
        }

        // 表示順を追加する
        sql.append(ORDER_BY_CREATED_DESC);

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            // Listに入れた条件値を順番に?へ設定する
            setParameters(ps, params);

            try (ResultSet rs = ps.executeQuery()) {

                // 検索結果を一覧に詰める
                while (rs.next()) {
                    projectList.add(setToProjectDTO(rs));
                }
            }
        }

        return projectList;
    }

    /**
     * 案件IDで1件取得する
     * クラス図のfindByIdに対応する
     * @param projectId 案件ID
     * @return 案件DTO
     * @throws SQLException SQLエラー
     */
    public ProjectsDTO findById(int projectId) throws SQLException {

        ProjectsDTO projectDto = null;

        String sql = baseSelectSql()
                + " WHERE p.project_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 案件IDを条件に設定する
            ps.setInt(1, projectId);

            try (ResultSet rs = ps.executeQuery()) {

                // 取得できた場合だけDTOに変換する
                if (rs.next()) {
                    projectDto = setToProjectDTO(rs);
                }
            }
        }

        return projectDto;
    }

    /**
     * 案件を登録する
     * クラス図のprojectInsertに対応する
     * @param projectDto 登録する案件DTO
     * @return 登録件数
     * @throws SQLException SQLエラー
     */
    public int projectInsert(ProjectsDTO projectDto) throws SQLException {

        String sql =
                "INSERT INTO Projects ("
                + "project_code, "
                + "project_name, "
                + "customer_name, "
                + "create_member_id, "
                + "project_manager_id, "
                + "start_date, "
                + "due_date, "
                + "estimated_manhours, "
                + "description, "
                + "status, "
                + "priority"
                + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 案件コードを設定する
            ps.setString(1, projectDto.getProjectCode());

            // 案件名を設定する
            ps.setString(2, projectDto.getProjectName());

            // 顧客名を設定する
            ps.setString(3, projectDto.getCustomerName());

            // 登録者IDを設定する
            setNullableInt(ps, 4, projectDto.getCreateMemberId());

            // PMユーザーIDを設定する
            setNullableInt(ps, 5, projectDto.getProjectManagerId());

            // 開始予定日を設定する
            setNullableDate(ps, 6, projectDto.getStartDate());

            // 終了予定日を設定する
            setNullableDate(ps, 7, projectDto.getDueDate());

            // 予算工数を設定する
            ps.setFloat(8, projectDto.getEstimatedManhours());

            // 説明を設定する
            ps.setString(9, projectDto.getDescription());

            // ステータスを設定する
            ps.setString(10, projectDto.getStatus());

            // 優先度を設定する
            ps.setString(11, projectDto.getPriority());

            // 登録件数を返す
            return ps.executeUpdate();
        }
    }

    /**
     * 案件を更新する
     * 案件コードは更新しない
     * クラス図のprojectUpdateに対応する
     * @param projectDto 更新する案件DTO
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int projectUpdate(ProjectsDTO projectDto) throws SQLException {

        String sql =
                "UPDATE Projects SET "
                + "project_name = ?, "
                + "customer_name = ?, "
                + "project_manager_id = ?, "
                + "start_date = ?, "
                + "due_date = ?, "
                + "estimated_manhours = ?, "
                + "description = ?, "
                + "status = ?, "
                + "priority = ?, "
                + "u_at = CURRENT_TIMESTAMP "
                + "WHERE project_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 案件名を設定する
            ps.setString(1, projectDto.getProjectName());

            // 顧客名を設定する
            ps.setString(2, projectDto.getCustomerName());

            // PMユーザーIDを設定する
            setNullableInt(ps, 3, projectDto.getProjectManagerId());

            // 開始予定日を設定する
            setNullableDate(ps, 4, projectDto.getStartDate());

            // 終了予定日を設定する
            setNullableDate(ps, 5, projectDto.getDueDate());

            // 予算工数を設定する
            ps.setFloat(6, projectDto.getEstimatedManhours());

            // 説明を設定する
            ps.setString(7, projectDto.getDescription());

            // ステータスを設定する
            ps.setString(8, projectDto.getStatus());

            // 優先度を設定する
            ps.setString(9, projectDto.getPriority());

            // 案件IDを設定する
            ps.setInt(10, projectDto.getProjectId());

            // 更新件数を返す
            return ps.executeUpdate();
        }
    }

    /**
     * 案件ステータスのみ更新する
     * DAO層メソッド概要のupdateStatusに対応する
     * @param projectId 案件ID
     * @param status ステータス
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int updateStatus(int projectId, String status) throws SQLException {

        String sql =
                "UPDATE Projects SET "
                + "status = ?, "
                + "u_at = CURRENT_TIMESTAMP "
                + "WHERE project_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // ステータスを設定する
            ps.setString(1, status);

            // 案件IDを設定する
            ps.setInt(2, projectId);

            // 更新件数を返す
            return ps.executeUpdate();
        }
    }

    /**
     * 案件コードが既に存在するか確認する
     * @param projectCode 案件コード
     * @return 存在する場合true
     * @throws SQLException SQLエラー
     */
    public boolean existsProjectCode(String projectCode) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS project_count "
                + "FROM Projects "
                + "WHERE project_code = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 案件コードを条件に設定する
            ps.setString(1, projectCode);

            try (ResultSet rs = ps.executeQuery()) {

                // 1件以上あればtrueを返す
                if (rs.next()) {
                    return rs.getInt("project_count") > 0;
                }
            }
        }

        return false;
    }

    /**
     * 進行中案件数を取得する
     * ダッシュボードカードで使う
     * @return 進行中案件数
     * @throws SQLException SQLエラー
     */
    public int countInProgressProjects() throws SQLException {

        String sql =
                "SELECT COUNT(*) AS project_count "
                + "FROM Projects "
                + "WHERE status = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 進行中ステータスを条件に設定する
            ps.setString(1, STATUS_IN_PROGRESS);

            try (ResultSet rs = ps.executeQuery()) {

                // 件数を返す
                if (rs.next()) {
                    return rs.getInt("project_count");
                }
            }
        }

        return 0;
    }

    /**
     * 進行中案件一覧を取得する
     * ダッシュボード一覧や案件選択肢で使う
     * @return 進行中案件一覧
     * @throws SQLException SQLエラー
     */
    public List<ProjectsDTO> selectInProgressProjects() throws SQLException {

        List<ProjectsDTO> projectList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE p.status = ?"
                + " ORDER BY p.due_date ASC, p.c_at DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // 進行中ステータスを条件に設定する
            ps.setString(1, STATUS_IN_PROGRESS);

            try (ResultSet rs = ps.executeQuery()) {

                // 進行中案件を一覧に詰める
                while (rs.next()) {
                    projectList.add(setToProjectDTO(rs));
                }
            }
        }

        return projectList;
    }

    /**
     * 案件取得用の共通SQLを作る
     * SELECT系メソッドの取得項目を統一する
     * @return 共通SELECT文
     */
    private String baseSelectSql() {

        return "SELECT "
                + "p.project_id, "
                + "p.project_code, "
                + "p.project_name, "
                + "p.customer_name, "
                + "p.create_member_id, "
                + "p.project_manager_id, "
                + "pm.name AS project_manager_name, "
                + "DATE_FORMAT(p.start_date, '%Y-%m-%d') AS start_date, "
                + "DATE_FORMAT(p.due_date, '%Y-%m-%d') AS due_date, "
                + "p.estimated_manhours, "
                + "COALESCE(( "
                + "    SELECT SUM(wl.man_hours) "
                + "    FROM Tasks t_sum "
                + "    INNER JOIN WorkLogs wl ON t_sum.task_id = wl.task_id "
                + "    WHERE t_sum.project_id = p.project_id "
                + "), 0) AS actual_manhours, "
                + "p.description, "
                + "p.status, "
                + "p.priority, "
                + "COALESCE(( "
                + "    SELECT COUNT(*) "
                + "    FROM Tasks t_all "
                + "    WHERE t_all.project_id = p.project_id "
                + "), 0) AS task_count, "
                + "COALESCE(( "
                + "    SELECT COUNT(*) "
                + "    FROM Tasks t_done "
                + "    WHERE t_done.project_id = p.project_id "
                + "    AND t_done.status = '" + STATUS_COMPLETED + "' "
                + "), 0) AS completed_task_count, "
                + "DATE_FORMAT(p.c_at, '%Y-%m-%d %H:%i:%s') AS c_at, "
                + "DATE_FORMAT(p.u_at, '%Y-%m-%d %H:%i:%s') AS u_at "
                + "FROM Projects p "
                + "LEFT JOIN Users pm ON p.project_manager_id = pm.user_id";
    }

    /**
     * ResultSetの1行をProjectDTOへ変換する
     * SELECT系メソッドの詰め替え処理を共通化する
     * @param rs SQL取得結果
     * @return 案件DTO
     * @throws SQLException SQLエラー
     */
    private ProjectsDTO setToProjectDTO(ResultSet rs) throws SQLException {

        ProjectsDTO projectDto = new ProjectsDTO();

        projectDto.setProjectId(rs.getInt("project_id"));
        projectDto.setProjectCode(rs.getString("project_code"));
        projectDto.setProjectName(rs.getString("project_name"));
        projectDto.setCustomerName(rs.getString("customer_name"));
        projectDto.setCreateMemberId(rs.getInt("create_member_id"));
        projectDto.setProjectManagerId(rs.getInt("project_manager_id"));
        projectDto.setProjectManagerName(rs.getString("project_manager_name"));
        projectDto.setStartDate(rs.getString("start_date"));
        projectDto.setDueDate(rs.getString("due_date"));
        projectDto.setEstimatedManhours(rs.getFloat("estimated_manhours"));
        projectDto.setActualManhours(rs.getFloat("actual_manhours"));
        projectDto.setDescription(rs.getString("description"));
        projectDto.setStatus(rs.getString("status"));
        projectDto.setPriority(rs.getString("priority"));
        projectDto.setTaskCount(rs.getInt("task_count"));
        projectDto.setCompletedTaskCount(rs.getInt("completed_task_count"));
        projectDto.setProgressRate(calcProgressRate(
                projectDto.getCompletedTaskCount(),
                projectDto.getTaskCount()));
        projectDto.setCreatedAt(rs.getString("c_at"));
        projectDto.setUpdatedAt(rs.getString("u_at"));

        return projectDto;
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
     * Stringの日付をSQL用Dateへ設定する
     * 未入力ならNULLを入れる
     * @param ps SQL実行準備オブジェクト
     * @param index ?の位置
     * @param value 日付文字列
     * @throws SQLException SQLエラー
     */
    private void setNullableDate(PreparedStatement ps, int index, String value) throws SQLException {

        if (!hasText(value)) {
            ps.setNull(index, Types.DATE);
            return;
        }

        ps.setDate(index, Date.valueOf(value));
    }

    /**
     * タスク完了率を計算する
     * @param completedTaskCount 完了タスク数
     * @param taskCount 全タスク数
     * @return 完了率
     */
    private float calcProgressRate(int completedTaskCount, int taskCount) {

        if (taskCount <= 0) {
            return 0;
        }

        return completedTaskCount * 100f / taskCount;
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