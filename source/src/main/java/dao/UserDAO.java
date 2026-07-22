package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import UserDTO;

public class UserDAO {
	
	public Connection conn = null;
	
	//コネクションを保持するコンストラクタ
	public UserDAO(Connection conn) {
		this.conn = conn;
	}
	
	//ログイン認証(ログインIDでユーザーを取得)
	public UserDTO findByLoginId(String loginId, String passwordHash) throws SQLException {
		UserDTO dto = null;
		
		//SELECT文を用意する
		String sql = "SELECT * FROM Users WHERE loginId = ? and passwordHash = ?";
		//デバック(SQL文の確認用)
		System.out.println(sql);
		
		//まとめる
		PreparedStatement pStmt = conn.prepareStatement(sql);
		
		//?に値をセットする
		pStmt.setString(1, loginId);
		pStmt.setString(2, passwordHash);
		
		//SELECT文を実行し、結果表を取得する
		ResultSet rs = pStmt.executeQuery();
		
		//移し替え
		while(rs.next()) {
			dto = new UserDTO();
			dto.setLoginId(rs.getString("loginId"));
			dto.setPasswordHash(rs.getString("passwordHash"));
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
		if(rs.next()) {
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
		while(rs.next()) {
			UserDTO dto = new UserDTO();
			dto.setLoginId(rs.getString("LoginId"));
			dto.setName(rs.setString("name"));
			dto.setEmail(rs.setString("email"));
			dto.setIsAdmin(rs.setString("isAdmin"));
			dto.setIsValid(rs.setString("isValid"));
			dto.setCreatedAt(rs.setDate("createdAt"));
			dto.setUpdateAt(rs.setDate)
		}
	}
	
	
	
}