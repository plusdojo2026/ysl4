package action;

import java.io.UnsupportedEncodingException;
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

	//プロジェクトID検索
	public String selectByProjectId() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskList.jsp";
		
		//ユーザーが操作した情報を受け取る
		String keyword = request.getParameter("keyword");
		
		//Serviceに接続する
		//TaskService service = new TaskService();
		
		TaskService searchservice = new TaskService();
		ArrayList<TaskDTO> taskList = searchservice.selectByProjectId(0);
		request.setAttribute("taskList", taskList);
		request.setAttribute("keyword", keyword);
		return page;
	}
	
	//タスクID検索
		public String search() throws UnsupportedEncodingException {
			String page = "/WEB-INF/jsp/taskList.jsp";
			
			//ユーザーが操作した情報を受け取る
			String keyword = request.getParameter("keyword");
			
			//Serviceに接続する 一覧所得
			//TaskService service = new TaskService();
			
			//検索語の一覧表示
			TaskService searchservice = new TaskService();
//			ArrayList<TaskDTO> taskList = searchservice.findById(0);
//			request.setAttribute("taskList", taskList);
			request.setAttribute("keyword", keyword);
			return page;
		}

}