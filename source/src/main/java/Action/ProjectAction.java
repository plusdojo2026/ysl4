package Action;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import model.UserDTO;
import service.UserService;

public class ProjectAction {
	HttpServletRequest request ;
	//コンストラクタ
	public ProjectAction(HttpServletRequest request) {
		this.request=request;
	}
	
	//案件の登録メソッド---------------------------------------
		public int projectInsert() throws UnsupportedEncodingException {
?????????????????????????????????????????????????????????????????????????????
         
}

			String page="/WEB-INF/jsp/projectList.jsp";
			????????????????????????????????????????????????
			//値の取得
			request.setCharacterEncoding("UTF-8");		
			String project_code = request.getParameter("project_code");
			String  project_name= request.getParameter("project_name");			
			String customer_name = request.getParameter("customer_name");
			int project_manager_id = request.getParameter("project_manager_id");
			int
			
			
			//必要ならここで入力値チェックをする
			if(kan==null) {
				request.setAttribute("msg", "※管理者チェックを付けてください");
				//ユーザー情報を全て取得する
				UserService service = new UserService();
				ArrayList<UserDTO> userList = service.selectAll();
				request.setAttribute("userList", userList);
				return page;
			}
			UserService service = new UserService();
			//serviceに処理を依頼
			int ans = service.userRegist(id,pw,name,address ,kan);
			//ちゃんと登録できたか確認
			if(ans == 1) {
				request.setAttribute("msg", "※"+name+"の登録完了！");
			}else {
				request.setAttribute("msg", "※登録失敗！IDが重複しています");
			}
			//ユーザー情報を全て取得する
			ArrayList<UserDTO> userList = service.selectAll();
			request.setAttribute("userList", userList);
			
			return page;  
		}