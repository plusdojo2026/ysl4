package action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
		String page = "/WEB-INF/jsp/taskDetail.jsp";
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

	//工数の削除メソッド---------------------------------------
	public String WorkLogDelete() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskDetail.jsp";

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

	//指定タスクの工数ログまとめて削除するメソッド---------------------------------------
	public String DeleteByTaskId() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskDetail.jsp";

		//値の取得
		request.setCharacterEncoding("UTF-8");
		int taskId = Integer.parseInt(request.getParameter("task_id"));

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		int ans = service.DeleteByTaskId(taskId);
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

	//指定タスクの実績工数を取得するメソッド---------------------------------------
	public String sumBytaskId() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskDetail.jsp";

		//値の取得
		request.setCharacterEncoding("UTF-8");
		int taskId = Integer.parseInt(request.getParameter("task_id"));

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		float ans = service.sumBytaskId(taskId);

		//ユーザー情報を全て取得する
		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}

	//指定案件の合計工数を取得するメソッド---------------------------------------
	public String sumByProjectId() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskDetail.jsp";

		//値の取得
		request.setCharacterEncoding("UTF-8");
		int projectId = Integer.parseInt(request.getParameter("project_id"));

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		float ans = service.sumByProjectId(projectId);

		//ユーザー情報を全て取得する
		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}

	//指定タスクの工数ログを確認するメソッド---------------------------------------
	public String selectByTaskId() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskDetail.jsp";

		ArrayList<WorkLogDTO> WorkLogList = new ArrayList<>();

		//値の取得
		request.setCharacterEncoding("UTF-8");
		int taskId = Integer.parseInt(request.getParameter("task_id"));

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		WorkLogList = service.selectByTaskId(taskId);

		//ユーザー情報を全て取得する
		//		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}

	//指定案件の最新工数ログを取得 案件詳細の最新ログを表示---------------------------------------
	public String selectRateByProject() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskDetail.jsp";

		List<WorkLogDTO.projectWorkLogDTO> WorkLogList = new ArrayList<>();
		//値の取得
		request.setCharacterEncoding("UTF-8");
		int taskId = Integer.parseInt(request.getParameter("task_id"));

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		WorkLogList = service.selectRateByProject(taskId);

		//ユーザー情報を全て取得する
		//		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}

	////指定月の工数ログを取得　月次集計CVS出力---------------------------------------
	public String selectByMonth() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/taskDetail.jsp";

		ArrayList<WorkLogDTO> WorkLogList = new ArrayList<>();
		//値の取得
		request.setCharacterEncoding("UTF-8");
		int workLogsId = Integer.parseInt(request.getParameter("work_logs_id"));
		String workDate = request.getParameter("work_date");

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		WorkLogList = service.selectByMonth(workLogsId, workDate);

		//ユーザー情報を全て取得する
		//		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}
}