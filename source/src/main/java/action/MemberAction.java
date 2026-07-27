package action;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.UserDTO;
import service.UserService;

/**
 * メンバー管理の画面処理を担当するAction
 * 管理者のみメンバー一覧、登録、編集、停止、復帰、パスワードリセットを行う
 */
public class MemberAction {

    /** メンバー一覧画面 */
    private static final String JSP_MEMBER_LIST = "/WEB-INF/jsp/memberList.jsp";

    /** メンバー登録編集画面 */
    private static final String JSP_MEMBER_FORM = "/WEB-INF/jsp/memberRegist.jsp";

    /** パスワードリセット画面 */
    private static final String JSP_PASSWORD_RESET = "/WEB-INF/jsp/passwordReset.jsp";

    /** ホーム画面へのredirect */
    private static final String REDIRECT_HOME = "redirect:Controller?page_id=H001";

    /** メンバー一覧へのredirect */
    private static final String REDIRECT_MEMBER_LIST = "redirect:Controller?page_id=M001";

    /** request */
    private final HttpServletRequest request;

    /**
     * requestを受け取る
     * @param request 画面から送られた情報
     */
    public MemberAction(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * メンバー一覧を表示する
     * @return 遷移先JSP
     */
    public String list() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        UserService service = new UserService();
        List<UserDTO> userList = service.selectAll();

        String keyword = getParam("keyword");
        List<UserDTO> displayUserList = filterUsers(userList, keyword);

        request.setAttribute("users", displayUserList);
        request.setAttribute("userList", displayUserList);
        request.setAttribute("keyword", keyword);

        setMessageFromParameter();

        return JSP_MEMBER_LIST;
    }

    /**
     * メンバー登録画面を表示する
     * @return 遷移先JSP
     */
    public String showRegist() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        request.setAttribute("mode", "regist");
        request.setAttribute("user", new UserDTO());

