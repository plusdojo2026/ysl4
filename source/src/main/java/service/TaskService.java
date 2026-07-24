package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ProjectsDAO;
import dao.TaskDAO;
import dao.UserDAO;
import dao.WorkLogDAO;
import model.ProjectsDTO;
import model.TaskDTO;
import model.UserDTO;

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

	// キーワード検索
	public ArrayList<TaskDTO> search(String condition) {

		ArrayList<TaskDTO> taskList = null;

		TaskDAO dao = new TaskDAO(super.conn);

		try {
			taskList = dao.search(condition);
		} catch (SQLException e) {
			System.out.println("SQL文おかしいよ");
			e.printStackTrace();
		}

		return taskList;
	}

	// 案件検索
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

	// タスクID検索

	public TaskDTO findDetail(int taskId) throws SQLException {

		// findByIdメソッドでタスク情報を取得
		TaskDTO taskDto = findById(taskId);

		// タスクが存在しない場合
		if (taskDto == null) {
			return null;
		}

		// 工数ログを取得
		WorkLogDAO workLogDao = new WorkLogDAO(conn);

		taskDto.setWorkLogs(workLogDao.selectByTaskId(taskId));

		return taskDto;
	}
	//  ボツ
	//	public TaskDTO findDetail(int taskId) {
	//
	//		TaskDTO dto = null;
	//
	//		TaskDAO dao = new TaskDAO(super.conn);
	//
	//		try {
	//			dto = dao.findById(taskId);
	//		} catch (SQLException e) {
	//			System.out.println("SQL文おかしいよ");
	//			e.printStackTrace();
	//		}
	//
	//		return dto;
	//	}

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
	public TaskDTO getTaskFormData(int projectId) {

		//DTO飛び出し
		TaskDTO formData = new TaskDTO();

		try {

			//DB接続
			access();

			//案件一覧を取得
			ProjectsDAO projectsDao = new ProjectsDAO(conn);
			List<ProjectsDTO> projectList = projectsDao.selectAll();

			//有効ユーザー一覧を取得
			UserDAO userDao = new UserDAO(conn);
			List<UserDTO> userList = userDao.selectValidUsers();

			//DTOにセット
			formData.setProjectList(projectList);
			formData.setUserList(userList);

			//選択中の案件を取得
			if (projectId > 0) {
				formData.setProjectId(projectId);
			}

			commit();

		} catch (SQLException e) {

			rollback();
			e.printStackTrace();

		} finally {

			close();
		}

		return formData;
	}
}