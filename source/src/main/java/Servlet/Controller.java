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

    private static final String JSP_LOGIN = "/WEB-INF/jsp/login.jsp";
    private static final String JSP_LOGOUT = "/WEB-INF/jsp/logout.jsp";
    private static final String JSP_PASSWORD_RESET = "/WEB-INF/jsp/passwordReset.jsp";
    private static final String JSP_ERROR = "/WEB-INF/jsp/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pageId = getRequestValue(request, "page_id", "pageId");
        String movePath = JSP_LOGIN;

        try {
            if (isEmpty(pageId) || "L001".equals(pageId)) {
                movePath = JSP_LOGIN;

            } else if (!isLogin(request)) {
                movePath = "redirect:Controller?page_id=L001";

            } else if ("D001".equals(pageId)) {
                movePath = new DashboardAction(request).show();

            } else if ("S001".equals(pageId)) {
                movePath = new SummaryAction(request, response).show();

            } else if ("M004".equals(pageId)) {
                movePath = JSP_PASSWORD_RESET;

            } else {
                request.setAttribute("errMsg", "指定された画面が見つかりません");
                movePath = JSP_ERROR;
            }

        } catch (Exception e) {
            request.setAttribute("errMsg", "画面表示中にエラーが発生しました");
            request.setAttribute("exception", e);
            movePath = JSP_ERROR;
        }

        move(request, response, movePath);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pageId = getRequestValue(request, "page_id", "pageId");
        String buttonId = getRequestValue(request, "button_id", "buttonId");
        String movePath = JSP_LOGIN;

        try {
            if ("none".equals(pageId) && "ログアウト".equals(buttonId)) {
                HttpSession session = request.getSession(false);

                // ログイン情報を破棄する
                if (session != null) {
                    session.invalidate();
                }

                movePath = JSP_LOGOUT;

            } else if ("L001".equals(pageId) && "ログイン".equals(buttonId)) {
                movePath = new LoginAction(request).login();

            } else if (!isLogin(request)) {
                movePath = "redirect:Controller?page_id=L001";

            } else if ("L001".equals(pageId) && "変更".equals(buttonId)) {
                movePath = new LoginAction(request).changePassword();

            } else if ("S001".equals(pageId) && "表示".equals(buttonId)) {
                movePath = new SummaryAction(request, response).search();

            } else if ("S002".equals(pageId) && "CSV出力".equals(buttonId)) {
                movePath = new SummaryAction(request, response).exportCsv();

            } else if ("M004".equals(pageId) && "リセット".equals(buttonId)) {
                movePath = new LoginAction(request).resetPassword();

            } else {
                request.setAttribute("errMsg", "指定された処理が見つかりません");
                movePath = JSP_ERROR;
            }

        } catch (Exception e) {
            request.setAttribute("errMsg", "処理中にエラーが発生しました");
            request.setAttribute("exception", e);
            movePath = JSP_ERROR;
        }

        // CSV出力時はAction内でresponseへ書き込み済み
        if (movePath == null) {
            return;
        }

        move(request, response, movePath);
    }

    private boolean isLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loginUser") != null;
    }

    private String getRequestValue(HttpServletRequest request, String snakeName, String camelName) {
        String value = request.getParameter(snakeName);

        // 既存コードがキャメルケースの場合も拾う
        if (isEmpty(value)) {
            value = request.getParameter(camelName);
        }

        return value;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void move(HttpServletRequest request, HttpServletResponse response, String movePath)
            throws ServletException, IOException {

        if (movePath.startsWith("redirect:")) {
            String redirectPath = movePath.substring("redirect:".length());
            response.sendRedirect(request.getContextPath() + "/" + redirectPath);
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(movePath);
        dispatcher.forward(request, response);
    }
}