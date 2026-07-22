package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import model.UserDTO;
import service.UserService;

public class LoginAction {

    // Controllerから受け取ったrequestを保持する
    private final HttpServletRequest request;

    public LoginAction(HttpServletRequest request) {
        this.request = request;
    }

    public String login() {

        // クラス図準拠のpublicメソッド
        // ログイン画面の入力値で認証する
        String loginId = request.getParameter("login_id");
        String password = request.getParameter("password");

        UserService service = new UserService();
        UserDTO loginUser = service.login(loginId, password);

        // ログイン失敗時はログイン画面へ戻す
        if (loginUser == null) {
            request.setAttribute("errMsg", "ログインIDまたはパスワードが正しくありません");
            request.setAttribute("loginId", loginId);
            return "/WEB-INF/jsp/login.jsp";
        }

        HttpSession session = request.getSession(true);

        // セッション固定化対策→ログイン成功時にIDを変更する
        request.changeSessionId();

        // ログインユーザー情報をセッションに保存する
        session.setAttribute("loginUser", loginUser);

        return "redirect:Controller?page_id=H001";
    }

    public String showPasswordChange() {

        // パスワード変更モーダルを開いた状態でホームへ戻す
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute("passwordModalOpen", true);
        }

        return "redirect:Controller?page_id=H001";
    }

    public String changePassword() {

        // クラス図準拠のpublicメソッド
        // 現在パスワード確認後に新パスワードを保存する
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            return "redirect:Controller?page_id=L001";
        }

        UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");
        String currentPassword = request.getParameter("current_password");
        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        boolean success = changePasswordInternal(loginUser.getUserId(), currentPassword, newPassword, confirmPassword);

        // リダイレクト後にモーダルを開いてメッセージを表示する
        session.setAttribute("passwordModalOpen", true);
        session.setAttribute("passwordMessageType", success ? "success" : "error");
        session.setAttribute("passwordMessage", success ? "パスワードを変更しました" : "パスワードを変更できませんでした");

        return "redirect:Controller?page_id=H001";
    }

    private boolean changePasswordInternal(int userId, String currentPassword, String newPassword, String confirmPassword) {

        // LoginAction内部だけで使う変更処理
        // publicメソッドをクラス図準拠に保つため内部処理へ分ける
        if (userId <= 0 || isBlank(currentPassword) || isBlank(newPassword) || isBlank(confirmPassword)) {
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        UserService service = new UserService();
        UserDTO dbUser = service.findById(userId);

        if (dbUser == null || isBlank(dbUser.getPasswordHash())) {
            return false;
        }

        if (!BCrypt.checkpw(currentPassword, dbUser.getPasswordHash())) {
            return false;
        }

        return service.resetPassword(userId, newPassword) == 1;
    }

    private static boolean isBlank(String value) {

        // LoginAction内部だけで使う空文字判定
        // nullと空文字を同じ扱いにする
        return value == null || value.trim().isEmpty();
    }
}