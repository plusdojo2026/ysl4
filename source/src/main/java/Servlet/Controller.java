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

// 
@WebServlet("/Controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 全リクエストで同じJSPパスを使う
    private static final String JSP_LOGIN = "/WEB-INF/jsp/login.jsp";

    // 全リクエストで同じエラー画面を使う
    private static final String JSP_ERROR = "/WEB-INF/jsp/error.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 戻るボタンでログイン後画面が見えないようにする
        setNoCache(response);

        String pageId = request.getParameter("page_id");
        String movePath = "";

        try {
            if (isBlank(pageId) || "L001".equals(pageId)) {
                movePath = JSP_LOGIN;

            } else if (!isLogin(request)) {
                movePath = "redirect:Controller?page_id=L001";

            } else if ("H001".equals(pageId)) {
                movePath = new DashboardAction(request).show();

            } else if ("S001".equals(pageId)) {
                movePath = new SummaryAction(request, response).show();

            } else if ("L002".equals(pageId)) {
                movePath = new LoginAction(request).showPasswordChange();

            } else if ("M004".equals(pageId)) {
                movePath = "/WEB-INF/jsp/passwordReset.jsp";

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // POST時もキャッシュ禁止
        setNoCache(response);

        // 文字コード設定
        request.setCharacterEncoding("UTF-8");

        String pageId = request.getParameter("page_id");
        String buttonId = request.getParameter("button_id");
        String movePath = "";

        try {
            if ("none".equals(pageId) && "ログアウト".equals(buttonId)) {
                movePath = logout(request);

            } else if ("L001".equals(pageId) && "ログイン".equals(buttonId)) {
                movePath = new LoginAction(request).login();

            } else if (!isLogin(request)) {
                movePath = "redirect:Controller?page_id=L001";

            } else if ("L002".equals(pageId) && "変更".equals(buttonId)) {
                movePath = new LoginAction(request).changePassword();

            } else if ("S001".equals(pageId) && "検索".equals(buttonId)) {
                movePath = new SummaryAction(request, response).search();

            } else if ("S001".equals(pageId) && "CSV出力".equals(buttonId)) {
                movePath = new SummaryAction(request, response).exportCsv();

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

    private String logout(HttpServletRequest request) {

        // Controller内部だけで使うログアウト処理
        // LoginActionのpublicメソッドをクラス図準拠に保つ
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "/WEB-INF/jsp/logout.jsp";
    }

    private void move(HttpServletRequest request, HttpServletResponse response, String movePath)
            throws ServletException, IOException {

        // Controller内部だけで使う画面遷移処理
        // nullならCSV出力済みとして何もしない
        if (movePath == null) {
            return;
        }

        if (movePath.startsWith("redirect:")) {
            response.sendRedirect(request.getContextPath() + "/" + movePath.substring("redirect:".length()));
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(movePath);
        dispatcher.forward(request, response);
    }

    private boolean isLogin(HttpServletRequest request) {

        // Controller内部だけで使う認証確認処理
        // AuthFilter未作成の代替として使う
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("loginUser") != null;
    }

    private static boolean isBlank(String value) {

        // Controller内部だけで使う空文字判定
        // page_idがない場合の判定に使う
        return value == null || value.trim().isEmpty();
    }

    private static void setNoCache(HttpServletResponse response) {

        // Controller内部だけで使うキャッシュ禁止処理
        // ログアウト後に戻るボタンで保護画面が出ることを防ぐ
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}