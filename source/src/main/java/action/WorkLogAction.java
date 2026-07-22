package action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import model.WorkLogDTO;
import service.WorkLogService;

public class WorkLogAction {

	HttpServletRequest request;

	//コンストラクタ
	public WorkLogAction(HttpServletRequest request) {
		this.request = request;
	}

	//工数の登録メソッド---------------------------------------
	public String WorkLogRegist() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/menu.jsp";
		//値の取得
		request.setCharacterEncoding("UTF-8");
		int workLogsId = Integer.parseInt(request.getParameter("work_logs_id"));
		int taskId = Integer.parseInt(request.getParameter("task_id"));
		int userId = Integer.parseInt(request.getParameter("user_id"));
		String workDate = request.getParameter("work_date");
		float manHours = Float.parseFloat(request.getParameter("man_hours"));
		String jobContents = request.getParameter("job_contents");

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		int ans = service.WorkLogInsert(workLogsId, taskId, userId, workDate, manHours, jobContents, null, null);
		//ちゃんと登録できたか確認
		if (ans == 1) {
			request.setAttribute("msg", "※登録完了！");
		} else {
			request.setAttribute("msg", "※登録失敗！IDが重複しています");
		}

		//工数情報を全て取得する
		WorkLogService insetservice = new WorkLogService();
		ArrayList<WorkLogDTO> WorkLogList = insetservice.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}

	//従業員の削除メソッド---------------------------------------
	public String delete() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/menu.jsp";

		//値の取得
		request.setCharacterEncoding("UTF-8");
		String workLogsId = request.getParameter("work_logs_id");

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		int ans = service.WorkLogDelete(workLogsId);
		//ちゃんと削除できたか確認
		if (ans == 1) {
			request.setAttribute("msg", "※削除完了！");
		} else {
			request.setAttribute("msg", "※削除失敗！");
		}
		//ユーザー情報を全て取得する
		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}

}