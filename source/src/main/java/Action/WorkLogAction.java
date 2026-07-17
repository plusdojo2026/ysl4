package Action;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import Model.WorkLogDTO;
import Service.WorkLogService;

public class WorkLogAction {

	HttpServletRequest request;

	//コンストラクタ
	public WorkLogAction(HttpServletRequest request) {
		this.request = request;
	}

	//工数の登録メソッド---------------------------------------
	public String regist() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/menu.jsp";
		//		dto.setWorkLogsId(rs.getInt("work_logs_id"));
		//		dto.setTaskId(rs.getInt("task_id"));
		//		dto.setUserId(rs.getInt("user_id"));
		//		dto.setWorkDate(rs.getDate("work_date"));
		//		dto.setManHours(rs.getFloat("man_hours"));
		//		dto.setJobContents(rs.getString("job_contents"));
		//		dto.setcAt(rs.getTimestamp("c_at"));
		//		dto.setuAt(rs.getTimestamp("u_at"));
		//		workLogList.add(dto);
		//値の取得
		request.setCharacterEncoding("UTF-8");
		int workLogsId = Integer.parseInt(request.getParameter("work_logs_id"));
		int taskId = Integer.parseInt(request.getParameter("task_id"));
		int userId = Integer.parseInt(request.getParameter("user_id"));
		Date workDate = request.getParameter("work_date");
		float manHours = request.getParameter("man_hours");
		String jobContents = request.getParameter("job_contents");
		Timestamp cAt = request.getParameter("c_at");
		Timestamp uAt = request.getParameter("u_at");

		//ユーザー情報を全て取得する
		WorkLogService service = new WorkLogService();
		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);
		return page;

		WorkLogService service = new WorkLogService();
		//serviceに処理を依頼
		int ans = service.WorkLogRegist(
				workLogsId,
				taskId,
				userId,
				workDate,
				manHours,
				jobContents,
				cAt,
				uAt);
		//ちゃんと登録できたか確認
		if (ans == 1) {
			request.setAttribute("msg", "※" + name + "の登録完了！");
		} else {
			request.setAttribute("msg", "※登録失敗！IDが重複しています");
		}
		//工数情報を全て取得する
		ArrayList<WorkLogDTO> WorkLogList = service.selectAll();
		request.setAttribute("WorkLogList", WorkLogList);

		return page;
	}

	//従業員の削除メソッド---------------------------------------
	public String delete() throws UnsupportedEncodingException {
		String page = "/WEB-INF/jsp/menu.jsp";

		//値の取得
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");

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