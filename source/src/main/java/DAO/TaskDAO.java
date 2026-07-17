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

	//タスクの一覧を取得するメソッド
	public ArrayList<TaskDTO> selectAll() throws SQLException {
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

}