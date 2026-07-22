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

        // クラス図準拠のpublicメソッド
        // ホーム兼ダッシュボード画面を表示する
        HttpSession session = request.getSession(false);
        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

        DashboardService service = new DashboardService();
        DashboardDTO dashboard = service.getDashboard(loginUser.getUserId());

        // JSPで使うDTOをrequestへ渡す
        request.setAttribute("dashboard", dashboard);

        return "/WEB-INF/jsp/home.jsp";
    }
}