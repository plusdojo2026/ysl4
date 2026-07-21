package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.TaskDTO;

public class TaskDAO {

	public Connection conn = null;

	//コネクションを保持するコンストラクタ
	public TaskDAO(Connection conn) {
		this.conn = conn;
	}

	//タスクの一覧を取得する
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
			dto.setStartDate(rs.getDate("start_date"));
			dto.setDueDate(rs.getDate("due_date"));
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

	//案件一覧を取得する
	public ArrayList<TaskDTO> selectByProjectId() throws SQLException {
		ArrayList<TaskDTO> TaskList = new ArrayList<TaskDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM Tasks";
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
			dto.setStartDate(rs.getDate("start_date"));
			dto.setDueDate(rs.getDate("due_date"));
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

	//検索のための一覧を取得する
	public ArrayList<TaskDTO> search() throws SQLException {
		ArrayList<TaskDTO> TaskList = new ArrayList<TaskDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM Tasks";
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
			dto.setStartDate(rs.getDate("start_date"));
			dto.setDueDate(rs.getDate("due_date"));
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
	public TaskDTO findById(int projectId) throws SQLException {

		TaskDTO dto = null;

		// SELECT文を準備する
		String sql = "SELECT * FROM projects WHERE project_id=?";

		// デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		pStmt.setInt(1, projectId);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		// 移し替え
		while (rs.next()) {

			dto = new TaskDTO();
			dto.setTaskId(rs.getInt("task_id"));
			dto.setTaskName(rs.getString("task_name"));
			dto.setProjectId(rs.getInt("project_id"));
			dto.setManagerId(rs.getInt("manager_id"));
			dto.setStartDate(rs.getDate("start_date"));
			dto.setDueDate(rs.getDate("due_date"));
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

	//全てのProjectIDを取得する
	public int countAllByProjectId(int projectId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT * FROM Tasks WHERE project_id=?";

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

	//完了したProjectIDを取得する
	public int countCompletedByProjectId(int projectId) throws SQLException {

		// SELECT文を準備する
		String sql = "SELECT COUNT * FROM Tasks WHERE project_id=? AND status = '完了'";

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
	
	//担当タスクについてuserIDを取得する
		public int countAssignedTasks(int userId) throws SQLException {

			// SELECT文を準備する
			String sql = "SELECT COUNT * FROM Tasks WHERE manager_id=?";

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
		

}