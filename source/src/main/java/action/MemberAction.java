package action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.UserDTO;
import service.UserService;

public class MemberAction {

    /** メンバー一覧画面 */
    private static final String JSP_MEMBER_LIST = "/WEB-INF/jsp/memberList.jsp";

    /** メンバー登録と編集画面 */
    private static final String JSP_MEMBER_FORM = "/WEB-INF/jsp/memberRegist.jsp";

    /** パスワードリセット画面 */
    private static final String JSP_PASSWORD_RESET = "/WEB-INF/jsp/passwordReset.jsp";

    /** ホーム画面へのリダイレクト先 */
    private static final String REDIRECT_HOME = "redirect:Controller?page_id=H001";

    /** メンバー一覧画面へのリダイレクト先 */
    private static final String REDIRECT_MEMBER_LIST = "redirect:Controller?page_id=M001";

    /** 画面から送られた値を扱うrequest */
    private final HttpServletRequest request;


	//コンストラクタ
	public MemberAction(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * メンバー一覧を取得
	 * GET表示や検索で使う
	 * @return
	 */
	public String list() {

		//一般ユーザーはアクセスできないバリデーション
		if (!isAdminUser()) {
			return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます。");
		}
		//Serviceを使って全ユーザーを取得する
		UserService service = new UserService();
		List<UserDTO> users = service.selectAll();

		//検索キーワードを取得する
		String keyword = request.getParameter("keyword");

		//キーワードがある場合のみ一覧を絞り込む
		List<UserDTO> displayUsers = filterUsers(users, keyword);

		//JSP表示用にrequestに入れる
		request.setAttribute("users", displayUsers);
		request.setAttribute("keyword", keyword);

		// redirectで渡されたメッセージをrequestに入れる
		setMessageFromParameter();

		return JSP_MEMBER_LIST;
	}

	//メンバー登録画面の表示---------------------------------

	//メンバーの登録メソッド---------------------------------
	public String regist() throws UnsupportedEncodingException {

		//一般ユーザーはアクセスできないバリデーション
		if (!isAdminUser()) {
			return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます。");
		}

		// 入力値から登録用DTOを作る
		UserDTO dto = createUserDtoForRegist();

		// 登録前の入力チェックを行う
		String errorMessage = validateForRegist(dto);
		
		//入力エラーがある場合は登録画面へ戻す
        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("mode", "regist");
            request.setAttribute("user", userDto);
            return JSP_MEMBER_FORM;
        }

		//必須項目すべてが入力されていたら、serviceを実体化
		UserService service = new UserService();
	}

	/**
	 * 登録用のUserDTOを作る
	 * メンバー登録画面の入力値をDTOに詰める
	 * @return 登録用ユーザーDTO
	 */
	private UserDTO createUserDtoForRegist() {

		// 空のDTOを作る
		UserDTO userDto = new UserDTO();

		// ログインIDを設定する
		userDto.setLoginId(getParam("login_id"));

		// 初期パスワードを設定する
		// Service側でBCryptハッシュ化してからDBへ保存する
		userDto.setPasswordHash(getParam("initial_password"));

		// 氏名を設定する
		userDto.setName(getParam("name"));

		// メールアドレスを設定する
		userDto.setEmail(getParam("email"));

		// 管理者フラグを設定する
		userDto.setIsAdmin(parseAdmin());

		// 登録時は有効ユーザーとして扱う
		userDto.setIsValid(true);

		return userDto;
	}

	/**
	 * 更新用のUserDTOを作る
	 * メンバー編集画面の入力値をDTOに詰める
	 * @return 更新用ユーザーDTO
	 */
	private UserDTO createUserDtoForUpdate() {

		// 空のDTOを作る
		UserDTO userDto = new UserDTO();

		// ユーザーIDを設定する
		userDto.setUserId(parseInt(getParam("user_id", "userId")));

		// 氏名を設定する
		userDto.setName(getParam("name"));

		// メールアドレスを設定する
		userDto.setEmail(getParam("email"));

		// 管理者フラグを設定する
		userDto.setIsAdmin(parseAdmin());

		// 有効フラグを設定する
		userDto.setIsValid(parseValid());

		return userDto;
	}

