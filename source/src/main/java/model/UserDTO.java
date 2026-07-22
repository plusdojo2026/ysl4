package model;

import java.io.Serializable;
import java.sql.Date;

public class UserDTO implements Serializable {
	private int userId;//ユーザーID
	private int loginId;//ログインID
	private String passwordHash;//パスワード
	private String name;//名前
	private String email;//メールアドレス
	private boolean isAdmin;//権限フラグ
	private boolean isValid;//ステータス
	private Date createdAt;//作成日
	private Date updateAt;//編集日
	
	//ゲッターとセッター
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getLoginId() {
		return loginId;
	}
	
	public void setLoginId(int loginId) {
		this.loginId = loginId;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean getIsAdmin() {
		return isAdmin;
	}
	
	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public boolean getIsValid() {
		return isValid;
	}
	
	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getUpdateAt() {
		return updateAt;
	}
	
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}