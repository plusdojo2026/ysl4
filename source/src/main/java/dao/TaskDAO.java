package dao;

import java.sql.Connection;
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

	//全タスクの一覧を取得する
	public ArrayList<TaskDTO> selectAll() throws SQLException {
		ArrayList<TaskDTO> TaskList = new ArrayList<TaskDTO>();

		// SELECT文の下準備
		String sql = "SELECT * FROM Tasks";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// コネクションとSQLをまとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行(executeQuery)し、結果表をResultSetに取得する
		ResultSet rs = pStmt.executeQuery();

		//reはDAOでしか使えないのでArrayList移し替え
		while (rs.next()) {
			TaskDTO dto = new TaskDTO();
			dto.setTaskId(rs.getInt("task_id"));
			dto.setTaskName(rs.getString("task_name"));
			dto.setProjectId(rs.getInt("project_id"));
			dto.setManagerId(rs.getInt("manager_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setEstimatedManhours(rs.getInt("estimated_manhours"));
			dto.setProgress(rs.getInt("progress"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setDescription(rs.getString("description"));
			dto.setCreatedAt(rs.getDate("c_at"));
			dto.setUpdatedAt(rs.getDate("u_at"));
			TaskList.add(dto);
		}
		//serviceに返却する
		return TaskList;
	}

	//条件に合うタスクを取得する
	public ArrayList<TaskDTO> selectByProjectId(int projectId)
			throws SQLException {
		ArrayList<TaskDTO> TaskList = new ArrayList<TaskDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM Tasks WHERE project_id = ?";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, projectId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		//移し替え
		while (rs.next()) {
			TaskDTO dto = new TaskDTO();
			dto.setTaskId(rs.getInt("task_id"));
			dto.setTaskName(rs.getString("task_name"));
			dto.setProjectId(rs.getInt("project_id"));
			dto.setManagerId(rs.getInt("manager_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setEstimatedManhours(rs.getInt("estimated_manhours"));
			dto.setProgress(rs.getInt("progress"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setDescription(rs.getString("description"));
			dto.setCreatedAt(rs.getDate("c_at"));
			dto.setUpdatedAt(rs.getDate("u_at"));
			TaskList.add(dto);
		}
		//serviceに返却する
		return TaskList;
	}

	//検索したタスクを取得する
	public ArrayList<TaskDTO> search(String condition) throws SQLException {
		ArrayList<TaskDTO> TaskList = new ArrayList<TaskDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM Tasks WHERE task_name LIKE '%?%' ";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		//移し替え
		while (rs.next()) {
			TaskDTO dto = new TaskDTO();
			dto.setTaskId(rs.getInt("task_id"));
			dto.setTaskName(rs.getString("task_name"));
			dto.setProjectId(rs.getInt("project_id"));
			dto.setManagerId(rs.getInt("manager_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setEstimatedManhours(rs.getInt("estimated_manhours"));
			dto.setProgress(rs.getInt("progress"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setDescription(rs.getString("description"));
			dto.setCreatedAt(rs.getDate("c_at"));
			dto.setUpdatedAt(rs.getDate("u_at"));
			TaskList.add(dto);
		}
		//serviceに返却する
		return TaskList;
	}

	//指定案件の全てのタスク総数を取得する
	public int countAllByProjectId(int projectId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) FROM Tasks WHERE project_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, projectId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 初期値0
		int count = 0;

		// 結果
		if (rs.next()) {
			return rs.getInt(1);
		}

		// serviceに返却する falseの場合は初期値
		return count;
	}

	//完了したタスク数を取得する
	public int countCompletedByProjectId(int projectId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) FROM Tasks WHERE project_id=? AND status = '完了'";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, projectId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 初期値0
		int count = 0;

		// 結果
		if (rs.next()) {
			return rs.getInt(1);
		}

		// serviceに返却する falseの場合は初期値
		return count;
	}

	//担当タスク数を取得する
	public int countAssignedTasks(int userId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) FROM Tasks WHERE manager_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, userId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 初期値0
		int count = 0;

		// 結果
		if (rs.next()) {
			return rs.getInt(1);
		}

		// serviceに返却する falseの場合は初期値
		return count;
	}

	//期限超過タスクを取得する
	public int countOverdueTasks(int userId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT(*) FROM Tasks"
				+ " WHERE manager_id=? AND due_date < CURRENT_DATE";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, userId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 初期値0
		int count = 0;

		// 結果
		if (rs.next()) {
			return rs.getInt(1);
		}

		// serviceに返却する falseの場合は初期値
		return count;
	}

	//担当タスク一覧を取得する
	public ArrayList<TaskDTO> selectAssignedTasks(int user) throws SQLException {
		ArrayList<TaskDTO> TaskList = new ArrayList<TaskDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM Tasks WHERE manager_id = ?";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, user);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		//移し替え
		while (rs.next()) {
			TaskDTO dto = new TaskDTO();
			dto.setTaskId(rs.getInt("task_id"));
			dto.setTaskName(rs.getString("task_name"));
			dto.setProjectId(rs.getInt("project_id"));
			dto.setManagerId(rs.getInt("manager_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setEstimatedManhours(rs.getInt("estimated_manhours"));
			dto.setProgress(rs.getInt("progress"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setDescription(rs.getString("description"));
			dto.setCreatedAt(rs.getDate("c_at"));
			dto.setUpdatedAt(rs.getDate("u_at"));
			TaskList.add(dto);
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
                + " ORDER BY t.due_date ASC, t.c_at DESC";

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
		String sql = "SELECT * FROM Tasks WHERE task_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, taskId);
		// SELECT文を実行し、結果表を取得する

		ResultSet rs = pStmt.executeQuery();

		// 移し替え
		while (rs.next()) {

			dto = new TaskDTO();
			dto.setTaskId(rs.getInt("task_id"));
			dto.setTaskName(rs.getString("task_name"));
			dto.setProjectId(rs.getInt("project_id"));
			dto.setManagerId(rs.getInt("manager_id"));
			dto.setStartDate(rs.getString("start_date"));
			dto.setDueDate(rs.getString("due_date"));
			dto.setEstimatedManhours(rs.getInt("estimated_manhours"));
			dto.setProgress(rs.getInt("progress"));
			dto.setStatus(rs.getString("status"));
			dto.setPriority(rs.getString("priority"));
			dto.setDescription(rs.getString("description"));
			dto.setCreatedAt(rs.getDate("c_at"));
			dto.setUpdatedAt(rs.getDate("u_at"));

		}

		// serviceに返却する
		return dto;
	}


	//タスク登録をする
	public int taskInsert(TaskDTO dto) throws SQLException {

		// SELECT文を準備する
		String sql = "INSERT INTO Tasks(" +
				"task_name, project_id, manager_id, " +
				"start_date, due_date, estimated_manhours, " +
				"progress, status, priority, description) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する ResultSet rs = pStmt.executeQuery();

		// 移し替え
		pStmt.setString(1, dto.getTaskName());
		pStmt.setInt(2, dto.getProjectId());
		pStmt.setInt(3, dto.getManagerId());
		pStmt.setString(4, dto.getStartDate());
		pStmt.setString(5, dto.getDueDate());
		pStmt.setFloat(6, dto.getEstimatedManhours());
		pStmt.setInt(7, dto.getProgress());
		pStmt.setString(8, dto.getStatus());
		pStmt.setString(9, dto.getPriority());
		pStmt.setString(10, dto.getDescription());

		return pStmt.executeUpdate();

	}

	//タスクを更新する
	public int taskUpdate(TaskDTO dto) throws SQLException {

		// SELECT文を準備する
		String sql = "UPDATE Tasks SET task_name=?, project_id=?, manager_id=?"
				+ ", start_date=?, due_date=?, estimated_manhours=?, progress=?"
				+ ", status=?, priority=?, description=? WHERE task_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する ResultSet rs = pStmt.executeQuery();

		// 移し替え
		pStmt.setString(1, dto.getTaskName());
		pStmt.setInt(2, dto.getProjectId());
		pStmt.setInt(3, dto.getManagerId());
		pStmt.setString(4, dto.getStartDate());
		pStmt.setString(5, dto.getDueDate());
		pStmt.setFloat(6, dto.getEstimatedManhours());
		pStmt.setInt(7, dto.getProgress());
		pStmt.setString(8, dto.getStatus());
		pStmt.setString(9, dto.getPriority());
		pStmt.setString(10, dto.getDescription());
		pStmt.setInt(11, dto.getTaskId());

		return pStmt.executeUpdate();

	}

	//進捗・ステータスを更新する
	public int updateStatusAndProgress(int taskId, String status, int progress)
			throws SQLException {

		// SELECT文を準備する
		String sql = "UPDATE Tasks SET status = ?, progress = ? WHERE task_id = ?";

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
		String sql = "DELETE FROM Tasks WHERE task_id = ?";

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
                + "    FROM WorkLogs wl "
                + "    WHERE wl.task_id = t.task_id "
                + "), 0) AS actual_manhours, "
                + "t.progress, "
                + "t.status, "
                + "t.priority, "
                + "t.description, "
                + "DATE_FORMAT(t.c_at, '%Y-%m-%d %H:%i:%s') AS c_at, "
                + "DATE_FORMAT(t.u_at, '%Y-%m-%d %H:%i:%s') AS u_at "
                + "FROM Tasks t "
                + "INNER JOIN Projects p ON t.project_id = p.project_id "
                + "LEFT JOIN Users u ON t.manager_id = u.user_id";
	}
	
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

}