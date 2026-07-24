package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.DashboardDTO;
import model.UserDTO;
import service.DashboardService;

/**
 * ダッシュボード画面の処理を担当するAction
 * Controllerから呼ばれてServiceへ表示情報の取得を依頼する
 */
public class DashboardAction {

    /** ダッシュボード画面 */
    private static final String JSP_DASHBOARD = "/WEB-INF/jsp/dashboard.jsp";

    /** ログイン画面 */
    private static final String JSP_LOGIN = "/WEB-INF/jsp/login.jsp";

    /** 画面から送られた値を扱うrequest */
    private final HttpServletRequest request;

    /**
     * requestを受け取る
     * @param request 画面から送られた情報
     */
    public DashboardAction(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * ダッシュボード画面を表示する
     * ログインユーザーIDを使って表示情報を取得する
     * @return 遷移先JSP
     */
    public String show() {

        // セッションからログインユーザーを取得する
        UserDTO loginUser = getLoginUser();

        // 未ログインの場合はログイン画面へ戻す
        if (loginUser == null) {
            request.setAttribute("errMsg", "ログインしてください");
            return JSP_LOGIN;
        }

        // ログインユーザーIDを取得する
        int userId = loginUser.getUserId();

        // ダッシュボード情報を取得する
        DashboardService service = new DashboardService();
        DashboardDTO dashboardDto = service.getDashboard(userId);

        // JSPで使うメインDTOを設定する
        request.setAttribute("dashboard", dashboardDto);

        // JSPで直接参照しやすいように件数も設定する
        request.setAttribute("inProgressProjectCount", dashboardDto.getInProgressProjectCount());
        request.setAttribute("assignedTaskCount", dashboardDto.getAssignedTaskCount());
        request.setAttribute("overdueTaskCount", dashboardDto.getOverdueTaskCount());

        // JSPで直接参照しやすいように一覧も設定する
        request.setAttribute("inProgressProjectList", dashboardDto.getInProgressProjectList());
        request.setAttribute("assignedTaskList", dashboardDto.getAssignedTaskList());

        return JSP_DASHBOARD;
    }

    /**
     * セッションからログインユーザーを取得する
     * セッションがない場合や型が違う場合はnullを返す
     * @return ログインユーザー
     */
    private UserDTO getLoginUser() {

        // 既存セッションだけ取得する
        HttpSession session = request.getSession(false);

        // セッションがない場合は未ログインとして扱う
        if (session == null) {
            return null;
        }

        // セッションからログインユーザーを取得する
        Object loginUser = session.getAttribute("loginUser");

        // UserDTO以外の場合は不正なセッションとして扱う
        if (!(loginUser instanceof UserDTO)) {
            return null;
        }

        return (UserDTO) loginUser;
    }
}