	/**
	 * 登録時の入力チェックを行う
	 * 未入力や形式不正を画面に戻すために使う
	 * @param userDto 入力値を入れたDTO
	 * @return エラーメッセージ
	 */
	private String validateForRegist(UserDTO userDto) {

		// ログインID未入力を確認する
		if (!hasText(userDto.getLoginId())) {
			return "ログインIDを入力してください";
		}

		// 初期パスワード未入力を確認する
		if (!hasText(userDto.getPasswordHash())) {
			return "初期パスワードを入力してください";
		}

		// 初期パスワードの文字数を確認する
		if (userDto.getPasswordHash().length() < 6) {
			return "初期パスワードは6文字以上で入力してください";
		}

		// 氏名未入力を確認する
		if (!hasText(userDto.getName())) {
			return "氏名を入力してください";
		}

		// メールアドレス未入力を確認する
		if (!hasText(userDto.getEmail())) {
			return "メールアドレスを入力してください";
		}

		// メールアドレスの簡易形式を確認する
		if (!userDto.getEmail().contains("@")) {
			return "メールアドレスの形式が正しくありません";
		}

		return "";
	}

	/**
	 * 更新時の入力チェックを行う
	 * ユーザーIDや氏名などの必須値を確認する
	 * @param userDto 入力値を入れたDTO
	 * @return エラーメッセージ
	 */
	private String validateForUpdate(UserDTO userDto) {

		// ユーザーIDの不正を確認する
		if (userDto.getUserId() <= 0) {
			return "ユーザーIDが不正です";
		}

		// 氏名未入力を確認する
		if (!hasText(userDto.getName())) {
			return "氏名を入力してください";
		}

		// メールアドレス未入力を確認する
		if (!hasText(userDto.getEmail())) {
			return "メールアドレスを入力してください";
		}

		// メールアドレスの簡易形式を確認する
		if (!userDto.getEmail().contains("@")) {
			return "メールアドレスの形式が正しくありません";
		}

		return "";
	}

	/**
	 * パスワードの入力チェックを行う
	 * リセット画面で使う
	 * @param newPassword 新しいパスワード
	 * @param confirmPassword 確認用パスワード
	 * @return エラーメッセージ
	 */
	private String validatePassword(String newPassword, String confirmPassword) {

		// 新しいパスワード未入力を確認する
		if (!hasText(newPassword)) {
			return "新しいパスワードを入力してください";
		}

		// 新しいパスワードの文字数を確認する
		if (newPassword.length() < 6) {
			return "新しいパスワードは6文字以上で入力してください";
		}

		// 確認用パスワードとの一致を確認する
		if (!newPassword.equals(confirmPassword)) {
			return "確認用パスワードが一致しません";
		}

		return "";
	}

	/**
	 * 管理者としてログインしているか確認する
	 * メンバー管理は管理者だけ使える
	 * @return 管理者ならtrue
	 */
	private boolean isAdminUser() {

		// 既存セッションだけを取得する
		HttpSession session = request.getSession(false);

		// セッションがない場合は未ログイン扱い
		if (session == null) {
			return false;
		}

		// セッションからログインユーザーを取得する
		Object loginUser = session.getAttribute("loginUser");

		// UserDTOでなければ不正なセッションとして扱う
		if (!(loginUser instanceof UserDTO)) {
			return false;
		}

		// 管理者フラグを返す
		return ((UserDTO) loginUser).getIsAdmin();
	}

	/**
	 * 検索キーワードで一覧を絞り込む
	 * searchをpublicに増やさないため補助処理にする
	 * @param users ユーザー一覧
	 * @param keyword 検索キーワード
	 * @return 絞り込み後の一覧
	 */
	private List<UserDTO> filterUsers(List<UserDTO> users, String keyword) {

		// キーワードがない場合はそのまま返す
		if (!hasText(keyword)) {
			return users;
		}

		// 絞り込み後の一覧を作る
		List<UserDTO> filteredUsers = new ArrayList<>();

		// ユーザー一覧を1件ずつ確認する
		for (UserDTO user : users) {

			// ログインID、氏名、メールアドレスのいずれかに含まれていれば追加する
			if (contains(user.getLoginId(), keyword)
					|| contains(user.getName(), keyword)
					|| contains(user.getEmail(), keyword)) {

				filteredUsers.add(user);
			}
		}

		return filteredUsers;
	}

