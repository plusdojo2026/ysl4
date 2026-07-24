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
		//				ArrayList<TaskDTO> taskList = searchservice.search(0);
		//				request.setAttribute("taskList", taskList);
		request.setAttribute("keyword", keyword);
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

		int taskId = Integer.parseInt(
				request.getParameter("taskId"));

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

}