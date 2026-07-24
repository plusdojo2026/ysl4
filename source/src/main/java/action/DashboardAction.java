package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.DashboardDTO;
import model.UserDTO;
import service.DashboardService;

public class DashboardAction {

	// Controllerから受け取ったrequestを保持する
	private final HttpServletRequest request;

	public DashboardAction(HttpServletRequest request) {
		this.request = request;
	}

	public String show() {

	    HttpSession session = request.getSession(false);

	    if (session == null) {
	        return "redirect:Controller?page_id=L001";
	    }

	    System.out.println("① session取得");
	    UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

	    if (loginUser == null) {
	        return "redirect:Controller?page_id=L001";
	    }

	    System.out.println("② loginUser取得");
	    DashboardService service = new DashboardService();
	    
	    System.out.println("③ service作成");
	    DashboardDTO dashboard = service.getDashboard(loginUser.getUserId());
	    
	    System.out.println("④ dashboard取得");

	    request.setAttribute("dashboard", dashboard);

	    return "/WEB-INF/jsp/home.jsp";
	}
}