package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.TaskDTO;

public class TaskDAO {

	public Connection conn = null;

	//コネクションを保持するコンストラクタ
	public TaskDAO(Connection conn) {
		this.conn = conn;
	}

	/**
     * タスクを全件取得する
     * クラス図のselectAllに対応する
     * @return タスク一覧
     * @throws SQLException SQLエラー
     */
    public ArrayList<TaskDTO> selectAll() throws SQLException {

        ArrayList<TaskDTO> taskList = new ArrayList<>();

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
     * 条件に合うタスクを取得する
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
        	for (int i = 0; i < params.size(); i++) {
        	    ps.setObject(i + 1, params.get(i));
        	}

            try (ResultSet rs = ps.executeQuery()) {

                // 検索結果を一覧に詰める
                while (rs.next()) {
                    taskList.add(setToTaskDTO(rs));
                }
            }
        }

        return taskList;
    }

	//指定案件の全てのタスク総数を取得する
	public int countAllByProjectId(int projectId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) AS task_count FROM tasks WHERE project_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, projectId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();


		// 結果
		if (rs.next()) {
			return rs.getInt("task_count");
		}

		return 0;
	}

	//完了したタスク数を取得する
	public int countCompletedByProjectId(int projectId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) AS completed_task_count FROM tasks WHERE project_id=? AND status = '完了'";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, projectId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 結果
		if (rs.next()) {
			return rs.getInt("completed_task_count");
		}

		return 0;
	}

	//担当タスク数を取得する
	public int countAssignedTasks(int userId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) AS assigned_task_count FROM tasks WHERE manager_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, userId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();


		// 結果
		if (rs.next()) {
			return rs.getInt("assigned_task_count");
		}

		return 0;
	}
	
	/**
     * 指定ユーザーの未完了担当タスク数を取得する
     * クラス図のcountAssignedTasksに対応する
     * @param userId ユーザーID
     * @return 担当タスク数
     * @throws SQLException SQLエラー
     */
    public int inCompleteCountAssignedTasks(int userId) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS incomplete_assigned_task_count "
                + "FROM tasks "
                + "WHERE manager_id = ? "
                + "AND status <> ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // ユーザーIDを条件に設定する
            ps.setInt(1, userId);

            // 完了以外を対象にする
            ps.setString(2, "完了");

            try (ResultSet rs = ps.executeQuery()) {

                // 件数を返す
                if (rs.next()) {
                    return rs.getInt("incomplete_assigned_task_count");
                }
            }
        }

        return 0;
    }

	//期限超過タスクを取得する
	public int countOverdueTasks(int userId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) AS overdue_task_count "
				+ "FROM tasks"
				+ " WHERE manager_id=? "
				+ "AND status <> '完了' "
				+ "AND due_date < CURRENT_DATE";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		//ユーザーiDを条件に設定
		pStmt.setInt(1, userId);


		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 結果
		if (rs.next()) {
			return rs.getInt("overdue_task_count");
		}

		// serviceに返却する falseの場合は初期値
		return 0;
	}

	//担当タスク一覧を取得する
	public List<TaskDTO> selectAssignedTasks(int userId) throws SQLException {
		List<TaskDTO> TaskList = new ArrayList<>();

		// SELECT文を準備する
		String sql = baseSelectSql()
				+ "WHERE t.manager_id = ? "
				+ ORDER_BY_DUE_DATE_ASC;
		
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, userId);

		// SELECT文を実行し、結果表を取得する
		try (ResultSet rs = pStmt.executeQuery()) {

            // 担当タスクを一覧に詰める
            while (rs.next()) {
                TaskList.add(setToTaskDTO(rs));
            }
        }

		//serviceに返却する
		return TaskList;
	}
	
	/**
     * 期限超過の未完了タスクを取得する
     * ホーム画面や注意表示で使う
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

            // 1番目の?に担当者IDを入れる
            ps.setInt(1, managerId);

            // 2番目の?に完了ステータスを入れる
            ps.setString(2, "完了");

            try (ResultSet rs = ps.executeQuery()) {

                // 期限超過タスクを一覧に詰める
                while (rs.next()) {
                    taskList.add(setToTaskDTO(rs));
                }
            }
        }

        return taskList;
    }
	//TaskIDを取得する
	public TaskDTO findById(int taskId) throws SQLException {

		TaskDTO dto = null;

		// SELECT文を準備する
		String sql = baseSelectSql()
				+ "WHERE task_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, taskId);
		// SELECT文を実行し、結果表を取得する
		try (ResultSet rs = pStmt.executeQuery()) {

            // 取得できた1行をDTOへ変換する
            if (rs.next()) {
                dto = setToTaskDTO(rs);
            }
        }

		// serviceに返却する
		return dto;
	}


	//タスク登録をする
	public int taskInsert(TaskDTO taskDto) throws SQLException {

		// SELECT文を準備する
		String sql = "INSERT INTO tasks(" +
				"task_name, project_id, manager_id, " +
				"start_date, due_date, estimated_manhours, " +
				"progress, status, priority, description) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する ResultSet rs = pStmt.executeQuery();

		// タスク名を設定する
		pStmt.setString(1, taskDto.getTaskName());

        // 案件IDを設定する
		pStmt.setInt(2, taskDto.getProjectId());

        // 担当者IDを設定する
        pStmt.setInt(3, taskDto.getManagerId());

        // 開始日を設定する
        pStmt.setDate(4, toSqlDate(taskDto.getStartDate()));

        // 期限日を設定する
        pStmt.setDate(5, toSqlDate(taskDto.getDueDate()));

        // 見積工数を設定する
        pStmt.setFloat(6, taskDto.getEstimatedManhours());

        // 進捗率を設定する
        pStmt.setInt(7, taskDto.getProgress());

        // ステータスを設定する
        pStmt.setString(8, taskDto.getStatus());

        // 優先度を設定する
        pStmt.setString(9, taskDto.getPriority());

        // 説明を設定する
        pStmt.setString(10, taskDto.getDescription());
        
		return pStmt.executeUpdate();

	}

	//タスク編集
	public int taskUpdate(TaskDTO taskDto) throws SQLException {

		// SELECT文を準備する
		String sql = "UPDATE tasks SET "
				+ "task_name=?, "
				+ "project_id=?, "
				+ "manager_id=?, "
				+ "start_date=?, "
				+ "due_date=?, "
				+ "estimated_manhours=?, "
				+ "progress=?, "
				+ "status=?, "
				+ "priority=?, "
				+ "description=? "
				+ "u_at = CURRENT_TIMESTAMP "
				+ "WHERE task_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する ResultSet rs = pStmt.executeQuery();

		// 移し替え
		// タスク名を設定する
		pStmt.setString(1, taskDto.getTaskName());

        // 案件IDを設定する
		pStmt.setInt(2, taskDto.getProjectId());

        // 担当者IDを設定する
        pStmt.setInt(3, taskDto.getManagerId());

        // 開始日を設定する
        pStmt.setDate(4, toSqlDate(taskDto.getStartDate()));

        // 期限日を設定する
        pStmt.setDate(5, toSqlDate(taskDto.getDueDate()));

        // 見積工数を設定する
        pStmt.setFloat(6, taskDto.getEstimatedManhours());

        // 進捗率を設定する
        pStmt.setInt(7, taskDto.getProgress());

        // ステータスを設定する
        pStmt.setString(8, taskDto.getStatus());

        // 優先度を設定する
        pStmt.setString(9, taskDto.getPriority());

        // 説明を設定する
        pStmt.setString(10, taskDto.getDescription());

        // タスクIDを条件に設定する
        pStmt.setInt(11, taskDto.getTaskId());

		return pStmt.executeUpdate();

	}

	//進捗・ステータスを更新する
	public int updateStatusAndProgress(int taskId, String status, int progress)throws SQLException {

		// SELECT文を準備する
		String sql = "UPDATE tasks SET "
				+ "status = ?, "
				+ "progress = ?, "
				+ "u_at = CURRENT_TIMESTAMP "
				+ "WHERE task_id = ?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する ResultSet rs = pStmt.executeQuery();

		// 移し替え

		pStmt.setString(1, status);
		pStmt.setInt(2, progress);
		pStmt.setInt(3, taskId);

		return pStmt.executeUpdate();

	}

	//タスクを削除する
	public int taskDelete(int taskId) throws SQLException {

		// SELECT文を準備する
		String sql = "DELETE FROM tasks WHERE task_id = ?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する ResultSet rs = pStmt.executeQuery();

		// 移し替え

		pStmt.setInt(1, taskId);

		return pStmt.executeUpdate();

	}
	/**
	 * タスク取得用の共通SQL文の補助メソッド
	 * 一覧、検索、詳細で同じ取得項目を使える
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
                + "    FROM workLogs wl "
                + "    WHERE wl.task_id = t.task_id "
                + "), 0) AS actual_manhours, "
                + "t.progress, "
                + "t.status, "
                + "t.priority, "
                + "t.description, "
                + "DATE_FORMAT(t.c_at, '%Y-%m-%d %H:%i:%s') AS c_at, "
                + "DATE_FORMAT(t.u_at, '%Y-%m-%d %H:%i:%s') AS u_at "
                + "FROM tasks t "
                + "INNER JOIN Projects p ON t.project_id = p.project_id "
                + "LEFT JOIN Users u ON t.manager_id = u.user_id ";
	}
	
	/** 一覧表示順 */
    private static final String ORDER_BY_DUE_DATE_ASC = " ORDER BY t.due_date ASC, t.c_at DESC";

	
	 /**
     * ResultSetの1行をTaskDTOへ変換する
     * SELECT系メソッドの詰め替え処理を共通化する
     * @param rs SQL取得結果
     * @return タスクDTO
     * @throws SQLException SQLエラー
     */
    private TaskDTO setToTaskDTO(ResultSet rs) throws SQLException {

        TaskDTO taskDto = new TaskDTO();

        // タスクIDを設定する
        taskDto.setTaskId(rs.getInt("task_id"));

        // タスク名を設定する
        taskDto.setTaskName(rs.getString("task_name"));

        // 案件IDを設定する
        taskDto.setProjectId(rs.getInt("project_id"));

        // 案件名を設定する
        taskDto.setProjectName(rs.getString("project_name"));

        // 担当者IDを設定する
        taskDto.setManagerId(rs.getInt("manager_id"));

        // 担当者名を設定する
        taskDto.setManagerName(rs.getString("manager_name"));

        // 開始日をStringで設定する
        taskDto.setStartDate(rs.getString("start_date"));

        // 期限日をStringで設定する
        taskDto.setDueDate(rs.getString("due_date"));

        // 見積工数をfloatで設定する
        taskDto.setEstimatedManhours(rs.getFloat("estimated_manhours"));

        // 実績工数をfloatで設定する
        taskDto.setActualManhours(rs.getFloat("actual_manhours"));

        // 進捗率を設定する
        taskDto.setProgress(rs.getInt("progress"));

        // ステータスを設定する
        taskDto.setStatus(rs.getString("status"));

        // 優先度を設定する
        taskDto.setPriority(rs.getString("priority"));

        // 説明を設定する
        taskDto.setDescription(rs.getString("description"));

        // 作成日時をStringで設定する
        taskDto.setCreatedAt(rs.getDate("c_at"));

        // 更新日時をStringで設定する
        taskDto.setUpdatedAt(rs.getDate("u_at"));

        return taskDto;
    }
    
    /**
     * yyyy-MM-dd形式の文字列をSQL用Dateへ変換する
     * 未入力の場合はnullを返す
     * @param value 日付文字列
     * @return SQL用Date
     */
    private Date toSqlDate(String value) {

        // 未入力の場合はnullをDBへ渡す
        if (!hasText(value)) {
            return null;
        }

        // yyyy-MM-dd形式をDateへ変換する
        return Date.valueOf(value);
    }
    
    /**
     * 文字列が入力されているかチェック
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

}