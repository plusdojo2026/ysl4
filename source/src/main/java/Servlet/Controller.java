package Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Action.DashboardAction;
import Action.LoginAction;
import Action.SummaryAction;

@WebServlet("/Controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ログイン画面のJSPパス
    private static final String JSP_LOGIN = "/WEB-INF/jsp/login.jsp";

    // エラー画面のJSPパス
    private static final String JSP_ERROR = "/WEB-INF/jsp/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ログアウト後に戻るボタンで前画面が出ないようにする
        setNoCache(response);

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pageId = request.getParameter("page_id");
        String movePath = JSP_LOGIN;

        try {
            // 未ログインならログイン画面へ戻す
            if (!isPublicPage(pageId) && !isLoggedIn(request)) {
                redirect(request, response, "Controller?page_id=L001");
                return;
            }

            if (isBlank(pageId) || "L001".equals(pageId)) {
                movePath = JSP_LOGIN;

            } else if ("H001".equals(pageId)) {
                movePath = new DashboardAction(request).showHome();

            } else if ("S001".equals(pageId)) {
                movePath = new SummaryAction(request, response).showMonthlySummary();

            } else if ("M004".equals(pageId)) {
                movePath = "/WEB-INF/jsp/passwordReset.jsp";

            } else {
                movePath = JSP_ERROR;
            }

            move(request, response, movePath);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errMsg", "画面表示中にエラーが発生しました");
            move(request, response, JSP_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ログアウト後に戻るボタンで前画面が出ないようにする
        setNoCache(response);

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pageId = request.getParameter("page_id");
        String buttonId = request.getParameter("button_id");
        String movePath = JSP_LOGIN;

        try {
            // ログイン処理だけは未ログインで通す
            if (isLoginRequest(pageId, buttonId)) {
                movePath = new LoginAction(request).login();
                move(request, response, movePath);
                return;
            }

            // ログアウト処理はセッション切れでも通す
            if ("none".equals(pageId) && "ログアウト".equals(buttonId)) {
                movePath = new LoginAction(request).logout();
                move(request, response, movePath);
                return;
            }

            // その他のPOSTはログイン必須にする
            if (!isLoggedIn(request)) {
                redirect(request, response, "Controller?page_id=L001");
                return;
            }

            if ("L002".equals(pageId) && "変更".equals(buttonId)) {
                movePath = new LoginAction(request).changePassword();

            } else if ("M004".equals(pageId) && "リセット".equals(buttonId)) {
                movePath = new LoginAction(request).resetPassword();

            } else if ("S001".equals(pageId) && "表示".equals(buttonId)) {
                movePath = new SummaryAction(request, response).searchMonthlySummary();

            } else if ("S001".equals(pageId) && "CSV出力".equals(buttonId)) {
                movePath = new SummaryAction(request, response).downloadCsv();

            } else {
                movePath = JSP_ERROR;
            }

            move(request, response, movePath);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errMsg", "処理中にエラーが発生しました");
            move(request, response, JSP_ERROR);
        }
    }

    private static boolean isLoginRequest(String pageId, String buttonId) {

        // staticなのでpageIdとbuttonIdだけでログイン処理か判定できる
        return "L001".equals(pageId) && "ログイン".equals(buttonId);
    }

    private static boolean isPublicPage(String pageId) {

        // staticなのでpageIdだけで認証不要ページか判定できる
        return isBlank(pageId) || "L001".equals(pageId);
    }

    private static boolean isLoggedIn(HttpServletRequest request) {

        // staticなのでrequestだけでログイン状態を判定できる
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loginUser") != null;
    }

    private static boolean isBlank(String value) {

        // staticなので文字列だけで空判定ができる
        return value == null || value.trim().isEmpty();
    }

    private static void setNoCache(HttpServletResponse response) {

        // staticなのでresponseだけでキャッシュ禁止を設定できる
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    private void move(HttpServletRequest request, HttpServletResponse response, String movePath)
            throws ServletException, IOException {

        // CSV出力などresponseへ直接書いた場合は遷移しない
        if (movePath == null) {
            return;
        }

        if (movePath.startsWith("redirect:")) {
            redirect(request, response, movePath.replaceFirst("redirect:", ""));
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(movePath);
        dispatcher.forward(request, response);
    }

    private static void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {

        // staticなのでrequestとresponseだけでリダイレクトできる
        response.sendRedirect(request.getContextPath() + "/" + path);
    }
}