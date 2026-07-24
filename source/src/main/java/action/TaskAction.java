package action;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import model.TaskDTO;
import service.TaskService;

public class TaskAction {

	HttpServletRequest request;

	//コンストラクタ
	public TaskAction(HttpServletRequest request) {
		this.request = request;
	}

	//タスク一覧表示
	public String list() throws UnsupportedEncodingException {

		String page = "/WEB-INF/jsp/taskList.jsp";

		TaskService service = new TaskService();

		//タスク情報を全て取得する
		TaskService selectservice = new TaskService();

		ArrayList<TaskDTO> taskList = selectservice.selectAll();

		request.setAttribute("taskList", taskList);

		return page;
	}

	//キーワード検索
	public String search() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskList.jsp";

		//ユーザーが操作した情報を受け取る
		String keyword = request.getParameter("keyword");

		//Serviceに接続する 一覧所得
		//TaskService service = new TaskService();

		//検索語の一覧表示
		TaskService searchservice = new TaskService();
		ArrayList<TaskDTO> taskList = searchservice.search(keyword);
		//				ArrayList<TaskDTO> taskList = searchservice.search(0);
		//				request.setAttribute("taskList", taskList);
		request.setAttribute("taskList", taskList);
		return page;
	}

	//　！！キーワード検索メモ！！
	//	public String search() throws UnsupportedEncodingException {
	//		String page = "/WEB-INF/jsp/taskList.jsp";
	//
	//		//ユーザーが操作した情報を受け取る
	//		String keyword = request.getParameter("keyword");
	//
	//		//Serviceに接続する 一覧所得
	//		//TaskService service = new TaskService();
	//
	//		//検索語の一覧表示
	//		TaskService searchservice = new TaskService();
	//		//				ArrayList<TaskDTO> taskList = searchservice.search(0);
	//		//				request.setAttribute("taskList", taskList);
	//		request.setAttribute("keyword", keyword);
	//		return page;
	//	}

	//プロジェクトID検索
	public String selectByProjectId() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskList.jsp";

		//ユーザーが操作した情報を受け取る
		String keyword = request.getParameter("keyword");

		TaskService searchservice = new TaskService();

		ArrayList<TaskDTO> taskList = searchservice.selectByProjectId(0);

		request.setAttribute("taskList", taskList);

		request.setAttribute("keyword", keyword);
		return page;
	}

	//タスクID検索
	public String findDetail() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskList.jsp";

		//ユーザーが操作した情報を受け取る
		String keyword = request.getParameter("keyword");

		//検索語の一覧表示
		TaskService searchservice = new TaskService();

		request.setAttribute("keyword", keyword);
		return page;
	}

	//詳細詳細
	public String detail() {

		String page = "/WEB-INF/jsp/taskDetail.jsp";

		int taskId = Integer.parseInt(request.getParameter("taskId"));

		TaskService service = new TaskService();

		TaskDTO task = null;
		try {
			task = service.findDetail(taskId);
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		request.setAttribute("task", task);

		return page;
	}

	// 登録画面表示
	public String showRegist() {

		return "/WEB-INF/jsp/taskRegist.jsp";

	}

	//タスク登録
	public String regist() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/projectDetail.jsp";

		//値の取得
		request.setCharacterEncoding("UTF-8");
		String projectId = request.getParameter("projectId");
		String taskName = request.getParameter("taskName");
		String status = request.getParameter("status");
		String priority = request.getParameter("priority");

		//入力値チェック
		if (projectId == null || taskName == null || status == null || priority == null) {
			request.setAttribute("msg", "※必須項目を入力してください");
		}

		TaskDTO dto = new TaskDTO();

		dto.setProjectId(Integer.parseInt(projectId));
		dto.setTaskName(taskName);
		dto.setStatus(status);
		dto.setPriority(priority);

		TaskService service = new TaskService();

		//serviceに処理を依頼
		int ans = service.regist(dto);

		//ちゃんと登録できたか確認
		if (ans == 1) {
			request.setAttribute("msg", "※" + taskName + "の登録完了！");
		} else {
			request.setAttribute("msg", "※登録失敗！IDが重複しています");
		}

		return page;
	}

	//編集内容を表示する
	public String showUpdate() {

		String page = "/WEB-INF/jsp/projectEdit.jsp";

		int taskId = Integer.parseInt(
				request.getParameter("taskName"));

		TaskService service = new TaskService();

		TaskDTO dto = service.findById(taskId);

		request.setAttribute("dto", dto);
		return page;

	}

	//編集
	public String update() throws UnsupportedEncodingException {

		String page = "/WEB-INF/jsp/projectDetail.jsp";

		request.setCharacterEncoding("UTF-8");

		TaskDTO dto = new TaskDTO();

		dto.setTaskId(Integer.parseInt(request.getParameter("task_id")));
		dto.setTaskName(request.getParameter("task_name"));
		dto.setStatus(request.getParameter("task_status"));
		dto.setPriority(request.getParameter("task_priority"));

		TaskService service = new TaskService();

		int ans = service.update(dto);

		if (ans == 1) {
			page = "/TaskServlet?action=detail"
					+ "&taskId="
					+ dto.getTaskId();
		}

		return page;
	}

	//ステータス変更
	public String changeStatus() {

		String page = "/TaskServlet?action=list";

		int taskId = Integer.parseInt(request.getParameter("taskId"));

		String status = request.getParameter("status");
		
		int progres = Integer.parseInt(request.getParameter("progres"));

		TaskService service = new TaskService();

		service.changeStatus(taskId, status, progres);

		return page;
	}

	//削除
	public String delete() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/projectDetail.jsp";

		//値の取得
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("delete_button");

		//入力値チェック
		if (id == null) {
			request.setAttribute("msg", "IDが入力されていません。");
		}

		return page;
	}

}