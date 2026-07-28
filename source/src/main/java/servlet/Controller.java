package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import action.DashboardAction;
import action.LoginAction;
import action.MemberAction;
import action.ProjectAction;
import action.SummaryAction;
import action.TaskAction;
import action.WorkLogAction;

/**
 * 全画面の入口になるController
 * page_idとbutton_idを見てActionへ処理を振り分ける
 */
@WebServlet("/Controller")
public class Controller extends HttpServlet {

    /** シリアルバージョンID */
    private static final long serialVersionUID = 1L;

    /** ログイン画面 */
    private static final String JSP_LOGIN = "/WEB-INF/jsp/login.jsp";

    /** ログイン画面へのredirect */
    private static final String REDIRECT_LOGIN = "redirect:Controller?page_id=L001";

    /** ホーム画面へのredirect */
    private static final String REDIRECT_HOME = "redirect:Controller?page_id=H001";

    /** sessionに保存するアラートメッセージ名 */
    private static final String SESSION_ALERT_MESSAGE = "alertMessage";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        // キャッシュを禁止
        setNoCache(response);

        // 文字コードを設定する
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String movePath = "";

        try {
            // GET処理を振り分ける
            movePath = handleGet(request, response);

            // 画面遷移する
            move(request, response, movePath);

        } catch (Exception e) {
            // 例外内容をコンソールへ出す
            e.printStackTrace();

            // エラーアラートを出して安全な画面へ戻す
            movePath = createAlertRedirect(request, "処理中にエラーが発生しました");
            move(request, response, movePath);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        // キャッシュを禁止する
        setNoCache(response);

        // 文字コードを設定する
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String movePath = "";

        try {
            // POST処理を振り分ける
            movePath = handlePost(request, response);

            // 画面遷移する
            move(request, response, movePath);

        } catch (Exception e) {
            // 例外内容をコンソールへ出す
            e.printStackTrace();

            // エラーアラートを出して安全な画面へ戻す
            movePath = createAlertRedirect(request, "処理中にエラーが発生しました");
            move(request, response, movePath);
        }
    }

    /**
     * GET処理を振り分ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 遷移先
     * @throws Exception 例外
     */
    private String handleGet(HttpServletRequest request, HttpServletResponse response)throws Exception {

        String pageId = getParam(request, "page_id", "pageId");
        
        System.out.print(pageId);

        // page_idなし、またはログイン画面
        if (isBlank(pageId) || "L001".equals(pageId)) {
            return JSP_LOGIN;
        }

        // 未ログインならログイン画面へ戻す
        if (!isLogin(request)) {
            return createAlertRedirect(request, "ログインしてください");
        }

        // ホーム
        if ("H001".equals(pageId)) {
            return new DashboardAction(request).show();
        }

        // パスワード変更画面
        if ("L002".equals(pageId)) {
            return new LoginAction(request).showPasswordChange();
        }

        // 案件一覧
        if ("P001".equals(pageId)) {
            return new ProjectAction(request).list();
        }

        // 案件詳細
        if ("P002".equals(pageId)) {
            return new ProjectAction(request).detail();
        }

        // 案件登録画面
        if ("P003".equals(pageId)) {
            return new ProjectAction(request).showRegist();
        }

        // 案件編集画面
        if ("P004".equals(pageId)) {
            return new ProjectAction(request).showUpdate();
        }

        // タスク一覧
        if ("T001".equals(pageId)) {
            return new TaskAction(request).list();
        }

        // タスク詳細
        if ("T002".equals(pageId)) {
            return new TaskAction(request).detail();
        }

        // タスク登録画面
        if ("T003".equals(pageId)) {
            return new TaskAction(request).showRegist();
        }

        // タスク編集画面
        if ("T004".equals(pageId)) {
            return new TaskAction(request).showUpdate();
        }

        // 工数登録モーダル
        if ("W001".equals(pageId)) {
            return new WorkLogAction(request).showRegist();
        }

        // 月次集計
        if ("S001".equals(pageId)) {
            return new SummaryAction(request, response).show();
        }

        // メンバー一覧
        if ("M001".equals(pageId)) {
            return new MemberAction(request).list();
        }

        // メンバー登録画面
        if ("M002".equals(pageId)) {
            return new MemberAction(request).showRegist();
        }

        // メンバー編集画面
        if ("M003".equals(pageId)) {
            return new MemberAction(request).showUpdate();
        }

        // パスワードリセット画面
        if ("M004".equals(pageId)) {
            return new MemberAction(request).showPasswordReset();
        }

        // 該当なしの場合はエラーアラートを出す
        return createAlertRedirect(request, "対象の画面が見つかりません");
    }