        return JSP_MEMBER_FORM;
    }

    /**
     * メンバーを登録する
     * @return 遷移先
     */
    public String regist() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        UserDTO userDto = createUserDtoForRegist();
        String errorMessage = validateForRegist(userDto);

        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("mode", "regist");
            request.setAttribute("user", userDto);
            return JSP_MEMBER_FORM;
        }

        UserService service = new UserService();

        if (service.existsLoginId(userDto.getLoginId())) {
            request.setAttribute("errMsg", "同じログインIDが既に登録されています");
            request.setAttribute("mode", "regist");
            request.setAttribute("user", userDto);
            return JSP_MEMBER_FORM;
        }

        int result = service.regist(userDto);

        if (result > 0) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("メンバーを登録しました");
        }

        request.setAttribute("errMsg", "メンバー登録に失敗しました");
        request.setAttribute("mode", "regist");
        request.setAttribute("user", userDto);

        return JSP_MEMBER_FORM;
    }

    /**
     * メンバー編集画面を表示する
     * @return 遷移先JSP
     */
    public String showUpdate() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        int userId = parseInt(getParam("user_id", "userId"));

        if (userId <= 0) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("ユーザーIDが不正です");
        }

        UserService service = new UserService();
        UserDTO userDto = service.findById(userId);

        if (userDto == null) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("対象メンバーが見つかりません");
        }

        request.setAttribute("mode", "update");
        request.setAttribute("user", userDto);

        return JSP_MEMBER_FORM;
    }

    /**
     * メンバー情報を更新する
     * @return 遷移先
     */
    public String update() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        UserDTO userDto = createUserDtoForUpdate();
        String errorMessage = validateForUpdate(userDto);

        if (hasText(errorMessage)) {
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("mode", "update");
            request.setAttribute("user", userDto);
            return JSP_MEMBER_FORM;
        }

        UserService service = new UserService();
        int result = service.update(userDto);

        if (result > 0) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("メンバー情報を更新しました");
        }

        request.setAttribute("errMsg", "メンバー更新に失敗しました");
        request.setAttribute("mode", "update");
        request.setAttribute("user", userDto);

        return JSP_MEMBER_FORM;
    }

    /**
     * メンバーの有効状態を変更する
     * @return 遷移先
     */
    public String changeValidity() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        int userId = parseInt(getParam("user_id", "userId"));

        if (userId <= 0) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("ユーザーIDが不正です");
        }

        boolean valid = decideValidity();

        UserService service = new UserService();
        int result = service.changeValidity(userId, valid);

        if (result > 0) {
            String message = valid ? "メンバーを有効化しました" : "メンバーを無効化しました";
            return REDIRECT_MEMBER_LIST + "&msg=" + encode(message);
        }

        return REDIRECT_MEMBER_LIST + "&msg=" + encode("有効状態の変更に失敗しました");
    }

    /**
     * パスワードリセット画面を表示する
     * @return 遷移先JSP
     */
    public String showPasswordReset() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        int userId = parseInt(getParam("user_id", "userId"));

        if (userId <= 0) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("ユーザーIDが不正です");
        }

        UserService service = new UserService();
        UserDTO userDto = service.findById(userId);

        if (userDto == null) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("対象メンバーが見つかりません");
        }

        request.setAttribute("user", userDto);

        return JSP_PASSWORD_RESET;
    }

    /**
     * 管理者がパスワードをリセットする
     * @return 遷移先
     */
    public String resetPassword() {

        if (!isAdminUser()) {
            return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます");
        }

        int userId = parseInt(getParam("user_id", "userId"));
        String newPassword = getParam("new_password", "newPassword", "password");
        String confirmPassword = getParam("confirm_password", "confirmPassword");

        String errorMessage = validatePassword(newPassword, confirmPassword);

        if (hasText(errorMessage)) {
            UserDTO userDto = new UserService().findById(userId);
            request.setAttribute("errMsg", errorMessage);
            request.setAttribute("user", userDto);
            return JSP_PASSWORD_RESET;
        }

        UserService service = new UserService();
        int result = service.resetPassword(userId, newPassword);

        if (result > 0) {
            return REDIRECT_MEMBER_LIST + "&msg=" + encode("パスワードをリセットしました");
        }

        UserDTO userDto = service.findById(userId);
        request.setAttribute("errMsg", "パスワードリセットに失敗しました");
        request.setAttribute("user", userDto);

        return JSP_PASSWORD_RESET;
    }

    /**
     * 登録用DTOを作る
     * @return ユーザーDTO
     */
    private UserDTO createUserDtoForRegist() {

        UserDTO userDto = new UserDTO();

        userDto.setLoginId(getParam("login_id", "loginId"));
        userDto.setPasswordHash(getParam("initial_password", "initialPassword", "password"));
        userDto.setName(getParam("name"));
        userDto.setEmail(getParam("email"));
        userDto.setIsAdmin(parseAdmin());
        userDto.setIsValid(true);

        return userDto;
    }

    /**
     * 更新用DTOを作る
     * @return ユーザーDTO
     */
    private UserDTO createUserDtoForUpdate() {

        UserDTO userDto = new UserDTO();

        userDto.setUserId(parseInt(getParam("user_id", "userId")));
        userDto.setName(getParam("name"));
        userDto.setEmail(getParam("email"));
        userDto.setIsAdmin(parseAdmin());
        userDto.setIsValid(parseValid());

        return userDto;
    }

    /**
     * 登録時の入力チェックを行う
     * @param userDto ユーザーDTO
     * @return エラーメッセージ
     */
    private String validateForRegist(UserDTO userDto) {

        if (!hasText(userDto.getLoginId())) {
            return "ログインIDを入力してください";
        }

        if (!hasText(userDto.getPasswordHash())) {
            return "初期パスワードを入力してください";
        }

        if (userDto.getPasswordHash().length() < 6) {
            return "初期パスワードは6文字以上で入力してください";
        }

        if (!hasText(userDto.getName())) {
            return "氏名を入力してください";
        }

        if (!hasText(userDto.getEmail())) {
            return "メールアドレスを入力してください";
        }

        if (!userDto.getEmail().contains("@")) {
            return "メールアドレスの形式が正しくありません";
        }

        return "";
    }

    /**
     * 更新時の入力チェックを行う
     * @param userDto ユーザーDTO
     * @return エラーメッセージ
     */
    private String validateForUpdate(UserDTO userDto) {

        if (userDto.getUserId() <= 0) {
            return "ユーザーIDが不正です";
        }

        if (!hasText(userDto.getName())) {
            return "氏名を入力してください";
        }

        if (!hasText(userDto.getEmail())) {
            return "メールアドレスを入力してください";
        }

        if (!userDto.getEmail().contains("@")) {
            return "メールアドレスの形式が正しくありません";
        }

        return "";
    }

    /**
     * パスワード入力チェックを行う
     * @param newPassword 新パスワード
     * @param confirmPassword 確認用パスワード
     * @return エラーメッセージ
     */
    private String validatePassword(String newPassword, String confirmPassword) {

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
     * 管理者としてログインしているか確認する
     * @return 管理者ならtrue
     */
    private boolean isAdminUser() {

        HttpSession session = request.getSession(false);

        if (session == null) {
            return false;
        }

        Object loginUser = session.getAttribute("loginUser");

        if (!(loginUser instanceof UserDTO)) {
            return false;
        }

        return ((UserDTO) loginUser).getIsAdmin();
    }

    /**
     * キーワードでユーザー一覧を絞り込む
     * @param users ユーザー一覧
     * @param keyword 検索キーワード
     * @return 絞り込み後一覧
     */
    private List<UserDTO> filterUsers(List<UserDTO> users, String keyword) {

        if (users == null) {
            return new ArrayList<>();
        }

        if (!hasText(keyword)) {
            return users;
        }

        List<UserDTO> filteredUsers = new ArrayList<>();

        for (UserDTO user : users) {
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
     * @param value 検索対象
     * @param keyword 検索キーワード
     * @return 含むならtrue
     */
    private boolean contains(String value, String keyword) {

        if (value == null || keyword == null) {
            return false;
        }

        return value.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * 管理者フラグを画面入力から判定する
     * @return 管理者ならtrue
     */
    private boolean parseAdmin() {

        String value = getParam("is_admin", "isAdmin", "role");

        if ("管理者".equals(value)) {
            return true;
        }

        if ("admin".equalsIgnoreCase(value)) {
            return true;
        }

        if ("true".equalsIgnoreCase(value)) {
            return true;
        }

        if ("1".equals(value)) {
            return true;
        }

        if ("on".equalsIgnoreCase(value)) {
            return true;
        }

        return false;
    }

    /**
     * 有効フラグを画面入力から判定する
     * @return 有効ならtrue
     */
    private boolean parseValid() {

        String value = getParam("is_valid", "isValid", "status");

        if (!hasText(value)) {
            return true;
        }

        if ("無効".equals(value)) {
            return false;
        }

        if ("inactive".equalsIgnoreCase(value)) {
            return false;
        }

        if ("false".equalsIgnoreCase(value)) {
            return false;
        }

        if ("0".equals(value)) {
            return false;
        }

        return true;
    }

    /**
     * 有効化か無効化か判定する
     * @return 有効化ならtrue
     */
    private boolean decideValidity() {

        String buttonId = getParam("button_id", "buttonId");

        if ("有効化".equals(buttonId)) {
            return true;
        }

        if ("無効化".equals(buttonId)) {
            return false;
        }

        return parseValid();
    }

    /**
     * requestから値を取得する
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
     * 文字列をintへ変換する
     * @param value 変換前文字列
     * @return 変換後数値
     */
    private int parseInt(String value) {

        if (!hasText(value)) {
            return -1;
        }

        try {
            return Integer.parseInt(value);

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 文字列が入力されているか確認する
     * @param value 確認する文字列
     * @return 入力ありならtrue
     */
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * URL用文字列へ変換する
     * @param value 変換前文字列
     * @return 変換後文字列
     */
    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    /**
     * redirect後のメッセージをrequestへ設定する
     */
    private void setMessageFromParameter() {

        String message = getParam("msg");

        if (hasText(message)) {
            request.setAttribute("successMsg", message);
        }
    }
}