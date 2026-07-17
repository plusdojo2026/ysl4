package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import Model.WorkLogDTO;

public class WorkLogDAO {

	public Connection conn = null;

	//コネクションを保持するコンストラクタ
	public WorkLogDAO(Connection conn) {
		this.conn = conn;
	}

	//ユーザーの一覧を取得するメソッド
	public ArrayList<WorkLogDTO> selectAll() throws SQLException {
		ArrayList<WorkLogDTO> workLogList = new ArrayList<WorkLogDTO>();

		// SELECT文を準備する
		String sql = "SELECT * FROM work_logs";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		// SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		//移し替え
		while (rs.next()) {
			WorkLogDTO dto = new WorkLogDTO(0, 0, 0, null, 0, sql, null, null);
			dto.setWorkLogsId(rs.getInt("work_logs_id"));
			dto.setTaskId(rs.getInt("task_id"));
			dto.setUserId(rs.getInt("user_id"));
			dto.setWorkDate(rs.getDate("work_date"));
			dto.setManHours(rs.getFloat("man_hours"));
			dto.setJobContents(rs.getString("job_contents"));
			dto.setcAt(rs.getTimestamp("c_at"));
			dto.setuAt(rs.getTimestamp("u_at"));
			workLogList.add(dto);
		}
		//serviceに返却する
		return workLogList;
	}

	//ユーザー登録のメソッド---------------------------------------
	public int WorkLogInsert(
			int workLogsId,
			int taskId,
			int userId,
			Date workDate,
			float manHours,
			String jobContents,
			Timestamp cAt,
			Timestamp uAt) throws SQLException {

		int ans = 0;
		// SELECT文を準備する
		String sql = "INSERT INTO work_logs VALUES(?,?,?,?,?,?,?,?)";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		pStmt.setInt(1, workLogsId);
		pStmt.setInt(2, taskId);
		pStmt.setInt(3, userId);
		pStmt.setDate(4, workDate);
		pStmt.setFloat(5, manHours);
		pStmt.setString(6, jobContents);
		pStmt.setTimestamp(7, cAt);
		pStmt.setTimestamp(8, uAt);

		// SELECT文を実行し、結果表を取得する
		ans = pStmt.executeUpdate();

		//serviceに返却する
		return ans;
	}

	//ユーザー削除用のメソッド---------------------------------------
	public int WorkLogDelete(int workLogsId) throws SQLException {

		int ans = 0;
		// SELECT文を準備する
		String sql = "DELETE FROM user WHERE work_logs_id = ?";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		// SQL文実際に挿入、当てはめる
		pStmt.setInt(1, workLogsId);

		// SELECT文を実行し、結果表を取得する
		ans = pStmt.executeUpdate();

		//serviceに返却する
		return ans;
	}

	public int DeleteBytaskId(int workLogsId) throws SQLException {
		int ans = 0;
		// SELECT文を準備する
		String sql = "DELETE FROM user WHERE task_id = ?";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		// SQL文実際に挿入、当てはめる
		pStmt.setInt(1, workLogsId);

		// SELECT文を実行し、結果表を取得する
		ans = pStmt.executeUpdate();

		//serviceに返却する
		return ans;
	}

	public float sumBytaskId(float manHours) throws SQLException {
		int ans = 0;
		// SELECT文を準備する
		String sql = "SELECT task_id,SUM(work_logs) FROM work_logs";
		//デバッグ（SQL文の確認用）
		System.out.println(sql);

		// まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		// SQL文実際に挿入、当てはめる
		pStmt.setFloat(1, manHours);

		// SELECT文を実行し、結果表を取得する
		ans = pStmt.executeUpdate();

		//serviceに返却する
		return ans;
	}

	public float sumByProjectId(int projectId) throws SQLException {

	}

	public ArrayList<WorkLogDTO> selectByTaskId(taskId) throws SQLException {
		
	}

	public ArrayList<WorkLogDTO> selectRateByProject(projectId) throws SQLException {
		
	}

	public ArrayList<WorkLogDTO> selectByMonth(targetMonth) throws SQLException {
		
	}
}