    /**
     * POST処理を振り分ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 遷移先
     * @throws Exception 例外
     */
    private String handlePost(HttpServletRequest request, HttpServletResponse response)throws Exception {

        String pageId = getParam(request, "page_id", "pageId");
        String buttonId = getParam(request, "button_id", "buttonId");
        
        //デバッグ
        System.out.println("pageId=" + pageId);
        System.out.println("buttonId=" + buttonId);
        System.out.println("keyword=" + request.getParameter("keyword"));

        // ログアウト
        if ("none".equals(pageId) && "ログアウト".equals(buttonId)) {
            return logout(request);
        }

        // ログイン
        if ("L001".equals(pageId) && "ログイン".equals(buttonId)) {
            return new LoginAction(request).login();
        }

        // 未ログインならログイン画面へ戻す
        if (!isLogin(request)) {
            return createAlertRedirect(request, "ログインしてください");
        }

        // パスワード変更
        if ("L002".equals(pageId) && "変更".equals(buttonId)) {
            return new LoginAction(request).changePassword();
        }

        // 案件検索
        if ("P001".equals(pageId) && "検索".equals(buttonId)) {
            return new ProjectAction(request).search();
        }

        // 案件登録
        if ("P003".equals(pageId) && "登録".equals(buttonId)) {
            return new ProjectAction(request).regist();
        }

        // 案件更新
        if ("P004".equals(pageId) && "更新".equals(buttonId)) {
            return new ProjectAction(request).update();
        }

        // 案件ステータス変更
        if ("P002".equals(pageId) && isStatusChangeButton(buttonId)) {
            return new ProjectAction(request).changeStatus();
        }

        // タスク検索
        if ("T001".equals(pageId) && "検索".equals(buttonId)) {
            return new TaskAction(request).search();
        }

        // タスク登録
        if ("T003".equals(pageId) && "登録".equals(buttonId)) {
            return new TaskAction(request).regist();
        }

        // タスク更新
        if ("T004".equals(pageId) && "更新".equals(buttonId)) {
            return new TaskAction(request).update();
        }

        // タスクステータス変更
        if ("T002".equals(pageId) && isStatusChangeButton(buttonId)) {
            return new TaskAction(request).changeStatus();
        }

        // タスク削除
        if ("T002".equals(pageId) && "削除".equals(buttonId)) {
            return new TaskAction(request).delete();
        }

        // 工数登録
        if ("W001".equals(pageId) && "登録".equals(buttonId)) {
            return new WorkLogAction(request).regist();
        }

        // 工数削除
        if ("W001".equals(pageId) && "削除".equals(buttonId)) {
            return new WorkLogAction(request).delete();
        }

        // 月次集計検索
        if ("S001".equals(pageId) && ("検索".equals(buttonId) || "表示".equals(buttonId))) {
            return new SummaryAction(request, response).search();
        }

        // CSV出力
        if (("S001".equals(pageId) || "S002".equals(pageId)) && "CSV出力".equals(buttonId)) {
            return new SummaryAction(request, response).exportCsv();
        }

        // メンバー検索
        if ("M001".equals(pageId) && "検索".equals(buttonId)) {
            return new MemberAction(request).list();
        }

        // メンバー登録
        if ("M002".equals(pageId) && "登録".equals(buttonId)) {
            return new MemberAction(request).regist();
        }

        // メンバー更新
        if ("M003".equals(pageId) && "更新".equals(buttonId)) {
            return new MemberAction(request).update();
        }

        // メンバー有効化
        if ("M003".equals(pageId) && "有効化".equals(buttonId)) {
            return new MemberAction(request).changeValidity();
        }

        // メンバー無効化
        if ("M003".equals(pageId) && "無効化".equals(buttonId)) {
            return new MemberAction(request).changeValidity();
        }

        // パスワードリセット
        if ("M004".equals(pageId) && "リセット".equals(buttonId)) {
            return new MemberAction(request).resetPassword();
        }

        // 該当なしの場合はエラーアラートを出す
        return createAlertRedirect(request, "対象の処理が見つかりません");
    }

