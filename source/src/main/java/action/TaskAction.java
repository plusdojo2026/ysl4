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

		return "/WEB-INF/jsp/taskList.jsp";
	}

}