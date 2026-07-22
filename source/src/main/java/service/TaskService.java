package Service;

import java.sql.SQLException;
import java.util.ArrayList;

import DAO.TaskDAO;
import Model.TaskDTO;

public class TaskService extends DBAccess {

	public TaskService() {
		try {
			super.access();
		} catch (SQLException e) {
			System.out.println("DB接続に失敗しました");
			e.printStackTrace();
		}
	}

	// 全件取得
	public ArrayList<TaskDTO> selectAll() {

		ArrayList<TaskDTO> taskList = null;

		TaskDAO dao = new TaskDAO(super.conn);

		try {
			taskList = dao.selectAll();
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}

		return taskList;
	}

	// プロジェクトIDで検索
	public ArrayList<TaskDTO> selectByProjectId(int projectId) {

		ArrayList<TaskDTO> taskList = null;

		TaskDAO dao = new TaskDAO(super.conn);

		try {
			taskList = dao.selectByProjectId(projectId);
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}

		return taskList;
	}

	// タスクID検索
	public TaskDTO findById(int taskId) {

		TaskDTO dto = null;

		TaskDAO dao = new TaskDAO(super.conn);

		try {
			dto = dao.findById(taskId);
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}

		return dto;
	}

	// タスク登録
	public int regist(TaskDTO dto) {

		int ans = 0;

		TaskDAO dao = new TaskDAO(super.conn);

		try {
			ans = dao.taskInsert(dto);

			if (ans > 0) {
				super.commit();
			} else {
				super.rollback();
			}

		} catch (SQLException e) {
			super.rollback();
			e.printStackTrace();
		}

		return ans;
	}

	// タスク更新
	public int update(TaskDTO dto) {

		int ans = 0;

		TaskDAO dao = new TaskDAO(super.conn);

		try {
			ans = dao.taskUpdate(dto);

			if (ans > 0) {
				super.commit();
			} else {
				super.rollback();
			}

		} catch (SQLException e) {
			super.rollback();
			e.printStackTrace();
		}

		return ans;
	}

	// ステータス・進捗更新
	public int changeStatus(
			int taskId,
			String status,
			int progress) {

		int ans = 0;

		TaskDAO dao = new TaskDAO(super.conn);

		try {

			ans = dao.updateStatusAndProgress(
					taskId,
					status,
					progress);

			if (ans > 0) {
				super.commit();
			} else {
				super.rollback();
			}

		} catch (SQLException e) {
			super.rollback();
			e.printStackTrace();
		}

		return ans;
	}

	// タスク削除
	public int delete(int taskId) {

		int ans = 0;

		TaskDAO dao = new TaskDAO(super.conn);

		try {

			ans = dao.taskDelete(taskId);

			if (ans > 0) {
				super.commit();
			} else {
				super.rollback();
			}

		} catch (SQLException e) {
			super.rollback();
			e.printStackTrace();
		}

		return ans;
	}

	// 担当タスク一覧取得
	public ArrayList<TaskDTO> selectAssignedTasks(int userId) {

		ArrayList<TaskDTO> taskList = null;

		TaskDAO dao = new TaskDAO(super.conn);

		try {
			taskList = dao.selectAssignedTasks(userId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return taskList;
	}

	// 終了処理
	public void closeConnection() {
		super.close();
	}
}