    /**
     * ログアウト処理を行う
     * @param request リクエスト
     * @return 遷移先
     */
    private String logout(HttpServletRequest request) {

        // 既存セッションを取得する
        HttpSession session = request.getSession(false);

        // セッションがある場合は破棄する
        if (session != null) {
            session.invalidate();
        }

        // 新しいセッションにログアウトメッセージを入れる
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute(SESSION_ALERT_MESSAGE, "ログアウトしました");

        return REDIRECT_LOGIN;
    }

    /**
     * 画面遷移を行う
     * @param request リクエスト
     * @param response レスポンス
     * @param movePath 遷移先
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    private void move(HttpServletRequest request, HttpServletResponse response, String movePath)throws ServletException, IOException {

        // CSV出力済みの場合は何もしない
        if (movePath == null) {
            return;
        }

        // redirect指定の場合
        if (movePath.startsWith("redirect:")) {
            String redirectPath = movePath.substring("redirect:".length());
            response.sendRedirect(request.getContextPath() + "/" + redirectPath);
            return;
        }

        // JSPへforwardする
        RequestDispatcher dispatcher = request.getRequestDispatcher(movePath);
        dispatcher.forward(request, response);
    }

    /**
     * エラーアラート用redirectを作る
     * @param request リクエスト
     * @param message 表示メッセージ
     * @return 遷移先
     */
    private String createAlertRedirect(HttpServletRequest request, String message) {

        // セッションにアラートメッセージを保存する
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_ALERT_MESSAGE, message);

        // ログイン済みならホームへ戻す
        if (isLogin(request)) {
            return REDIRECT_HOME;
        }

        // 未ログインならログイン画面へ戻す
        return REDIRECT_LOGIN;
    }

    /**
     * ログイン済みか確認する
     * @param request リクエスト
     * @return ログイン済みならtrue
     */
    private boolean isLogin(HttpServletRequest request) {

        // 既存セッションだけ取得する
        HttpSession session = request.getSession(false);

        // セッションがない場合は未ログイン
        if (session == null) {
            return false;
        }

        // ログインユーザーがあればログイン済み
        return session.getAttribute("loginUser") != null;
    }

    /**
     * ステータス変更ボタンか確認する
     * @param buttonId ボタンID
     * @return ステータス変更ならtrue
     */
    private boolean isStatusChangeButton(String buttonId) {

        if ("ステータス変更".equals(buttonId)) {
            return true;
        }

        if ("状態変更".equals(buttonId)) {
            return true;
        }

        if ("変更".equals(buttonId)) {
            return true;
        }

        return false;
    }

    /**
     * requestから値を取得する
     * @param request リクエスト
     * @param names name候補
     * @return 取得値
     */
    private String getParam(HttpServletRequest request, String... names) {

        for (String name : names) {
            String value = request.getParameter(name);

            if (value != null) {
                return value.trim();
            }
        }

        return "";
    }

    /**
     * 文字列が未入力か確認する
     * @param value 確認する文字列
     * @return 未入力ならtrue
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * キャッシュを禁止する
     * @param response レスポンス
     */
    private void setNoCache(HttpServletResponse response) {

        // 戻るボタンで保護画面が表示されることを防ぐ
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }
}