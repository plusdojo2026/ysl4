package action;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.UserDTO;
import service.UserService;

/**
 * ログイン関連の画面処理を担当するAction
 * ログインとパスワード変更を行う
 */
public class LoginAction {

    /** ログイン画面 */
    private static final String JSP_LOGIN = "/WEB-INF/jsp/login.jsp";

    /** ホーム画面へのredirect */
    private static final String REDIRECT_HOME = "redirect:Controller?page_id=H001";

    /** request */
    private final HttpServletRequest request;

    /**
     * requestを受け取る
     *
     * @param request 画面から送られた情報
     */
    public LoginAction(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * ログイン処理を行う
     *
     * @return 遷移先
     */
    public String login() {

        String loginId = getParam("login_id", "loginId");
        String password = getParam("password");

        UserService service = new UserService();
        UserDTO loginUser = service.login(loginId, password);

        if (loginUser == null) {
            request.setAttribute("errMsg", "ログインIDまたはパスワードが正しくありません");
            request.setAttribute("loginId", loginId);
            return JSP_LOGIN;
        }

        HttpSession session = request.getSession(true);

        request.changeSessionId();

        session.setAttribute("loginUser", loginUser);

        return REDIRECT_HOME;
    }

    /**
     * パスワード変更画面を表示
     *
     * @return 遷移先JSP
     */
    public String showPasswordChange() {

        if (getLoginUser() == null) {
            return "redirect:Controller?page_id=L001&msg="
                    + encode("ログインしてください");
        }

        return "/WEB-INF/jsp/passwordChange.jsp";
    }

    /**
     * 自分のパスワードを変更
     *
     * @return 遷移先
     */
    public String changePassword() {

        UserDTO loginUser = getLoginUser();

        if (loginUser == null) {
            return "redirect:Controller?page_id=L001&msg="
                    + encode("ログインしてください");
        }

        String currentPassword = getParam(
                "current_password",
                "currentPassword");

        String newPassword = getParam(
                "new_password",
                "newPassword");

        String confirmPassword = getParam(
                "confirm_password",
                "confirmPassword");

        String errorMessage = validatePassword(
                currentPassword,
                newPassword,
                confirmPassword);

        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            return "/WEB-INF/jsp/passwordChange.jsp";
        }

        UserService service = new UserService();

        boolean success = service.changePassword(
                loginUser.getUserId(),
                currentPassword,
                newPassword,
                confirmPassword);

        if (success) {
            return REDIRECT_HOME
                    + "&msg="
                    + encode("パスワードを変更しました");
        }

        request.setAttribute(
                "errMsg",
                "現在のパスワードが正しくありません");

        return "/WEB-INF/jsp/passwordChange.jsp";
    }

    /**
     * パスワード変更の入力チェックを行う
     *
     * @param currentPassword 現在パスワード
     * @param newPassword 新パスワード
     * @param confirmPassword 確認用パスワード
     * @return エラーメッセージ
     */
    private String validatePassword(
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        if (!hasText(currentPassword)) {
            return "現在のパスワードを入力してください";
        }

        if (!hasText(newPassword)) {
            return "新しいパスワードを入力してください";
        }

        if (newPassword.length() < 6) {
            return "新しいパスワードは6文字以上で入力してください";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "確認用パスワードが一致しません";
        }

        return "";
    }

    /**
     * ログインユーザーを取得
     *
     * @return ログインユーザー
     */
    private UserDTO getLoginUser() {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        Object loginUser = session.getAttribute("loginUser");

        if (!(loginUser instanceof UserDTO)) {
            return null;
        }

        return (UserDTO) loginUser;
    }

    /**
     * requestから値を取得
     *
     * @param names name候補
     * @return 取得値
     */
    private String getParam(String... names) {

        for (String name : names) {
            String value = request.getParameter(name);

            if (value != null) {
                return value.trim();
            }
        }

        return "";
    }

    /**
     * 文字列が入力されているか確認
     *
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * URL用文字列へ変換
     *
     * @param value 変換前文字列
     * @return 変換後文字列
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}