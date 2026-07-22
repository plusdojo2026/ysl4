package service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import dao.WorkLogDAO;
import model.WorkLogDTO;

public class WorkLogService extends DBAccess {
	//親クラスのDBAccessが実行される
	//ここでデータベースに接続するメゾットを呼び出している（親メゾットがデータベースに接続する）
	public WorkLogService() {
		try {
			super.access();
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	//全てのユーザー情報を取得するメソッド---------------------------------------
	public ArrayList<WorkLogDTO> selectAll() {
		ArrayList<WorkLogDTO> userList = null;

		WorkLogDAO dao = new WorkLogDAO(super.conn);
		try {
			userList = dao.selectAll();
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}
		super.close();

		return userList;

	}

	//工数を登録するメソッド---------------------------------------
	public int WorkLogInsert(
			int workLogsId,
			int taskId,
			int userId,
			String workDate,
			float manHours,
			String jobContents,
			Timestamp cAt,
			Timestamp uAt) {
		//DAOに処理を任せる
		WorkLogDAO dao = new WorkLogDAO(conn);
		int ans = 0;
		try {
			ans = dao.WorkLogInsert(
					workLogsId,
					taskId,
					userId,
					workDate,
					manHours,
					jobContents,
					cAt,
					uAt);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return ans;
	}

	//ユーザーを削除するメソッド---------------------------------------
	public int WorkLogDelete(String workLogsId) {
		//DAOに処理を任せる
		WorkLogDAO dao = new WorkLogDAO(conn);
		int ans = 0;
		try {
			ans = dao.WorkLogDelete(workLogsId);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return ans;
	}

	//指定タスクの工数ログをまとめて削除
	public int deleteByTaskId(int taskId) {
		WorkLogDAO dao = new WorkLogDAO(conn);
		int ans = 0;
		try {
			ans = dao.DeleteBytaskId(taskId);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return ans;
	}

	//指定タスクの実績工数合計を取得
	public float sumBytaskId(int taskId) {
		WorkLogDAO dao = new WorkLogDAO(conn);
		float ans = 0;
		try {
			ans = dao.sumBytaskId(taskId);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return ans;
	}

	//指定案件の合計工数を取得
	public float sumByProjectId(int projectId) {
		WorkLogDAO dao = new WorkLogDAO(conn);
		float ans = 0;
		try {
			ans = dao.sumBytaskId(projectId);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return ans;
	}

	//指定タスクの工数ログを確認
	public ArrayList<WorkLogDTO> selectByTaskId() {
		ArrayList<WorkLogDTO> taskWorkLogList = null;

		WorkLogDAO dao = new WorkLogDAO(super.conn);
		try {
			taskWorkLogList = dao.selectAll();
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}
		super.close();

		return taskWorkLogList;
	}

	//指定案件の最新工数ログを取得 案件詳細の最新ログを表示
	public ArrayList<WorkLogDTO.projectWorkLogDTO> selectRateByProject() {
		ArrayList<WorkLogDTO.projectWorkLogDTO> newtaskWorkLogList = null;

		WorkLogDAO dao = new WorkLogDAO(super.conn);
		try {
			newtaskWorkLogList = dao.selectRateByProject();
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}
		super.close();

		return newtaskWorkLogList;
	}

	//指定月の工数ログを取得　月次集計CVS出力
	public ArrayList<WorkLogDTO> selectByMonth() {
		ArrayList<WorkLogDTO> monthWorkLogList = null;

		WorkLogDAO dao = new WorkLogDAO(super.conn);
		try {
			monthWorkLogList = dao.selectAll();
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}
		super.close();

		return monthWorkLogList;
	}
}