	/**
	 * 文字列がキーワードを含むか確認する
	 * nullでもエラーにしない
	 * @param value 検索対象
	 * @param keyword 検索キーワード
	 * @return 含む場合true
	 */
	private boolean contains(String value, String keyword) {

		// 検索対象かキーワードがない場合は含まない
		if (value == null || keyword == null) {
			return false;
		}

		// 大文字小文字を区別せずに比較する
		return value.toLowerCase().contains(keyword.toLowerCase());
	}

	/**
	 * 管理者フラグを画面入力から判定する
	 * selectとcheckboxの両方に対応する
	 * @return 管理者ならtrue
	 */
	private boolean parseAdmin() {

		// 管理者入力値を取得する
		String value = getParam("is_admin", "isAdmin", "role");

		// 日本語の管理者表記を判定する
		if ("管理者".equals(value)) {
			return true;
		}

		// admin表記を判定する
		if ("admin".equalsIgnoreCase(value)) {
			return true;
		}

		// true表記を判定する
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}

		// 1表記を判定する
		if ("1".equals(value)) {
			return true;
		}

		// checkboxのon表記を判定する
		if ("on".equalsIgnoreCase(value)) {
			return true;
		}

		return false;
	}

	/**
	 * 有効フラグを画面入力から判定する
	 * 未指定の場合は有効として扱う
	 * @return 有効ならtrue
	 */
	private boolean parseValid() {

		// 有効状態の入力値を取得する
		String value = getParam("is_valid", "isValid", "status");

		// 未指定の場合は有効として扱う
		if (!hasText(value)) {
			return true;
		}

		// 日本語の無効表記を判定する
		if ("無効".equals(value)) {
			return false;
		}

		// inactive表記を判定する
		if ("inactive".equalsIgnoreCase(value)) {
			return false;
		}

		// false表記を判定する
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}

		// 0表記を判定する
		if ("0".equals(value)) {
			return false;
		}

		return true;
	}

	/**
	 * 有効化か無効化かを判定する
	 * button_idを優先して判定する
	 * @return 有効にする場合true
	 */
	private boolean decideValidity() {

		// 押されたボタン名を取得する
		String buttonId = getParam("button_id", "buttonId");

		// 有効化ボタンの場合はtrue
		if ("有効化".equals(buttonId)) {
			return true;
		}

		// 無効化ボタンの場合はfalse
		if ("無効化".equals(buttonId)) {
			return false;
		}

		// ボタン名で判断できない場合は入力値から判定する
		return parseValid();
	}

	/**
	 * requestから値を取得する
	 * 複数候補を順番に確認する
	 * @param names 取得候補のname
	 * @return 取得値
	 */
	private String getParam(String... names) {

		// 候補のnameを順番に確認する
		for (String name : names) {

			// requestから値を取得する
			String value = request.getParameter(name);

			// 値が見つかったら前後の空白を除いて返す
			if (value != null) {
				return value.trim();
			}
		}

		return "";
	}

	/**
	 * 文字列をintに変換する
	 * 数値以外の場合は-1を返す
	 * @param value 変換前の文字列
	 * @return 変換後の数値
	 */
	private int parseInt(String value) {

		// 未入力の場合は不正値にする
		if (!hasText(value)) {
			return -1;
		}

		try {
			// 数値に変換して返す
			return Integer.parseInt(value);

		} catch (NumberFormatException e) {
			// 数値以外の場合は不正値にする
			return -1;
		}
	}

	/**
	 * 文字列が入力されているか確認する
	 * nullと空文字を未入力として扱う
	 * @param value 確認する文字列
	 * @return 入力ありならtrue
	 */
	private boolean hasText(String value) {
		return value != null && !value.trim().isEmpty();
	}

	/**
	 * URLに入れる文字列をエンコードする
	 * redirect時の日本語文字化けを防ぐ
	 * @param value エンコード前の文字列
	 * @return エンコード後の文字列
	 */
	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	/**
	 * redirect後のメッセージをrequestへ入れる
	 * JSPまたは共通JSで表示する
	 */
	private void setMessageFromParameter() {

		// URLパラメータからメッセージを取得する
		String message = getParam("msg");

		// メッセージがある場合だけrequestへ渡す
		if (hasText(message)) {
			request.setAttribute("successMsg", message);
		}
	}

}
