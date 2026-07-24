package action;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.ProjectsDTO;
import service.ProjectService;

public class ProjectAction {
	HttpServletRequest request;

	//コンストラクタ
	public ProjectAction(HttpServletRequest request) {
		this.request = request;
	}

	public String list() {

		String page = "/WEB-INF/jsp/projectList.jsp";

		ProjectService service = new ProjectService();

		List<ProjectsDTO> projectList = service.selectAll();

		request.setAttribute("projectList", projectList);

		return page;

	}

	public String showRegist() {

		String page = "WEB-INF/jsp/projectRegist.jsp";

		return page;
	}
  //必須項目未入力エラー
	public String regist() throws UnsupportedEncodingException {

		String page = "/WEB-INF/jsp/projectRegist.jsp";

		request.setCharacterEncoding("UTF-8");
		String projectCode = request.getParameter("project_code");
		String projectName = request.getParameter("project_name");
		String projectManagerId = request.getParameter("project_manager_id");
		String status = request.getParameter("status");
		String estimatedManhours = request.getParameter("estimated_manhours");
		String priority = request.getParameter("priority");
		String startDate = request.getParameter("start_date");
		String dueDate = request.getParameter("due_date");

		if (projectCode.trim().isEmpty()
				|| projectName.trim().isEmpty()
				|| projectManagerId.trim().isEmpty()
				|| status.trim().isEmpty()
				|| priority.trim().isEmpty()
				|| estimatedManhours.trim().isEmpty()
				|| startDate.trim().isEmpty()
				|| dueDate.trim().isEmpty()) {

			request.setAttribute(
					"errorMessage", "※必須項目が入力されていません");

			return "/WEB-INF/jsp/projectRegist.jsp";
		}

		ProjectsDTO dto = new ProjectsDTO();

		dto.setProjectCode(projectCode);
		dto.setProjectName(projectName);
		dto.setProjectManagerId(Integer.parseInt(projectManagerId));
		dto.setStatus(status);
		dto.setPriority(priority);
		dto.setEstimatedManhours(Integer.parseInt(estimatedManhours));
		dto.setStartDate(startDate);
		dto.setDueDate(dueDate);

		//案件コード重複確認
		ProjectService service = new ProjectService();

		if (service.existsProjectCode(dto.getProjectCode())) {
			request.setAttribute(
					"errorMessage", "※同じ案件コードは利用出来ません");
			return page;
		}

		int ans = service.regist(dto);

		if (ans == 1) {
			page = "ProjectServlet?action=list";
		}

		return page;
	}

	public String detail() {

		String page = "/WEB-INF/jsp/projectDetail.jsp";

		int projectId = Integer.parseInt(

				request.getParameter("projectId"));

		ProjectService service = new ProjectService();

		ProjectsDTO dto = service.findDetail(projectId);

		request.setAttribute("dto", dto);
		return page;
	}

	public String showUpdate() {

		String page = "/WEB-INF/jsp/projectEdit.jsp";

		int projectId = Integer.parseInt(
				request.getParameter("projectId"));

		ProjectService service = new ProjectService();

		ProjectsDTO dto = service.findById(projectId);

		request.setAttribute("dto", dto);
		return page;

	}

	public String update()
			throws UnsupportedEncodingException {

		String page = "/WEB-INF/jsp/projectUpdate.jsp";

		request.setCharacterEncoding("UTF-8");

		ProjectsDTO dto = new ProjectsDTO();

		dto.setProjectId(
				Integer.parseInt(
						request.getParameter("project_id")));

		dto.setProjectCode(
				request.getParameter("project_code"));

		dto.setProjectName(
				request.getParameter("project_name"));

		ProjectService service = new ProjectService();

		int ans = service.update(dto);

		if (ans == 1) {
			page = "/ProjectServlet?action=detail"
					+ "&projectId="
					+ dto.getProjectId();
		}

		return page;
	}

	public String changeStatus() {

		String page = "/ProjectServlet?action=list";

		int projectId = Integer.parseInt(
				request.getParameter("projectId"));

		String status = request.getParameter("status");

		ProjectService service = new ProjectService();

		service.changeStatus(
				projectId,
				status);

		return page;
	}
}
