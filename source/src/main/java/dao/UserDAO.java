package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.UserDTO;

public class UserDAO {

	public Connection conn = null;

	//コネクションを保持するコンストラクタ
	public UserDAO(Connection conn) {
		this.conn = conn;
	}

	//ログイン認証(ログインIDでユーザーを取得)
	public UserDTO findByLoginId(String loginId, String passwordHash, boolean isAdmin, boolean isValid)
			throws SQLException {
		UserDTO dto = null;

		//SELECT文を用意する
		String sql = "SELECT * FROM Users WHERE loginId = ? and passwordHash = ? and isAdmin = ? and isValid = ?";
		//デバック(SQL文の確認用)
		System.out.println(sql);

		//まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		//?に値をセットする
		pStmt.setString(1, loginId);
		pStmt.setString(2, passwordHash);
		pStmt.setBoolean(3, isAdmin);
		pStmt.setBoolean(4, isValid);

		//SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		//移し替え
		while (rs.next()) {
			dto = new UserDTO();
			dto.setLoginId(rs.getString("loginId"));
			dto.setPasswordHash(rs.getString("passwordHash"));
			dto.setIsAdmin(rs.getBoolean("isAdmin"));
			dto.setIsValid(rs.getBoolean("isValid"));
		}
		//serviceに返却する
		return dto;
	}

	//ユーザーIDで１件取得する
	public UserDTO findById(int userId) throws SQLException {
		UserDTO dto = null;

		//SELECT文を用意する
		String sql = "SELECT FROM Users WHERE userId = ?";
		//デバック(SQL文の確認用)
		System.out.println(sql);

		//まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		//?に値をセットする
		pStmt.setInt(1, userId);

		//SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		//結果が１件あれば、DTOに値をセットする
		if (rs.next()) {
			dto = new UserDTO();
			dto.setUserId(rs.getInt("userId"));
		}
		rs.close();
		pStmt.close();

		return dto;
	}

	//ユーザーの一覧を取得するメソッド
	public ArrayList<UserDTO> selectAll() throws SQLException {
		ArrayList<UserDTO> userList = new ArrayList<UserDTO>();

		//SELECT文を準備する
		String sql = "SELECT * FROM Users";
		//デバック(SQL文の確認用)
		System.out.println(sql);

		//まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		//SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();

		//移し替え
		while (rs.next()) {
			UserDTO dto = new UserDTO();
			dto.setUserId(rs.getInt("userId"));
			dto.setLoginId(rs.getString("loginId"));
			dto.setName(rs.getString("name"));
			dto.setEmail(rs.getString("email"));
			dto.setIsAdmin(rs.getBoolean("isAdmin"));
			dto.setIsValid(rs.getBoolean("isValid"));
			dto.setCreatedAt(rs.getDate("createdAt"));
			dto.setUpdateAt(rs.getDate("updateAt"));
			userList.add(dto);
		}
		//serviceに返却する
		return userList;
	}

	//ユーザーの新規登録
	public int userInsert(int userId, String loginId, String passwordHash, String name, String email, boolean isAdmin,
			boolean isValid, Date createdAt, Date updateAt) throws SQLException {

		int ans = 0;
		//SELECT文を準備する
		String sql = "INSERT INTO Users VALUES(?,?,?,?,?,?,?,?,?)";
		//デバック(SQL文の確認用)
		System.out.println(sql);

		//まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);

		pStmt.setInt(1, userId);
		pStmt.setString(2, loginId);
		pStmt.setString(3, passwordHash);
		pStmt.setString(4, name);
		pStmt.setString(5, email);
		pStmt.setBoolean(6, isAdmin);
		pStmt.setBoolean(7, isValid);
		pStmt.setDate(8, createdAt);
		pStmt.setDate(9, updateAt);

		//SELECT文を実行し、結果表を取得する
		ans = pStmt.executeUpdate();

		//serviceに返却する
		return ans;
	}

	//ユーザー情報を更新する
	public int update(int userId, String passwordHash, String name, String email, boolean isAdmin,
				boolean isValid, Date createdAt, Date updateAt) throws SQLException {
		
			int ans = 0;
			//SELECT文を用意する
			String sql = "UPDATE Users SET userId = ?, passwordHash = ?, name = ?, email = ?, isAdmin = ?, isValid = ?, createdAt = ?, updateAt = ?";
			//デバック(SQL文の確認用)
			System.out.println(sql);
			
			//まとめる
			PreparedStatement pStmt = conn.prepareStatement(sql);
			
			pStmt.setInt(1, userId);
			pStmt.setString(2, passwordHash);
			pStmt.setString(3, name);
			pStmt.setString(4, email);
			pStmt.setBoolean(5, isAdmin);
			pStmt.setBoolean(6, isValid);
			pStmt.setDate(7, createdAt);
			pStmt.setDate(8, updateAt);
			
			//SELECT文を実行し、結果表を取得する
			ans = pStmt.executeUpdate();
			
			//serviceに返却する
			return ans;				
	}
	
	//パスワードの更新
	public int updatePassword(String passwordHash) throws SQLException {
		
		int ans = 0;
		//SELECT文を用意する
		String sql = "UPDATE Users SET passwordHash = ?";
		//デバック(SQL文の確認用)
		System.out.println(sql);
		
		//まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		
		pStmt.setString(1, passwordHash);
		
		//SELECT文を実行し、結果表を取得する
		ans = pStmt.executeUpdate();
		
		//serviceに返却する
		return ans;
		
	}
	
	//ログインIDの重複チェック
	public boolean existLoginId(String loginId) throws SQLException {
		
		String sql = "SELECT COUNT(*) AS cnt FROM Users WHERE login_id = ?";
		
		PreparedStatement pStmt = conn.prepareStatement(sql);
		
		pStmt.setString(1, loginId);
		
		//重複していたらtrueで返す
		try(ResultSet rs = pStmt.executeQuery()) {
			if(rs.next()) {
				return rs.getInt("cnt") > 0;
			}
		}
		return false;
		
	}
	
	//有効なメンバーだけ取得する
	public ArrayList<UserDTO>selectValidUsers() throws SQLException{
		ArrayList<UserDTO> validUserList = new ArrayList<UserDTO>();

		String sql = "SELECT * FROM users WHERE is_valid= ture";
		PreparedStatement pStmt = conn.prepareStatement(sql);
		try(ResultSet rs = pStmt.executeQuery()) {

		//移し替え
		while (rs.next()) {
		UserDTO dto = new UserDTO();
				dto.setUserId(rs.getInt("user_id"));
				dto.setLoginId(rs.getString("login_id"));
				dto.setPasswordHash(rs.getString("password_hash"));
				dto.setName(rs.getString("name"));
				dto.setEmail(rs.getString("email"));
				dto.setIsAdmin(rs.getBoolean("is_admin"));
				dto.setIsValid(rs.getBoolean("is_valid"));
				dto.setCreatedAt(rs.getDate("c_at"));
				dto.setUpdateAt(rs.getDate("u_at"));
				validUserList.add(dto);
			}
		}
			//serviceに返却する
			return validUserList;
	}

	//ユーザーの有効・無効状態の更新
	public int updateValidity(int userId, boolean isValid) throws SQLException{
		int ans = 0;
		
		String sql = "Update users SET is_valid= ? WHERE user_id = ?";
		// まとめる
			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setBoolean(1, isValid);
			pStmt.setInt(2, userId);
			
			// SELECT文を実行し、結果表を取得する
			ans = pStmt.executeUpdate();

			//serviceに返却する
			return ans;
	}

}