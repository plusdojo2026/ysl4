package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
			dto.setCreatedAt(rs.getTimestamp("c_at"));
			dto.setUpdatedAt(rs.getTimestamp("u_at"));
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
			dto.setCreatedAt(rs.getTimestamp("c_at"));
			dto.setUpdatedAt(rs.getTimestamp("u_at"));
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
			dto.setCreatedAt(rs.getTimestamp("c_at"));
			dto.setUpdatedAt(rs.getTimestamp("u_at"));
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
			dto.setCreatedAt(rs.getTimestamp("c_at"));
			dto.setUpdatedAt(rs.getTimestamp("u_at"));
			TaskList.add(dto);
		}
		//serviceに返却する
		return TaskList;
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
			dto.setCreatedAt(rs.getTimestamp("c_at"));
			dto.setUpdatedAt(rs.getTimestamp("u_at"));

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
		pStmt.setInt(6, dto.getEstimatedManhours());
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
		pStmt.setInt(6, dto.getEstimatedManhours());
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

}