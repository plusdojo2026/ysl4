package action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.UserDTO;
import service.UserService;

public class MemberAction {

	//メンバー一覧JSP
	private static final String JSP_MEMBER_LIST = "/WEB-INF/jsp/memberList.jsp";

	//メンバー登録JSP
	private static final String JSP_MEMBER_REGIST = "/WEB-INF/jsp/memberRegist.jsp";

	//メンバー編集JSP
	private static final String JSP_MEMBER_EDIT = "/WEB-INF/jsp/menberEdit.jsp";

	//パスワードリセットJSP
	private static final String JSP_PW_RESET = "/WEB-INF/jsp/passwordReset.jsp";

	//ホーム画面へ戻す(リダイレクト)
	private static final String REDIRECT_HOME = "redirect:Controller?page_id=D001";

	//メンバー一覧へ戻す(リダイレクト)
	private static final String REDIRECT_MEMBER_LIST = "redirect:Controller?page_id=M001";

	HttpServletRequest request;

	//コンストラクタ
	public MemberAction(HttpServletRequest request) {
		this.request = request;
	}

	public String list() {
		//一般ユーザーはアクセスできないバリデーション
		if (!isAdminUser()) {
			return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます。");
		}
		//インスタンス化&ServiceのselectAllメソッドを呼び出す
		UserService service = new UserService();
		List<UserDTO> users = service.selectAll();

		String keyword = request.getParameter("keyword");
		List<UserDTO> displayUsers = filterUsers(users, keyword);

		request.setAttribute("users", displayUsers);
		request.setAttribute("keyword", keyword);
		setMessageFromParameter();

		return JSP_MEMBER_LIST;
	}
	//従業員情報の一覧表示-------------------------------------------
	//	public ArrayList throws UnsupportedEncodingException {
	//		
	//		//値の取得
	//		request.setCharacterEncoding("UTF-8");
	//		String userId = request.getParameter("user_id");
	//		String loginId = request.getParameter("login_id");
	//		String name = request.getParameter("name");
	//		String email = request.getParameter("email");
	//		String IsAdmin = request.getParameter("is_admin");
	//		String IsValid = request.getParameter("is_valid");
	//		String CreatedAt = request.getParameter("c_at");
	//		String UpdateAt = request.getParameter("u_at");
	//		
	//		//管理者かどうかをチェックする
	//		
	//		UserService service = new UserService();
	//		ArrayList<UserDTO> userList = service.selectAll();
	//		
	//		
	//	
	//		
	//	}

	//メンバー登録画面の表示---------------------------------

	//メンバーの登録メソッド---------------------------------
	public String regist() throws UnsupportedEncodingException {
		//DTOの箱を用意
	
	//一般ユーザーはアクセスできないバリデーション
	if(!isAdminUser()) {
		return REDIRECT_HOME + "&msg=" + encode("管理者のみ利用できます。");
	}
	
	UserDTO dto = createUserDtoForRegist();
	
	//入力値チェック
	if(loginId.trim().equals("") || name.trim().equals("") || IsAdmin.trim().equals("") || IsValid.trim().equals("")) {
		request.setAttribute("errMsg", "※未入力の項目があります。");
		return JSP_MEMBER_REGIST;
	}
	
	//必須項目すべてが入力されていたら、serviceを実体化
	UserService service = new UserService();
	}
	
	/**
	*現在のセッションにログインしているユーザーが管理者(Admin)かどうかを判定するための補助メソッド
	*/
	private  boolean isAdminUser() {

		//セッションを取得(セッションがなければ、nullを返す)
		HttpSession session = request.getSession(false);

		//セッション存在確認
		if(session == null) {
	 	return false;
		}

		//セッションからログインユーザー情報を取得(loginUserにUserDTOオブジェクトを格納)
		Object loginUser = session.getAttribute("loginUser");

		//UserDTO型かチェック(instanceofは、このオブジェクトは UserDTO 型か確認する演算子)
		if(!(loginUser instanceof UserDTO)) {
	 	return false;
		}

		//管理者フラグを返す
		return ((UserDTO) loginUser).getIsAdmin();
	}
	
	private String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}
	
	/**
     * 登録用のUserDTOを作る
     * メンバー登録画面の入力値をDTOに詰める
     * @return 登録用ユーザーDTO
     */
    private UserDTO createUserDtoForRegist() {

        // 空DTOを作る
        UserDTO userDto = new UserDTO();

        // ログインIDを設定する
        userDto.setLoginId(request.getParameter("login_id"));

        // 初期パスワードを設定する
        userDto.setPasswordHash(request.getParameter("initial_password"));

        // 氏名を設定する
        userDto.setName(request.getParameter("name"));

        // メールアドレスを設定する
        userDto.setEmail(request.getParameter("email"));

        // 管理者フラグを設定する
        userDto.setAdmin(parseAdmin());

        // 登録時は有効ユーザーとして扱う
        userDto.setIsValid(true);

        return userDto;
    }

}
