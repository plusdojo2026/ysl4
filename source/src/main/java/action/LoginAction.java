package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.UserDTO;
import service.UserService;

/**

◦ ログインとパスワード変更を担当するAction.
 */
public class LoginAction {

	/** request */
	private final HttpServletRequest request;

	/**

* requestを受け取る.
* @param request リクエスト.
	 */
	public LoginAction(HttpServletRequest request) {
		this.request = request;
	}

	/**

* ログイン処理を行う.
* @return 遷移先.
	 */
	public String login() {

		// ログイン画面の入力値を取得する
		String loginId = request.getParameter("login_id");
		String password = request.getParameter("password");

		// ログイン認証を行う
		UserService service = new UserService();
		UserDTO loginUser = service.login(loginId, password);

		// ログイン失敗時はログイン画面へ戻す
		if (loginUser == null) {
			request.setAttribute("errMsg", "ログインIDまたはパスワードが正しくありません");
			request.setAttribute("loginId", loginId);
			return "/WEB-INF/jsp/login.jsp";
		}

		// セッションを取得する
		HttpSession session = request.getSession(true);

		// セッション固定化対策でIDを変更する
		request.changeSessionId();

		// ログインユーザーを保存する
		session.setAttribute("loginUser", loginUser);

		return "redirect:Controller?page_id=H001";
	}

	/**

* パスワード変更モーダルを開く.
* @return 遷移先.
	 */
	public String showPasswordChange() {

		// セッションを取得する
		HttpSession session = request.getSession(false);

		// 次画面でモーダルを開く指定を保存する
		if (session != null) {
			session.setAttribute("passwordModalOpen", true);
		}

		return "redirect:Controller?page_id=H001";
	}

	/**

■ パスワードを変更する.
■ @return 遷移先.
	 */
	public String changePassword() {

		// セッションを取得する
		HttpSession session = request.getSession(false);

		// 未ログインならログイン画面へ戻す
		if (session == null || session.getAttribute("loginUser") == null) {
			return "redirect:Controller?page_id=L001";
		}

		// ログインユーザーを取得する
		UserDTO loginUser = (UserDTO) session.getAttribute("loginUser");

		// 入力値を取得する
		String currentPassword = request.getParameter("current_password");
		String newPassword = request.getParameter("new_password");
		String confirmPassword = request.getParameter("confirm_password");

		// 入力チェックを行う
		String errorMessage = validatePasswordInput(
				currentPassword,
				newPassword,
				confirmPassword);

		// 入力エラー時はモーダルを開いた状態で戻す
		if (hasText(errorMessage)) {
			setPasswordModalMessage(session, "error", errorMessage);
			return "redirect:Controller?page_id=H001";
		}

		// パスワードを変更する
		UserService service = new UserService();
		boolean success = service.changePassword(
				loginUser.getUserId(),
				currentPassword,
				newPassword,
				confirmPassword);

		// 変更成功時
		if (success) {
			setPasswordModalMessage(session, "success", "パスワードを変更しました");
			return "redirect:Controller?page_id=H001";
		}

		// 変更失敗時
		setPasswordModalMessage(session, "error", "現在のパスワードが正しくありません");
		return "redirect:Controller?page_id=H001";
	}

	/**

* パスワード入力値を確認する.
* @param currentPassword 現在パスワード.
* @param newPassword 新しいパスワード.
* @param confirmPassword 確認用パスワード.
* @return エラーメッセージ.
	 */
	private String validatePasswordInput(
			String currentPassword,
			String newPassword,
			String confirmPassword) {

		// 現在パスワードを確認する
		if (!hasText(currentPassword)) {
			return "現在のパスワードを入力してください";
		}

		// 新しいパスワードを確認する
		if (!hasText(newPassword)) {
			return "新しいパスワードを入力してください";
		}

		// 確認用パスワードを確認する
		if (!hasText(confirmPassword)) {
			return "新しいパスワード確認を入力してください";
		}

		// 文字数を確認する
		if (newPassword.length() < 6) {
			return "新しいパスワードは6文字以上で入力してください";
		}

		// 確認用との一致を確認する
		if (!newPassword.equals(confirmPassword)) {
			return "新しいパスワードと確認用パスワードが一致しません";
		}

		// 現在パスワードとの重複を確認する
		if (currentPassword.equals(newPassword)) {
			return "現在のパスワードとは別の値を入力してください";
		}

		return "";
	}

	/**

* パスワード変更モーダル用メッセージを設定する.
* @param session セッション.
* @param type メッセージ種別.
* @param message メッセージ.
	 */
	private void setPasswordModalMessage(
			HttpSession session,
			String type,
			String message) {

		// 次画面でモーダルを開く
		session.setAttribute("passwordModalOpen", true);

		// メッセージ種別を設定する
		session.setAttribute("passwordMessageType", type);

		// メッセージ本文を設定する
		session.setAttribute("passwordMessage", message);
	}

	/**

* 文字列があるか確認する.
* @param value 確認値.
* @return 文字列があればtrue.
	 */
	private boolean hasText(String value) {
		return value != null && !value.trim().isEmpty();
	}
}