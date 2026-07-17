package Service;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import DAO.WorkLogDAO;
import Model.WorkLogDTO;

public class WorkLogService extends DBAccess {
	//親クラスのDBAccessが実行される
	//ここでデータベースに接続するメゾットを呼び出している（親メゾットがデータベースに接続する）
	public WorkLogService() {
		super.access();
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
	public int WorkLogRegist(
			int workLogsId,
			int taskId,
			int userId,
			Date workDate,
			float manHours,
			String jobContents,
			Timestamp cAt,
			Timestamp uAt) {
		//DAOに処理を任せる
		WorkLogDAO dao = new WorkLogDAO(conn);
		int ans = 0;
		try {
			ans = dao.WorkLogRegist(
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
	public int WorkLogDelete(int workLogsId) {
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

}
