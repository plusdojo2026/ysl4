package service;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import model.UserDTO;
/**
 * ユーザー関連の業務処理を担当するService
 * ActionとDAOの間に入り、入力チェックやパスワード処理を行う
 */
public class UserService extends DBAccess {
    /** パスワードの最低文字数 */
    private static final int MIN_PASSWORD_LENGTH = 6;
    /**
     * ログイン処理を行う
     * loginIdでユーザーを取得し、BCryptでパスワードを照合する
     * @param loginId ログインID
     * @param password 入力されたパスワード
     * @return ログイン成功時はユーザー情報、失敗時はnull
     */
    public UserDTO login(String loginId, String password) {
        UserDTO loginUser = null;
        if (isBlank(loginId) || isBlank(password)) {
            return null;
        }
        try {
            // DB接続を開始する
            access();
            // UserDAOを作成する
            UserDAO dao = new UserDAO(conn);
            // ログインIDから有効ユーザーを取得する
            UserDTO dbUser = dao.findByLoginId(loginId);
            
            //ログ
            String hash = BCrypt.hashpw("password123", BCrypt.gensalt());
            System.out.println(hash);
            System.out.println(BCrypt.checkpw("password123", hash));
            
            System.out.println(BCrypt.class.getProtectionDomain().getCodeSource().getLocation());
            
            // ユーザーが存在しない場合はログイン失敗
            if (dbUser == null) {
                commit();
                return null;
            }
            // DBにパスワードハッシュがない場合はログイン失敗
            if (isBlank(dbUser.getPasswordHash())) {
                commit();
                return null;
            }
            // 入力パスワードとDBのハッシュを照合する
            if (!checkPassword(password, dbUser.getPasswordHash())) {
                commit();
                return null;
            }
            // セッションに不要なパスワードハッシュを消す
            clearPasswordHash(dbUser);
            // ログイン成功ユーザーとして返す
            loginUser = dbUser;
            // 参照処理だがDB処理完了としてcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return loginUser;
    }
    /**
     * ユーザーIDでユーザー情報を1件取得する
     * メンバー編集やパスワード変更で使う
     * @param userId ユーザーID
     * @return ユーザー情報
     */
    public UserDTO findById(int userId) {
        UserDTO userDto = null;
        if (userId <= 0) {
            return null;
        }
        try {
            // DB接続を開始する
            access();
            // DAOでユーザーを1件取得する
            UserDAO dao = new UserDAO(conn);
            userDto = dao.findById(userId);
            // 参照処理の完了としてcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return userDto;
    }
    /**
     * ユーザーを全件取得する
     * メンバー一覧画面で使う
     * @return ユーザー一覧
     */
    public List<UserDTO> selectAll() {
        List<UserDTO> userList = new ArrayList<>();
        try {
            // DB接続を開始する
            access();
            // DAOで全ユーザーを取得する
            UserDAO dao = new UserDAO(conn);
            userList = dao.selectAll();
            // 一覧表示ではパスワードハッシュを画面に渡さない
            clearPasswordHashList(userList);
            // 参照処理の完了としてcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return userList;
    }
    /**
     * 有効ユーザーだけを取得する
     * 案件PMやタスク担当者の選択肢で使う
     * @return 有効ユーザー一覧
     */
    public List<UserDTO> selectValidUsers() {
        List<UserDTO> userList = new ArrayList<>();
        try {
            // DB接続を開始する
            access();
            // DAOで有効ユーザーだけ取得する
            UserDAO dao = new UserDAO(conn);
            userList = dao.selectValidUsers();
            // 選択肢表示ではパスワードハッシュを渡さない
            clearPasswordHashList(userList);
            // 参照処理の完了としてcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return userList;
    }
    /**
     * メンバーを登録する
     * UserDTOのpasswordHashには初期パスワードが入っている想定
     * @param userDto 登録するユーザー情報
     * @return 登録件数
     */
    public int regist(UserDTO userDto) {
        int result = 0;
        if (userDto == null) {
            return 0;
        }
        if (!isValidForRegist(userDto)) {
            return 0;
        }
        try {
            // DB接続を開始する
            access();
            // DAOを作成する
            UserDAO dao = new UserDAO(conn);
            // ログインIDの重複を確認する
            if (dao.existsLoginId(userDto.getLoginId())) {
                rollback();
                return 0;
            }
            // 入力された初期パスワードをハッシュ化する
            String hashedPassword = hashPassword(userDto.getPasswordHash());
            // DB保存用にハッシュ化済みパスワードを設定する
            userDto.setPasswordHash(hashedPassword);
            // 未指定の場合は有効ユーザーとして登録する
            userDto.setIsValid(true);
            // DAOでユーザーを登録する
            result = dao.userInsert(userDto);
            // 登録成功時はcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return result;
    }
    /**
     * メンバー情報を更新する
     * ログインIDとパスワードはここでは更新しない
     * @param userDto 更新するユーザー情報
     * @return 更新件数
     */
    public int update(UserDTO userDto) {
        int result = 0;
        if (userDto == null) {
            return 0;
        }
        if (!isValidForUpdate(userDto)) {
            return 0;
        }
        try {
            // DB接続を開始する
            access();
            // DAOでユーザー情報を更新する
            UserDAO dao = new UserDAO(conn);
            result = dao.userUpdate(userDto);
            // 更新成功時はcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return result;
    }
    /**
     * ユーザーの有効状態を変更する
     * 有効化と無効化で使う
     * @param userId ユーザーID
     * @param isValid 有効ならtrue
     * @return 更新件数
     */
    public int changeValidity(int userId, boolean isValid, Date updateAt) {
        int result = 0;
        if (userId <= 0) {
            return 0;
        }
        try {
            // DB接続を開始する
            access();
            // DAOで有効状態を更新する
            UserDAO dao = new UserDAO(conn);
            result = dao.updateValidity(userId, isValid, updateAt);
            // 更新成功時はcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return result;
    }
    /**
     * 自分のパスワードを変更する
     * 現在パスワードを確認してから新しいパスワードへ変更する
     * @param userId ユーザーID
     * @param currentPassword 現在パスワード
     * @param newPassword 新しいパスワード
     * @param confirmPassword 確認用パスワード
     * @return 変更成功ならtrue
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword, String confirmPassword) {
        if (userId <= 0) {
            return false;
        }
        if (isBlank(currentPassword) || isBlank(newPassword) || isBlank(confirmPassword)) {
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            return false;
        }
        if (!isValidPassword(newPassword)) {
            return false;
        }
        boolean success = false;
        try {
            // DB接続を開始する
            access();
            // DAOで現在のユーザー情報を取得する
            UserDAO dao = new UserDAO(conn);
            UserDTO dbUser = dao.findById(userId);
            // ユーザーが存在しない場合は変更不可
            if (dbUser == null) {
                rollback();
                return false;
            }
            // 現在パスワードの比較元がない場合は変更不可
            if (isBlank(dbUser.getPasswordHash())) {
                rollback();
                return false;
            }
            // 入力された現在パスワードをDBのハッシュと照合する
            if (!checkPassword(currentPassword, dbUser.getPasswordHash())) {
                rollback();
                return false;
            }
            // 新しいパスワードをハッシュ化する
            String newPasswordHash = hashPassword(newPassword);
            // DAOでパスワードを更新する
            int result = dao.updatePassword(userId, newPasswordHash);
            // 更新件数が1件なら成功
            success = result == 1;
            // 更新成功時はcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return success;
    }
    /**
     * 管理者がパスワードをリセットする
     * 現在パスワードの確認は行わない
     * @param userId ユーザーID
     * @param newPassword 新しいパスワード
     * @return 更新件数
     */
    public int resetPassword(int userId, String newPassword) {
        int result = 0;
        if (userId <= 0) {
            return 0;
        }
        if (!isValidPassword(newPassword)) {
            return 0;
        }
        try {
            // DB接続を開始する
            access();
            // 新しいパスワードをハッシュ化する
            String newPasswordHash = hashPassword(newPassword);
            // DAOでパスワードを更新する
            UserDAO dao = new UserDAO(conn);
            result = dao.updatePassword(userId, newPasswordHash);
            // 更新成功時はcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return result;
    }
    /**
     * ログインIDが既に存在するか確認する
     * メンバー登録時の重複チェックで使う
     * @param loginId ログインID
     * @return 存在する場合true
     */
    public boolean existsLoginId(String loginId) {
        if (isBlank(loginId)) {
            return false;
        }
        boolean exists = false;
        try {
            // DB接続を開始する
            access();
            // DAOでログインIDの存在確認を行う
            UserDAO dao = new UserDAO(conn);
            exists = dao.existsLoginId(loginId);
            // 参照処理の完了としてcommitする
            commit();
        } catch (SQLException e) {
            // SQLエラー時はrollbackする
            rollback();
            e.printStackTrace();
        } finally {
            // DB接続を閉じる
            close();
        }
        return exists;
    }
    /**
     * 登録時の最低限チェックを行う
     * 画面側でもチェックするがService側でも防御する
     * @param userDto 登録するユーザー情報
     * @return 登録可能ならtrue
     */
    private boolean isValidForRegist(UserDTO userDto) {
        if (isBlank(userDto.getLoginId())) {
            return false;
        }
        if (isBlank(userDto.getPasswordHash())) {
            return false;
        }
        if (!isValidPassword(userDto.getPasswordHash())) {
            return false;
        }
        if (isBlank(userDto.getName())) {
            return false;
        }
        if (isBlank(userDto.getEmail())) {
            return false;
        }
        if (!userDto.getEmail().contains("@")) {
            return false;
        }
        return true;
    }
    /**
     * 更新時の最低限チェックを行う
     * ユーザーID、氏名、メールアドレスを確認する
     * @param userDto 更新するユーザー情報
     * @return 更新可能ならtrue
     */
    private boolean isValidForUpdate(UserDTO userDto) {
        if (userDto.getUserId() <= 0) {
            return false;
        }
        if (isBlank(userDto.getName())) {
            return false;
        }
        if (isBlank(userDto.getEmail())) {
            return false;
        }
        if (!userDto.getEmail().contains("@")) {
            return false;
        }
        return true;
    }
    /**
     * パスワードの最低限チェックを行う
     * 6文字以上を正常とする
     * @param password パスワード
     * @return 正常ならtrue
     */
    private boolean isValidPassword(String password) {
        if (isBlank(password)) {
            return false;
        }
        return password.length() >= MIN_PASSWORD_LENGTH;
    }
    /**
     * パスワードをBCryptでハッシュ化する
     * DBには平文パスワードを保存しない
     * @param password 平文パスワード
     * @return ハッシュ化済みパスワード
     */
    private String hashPassword(String password) {
    	
    	String hash = BCrypt.hashpw(password, BCrypt.gensalt());
    	
    	System.out.println("generatedHash=" + hash);
    	System.out.println("length=" + hash.length());
    	return hash;
    	
    	}
    /**
     * 入力パスワードとDBのハッシュを照合する
     * BCryptは同じパスワードでも毎回違うハッシュになるためcheckpwで確認する
     * @param plainPassword 入力パスワード
     * @param passwordHash DBのハッシュ
     * @return 一致する場合true
     */
    private boolean checkPassword(String plainPassword, String passwordHash) {

        System.out.println("入力PW=" + plainPassword);
        System.out.println("DB Hash=" + passwordHash);

        if (passwordHash.startsWith("$2b$")) {
            passwordHash = "$2a$" + passwordHash.substring(4);
        }

        boolean result = BCrypt.checkpw(plainPassword, passwordHash);

        System.out.println("照合結果=" + result);

        return result;
    }    

    
    /**
     * DTOからパスワードハッシュを消す
     * 画面やセッションへ不要な情報を渡さないため
     * @param userDto ユーザーDTO
     */
    private void clearPasswordHash(UserDTO userDto) {
        if (userDto != null) {
            userDto.setPasswordHash(null);
        }
    }
    /**
     * 一覧内のDTOからパスワードハッシュを消す
     * メンバー一覧や選択肢に不要な情報を渡さないため
     * @param userList ユーザー一覧
     */
    private void clearPasswordHashList(List<UserDTO> userList) {
        if (userList == null) {
            return;
        }
        for (UserDTO userDto : userList) {
            clearPasswordHash(userDto);
        }
    }
    /**
     * 文字列が未入力か確認する
     * nullと空文字を未入力として扱う
     * @param value 確認する文字列
     * @return 未入力ならtrue
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}