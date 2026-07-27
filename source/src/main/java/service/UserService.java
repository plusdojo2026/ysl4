package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import model.UserDTO;

/**
 * ユーザー関連の業務処理を担当するService
 * 入力チェック、パスワード処理、DAO呼び出し、トランザクション制御を行う
 */
public class UserService extends DBAccess {

    /** パスワード最低文字数 */
    private static final int MIN_PASSWORD_LENGTH = 6;

    /**
     * ログイン処理を行う
     *
     * @param loginId ログインID
     * @param password 入力パスワード
     * @return ログイン成功時はユーザーDTO、失敗時はnull
     */
    public UserDTO login(String loginId, String password) {

        UserDTO loginUser = null;

        if (isBlank(loginId) || isBlank(password)) {
            return null;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            UserDTO dbUser = userDao.findByLoginId(loginId);

            if (dbUser == null) {
                commit();
                return null;
            }

            if (isBlank(dbUser.getPasswordHash())) {
                commit();
                return null;
            }

            if (!checkPassword(password, dbUser.getPasswordHash())) {
                commit();
                return null;
            }

            clearPasswordHash(dbUser);
            loginUser = dbUser;

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return loginUser;
    }

    /**
     * ユーザーを全件取得する
     *
     * @return ユーザー一覧
     */
    public List<UserDTO> selectAll() {

        List<UserDTO> userList = new ArrayList<>();

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            userList = userDao.selectAll();

            clearPasswordHashList(userList);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return userList;
    }

    /**
     * 有効ユーザーだけ取得
     *
     * @return 有効ユーザー一覧
     */
    public List<UserDTO> selectValidUsers() {

        List<UserDTO> userList = new ArrayList<>();

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            userList = userDao.selectValidUsers();

            clearPasswordHashList(userList);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return userList;
    }

    /**
     * ユーザーIDで1件取得
     *
     * @param userId ユーザーID
     * @return ユーザーDTO
     */
    public UserDTO findById(int userId) {

        UserDTO userDto = null;

        if (userId <= 0) {
            return null;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            userDto = userDao.findById(userId);

            clearPasswordHash(userDto);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return userDto;
    }

    /**
     * メンバーを登録
     *
     * @param userDto 登録するユーザーDTO
     * @return 登録件数
     */
    public int regist(UserDTO userDto) {

        int result = 0;

        if (!isValidForRegist(userDto)) {
            return 0;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);

            if (userDao.existsLoginId(userDto.getLoginId())) {
                rollback();
                return 0;
            }

            userDto.setPasswordHash(hashPassword(userDto.getPasswordHash()));
            userDto.setIsValid(true);

            result = userDao.insert(userDto);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    /**
     * メンバー情報を更新
     * ログインIDとパスワードは更新しない
     *
     * @param userDto 更新するユーザーDTO
     * @return 更新件数
     */
    public int update(UserDTO userDto) {

        int result = 0;

        if (!isValidForUpdate(userDto)) {
            return 0;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            result = userDao.update(userDto);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    /**
     * ユーザーの有効状態を変更
     *
     * @param userId ユーザーID
     * @param isValid 有効ならtrue
     * @return 更新件数
     */
    public int changeValidity(int userId, boolean isValid) {

        int result = 0;

        if (userId <= 0) {
            return 0;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            result = userDao.updateValidity(userId, isValid);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    /**
     * 自分のパスワードを変更
     *
     * @param userId ユーザーID
     * @param currentPassword 現在パスワード
     * @param newPassword 新パスワード
     * @return 変更成功ならtrue
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword) {
        return changePassword(userId, currentPassword, newPassword, newPassword);
    }

    /**
     * 自分のパスワードを変更
     * 確認用パスワードも確認
     *
     * @param userId ユーザーID
     * @param currentPassword 現在パスワード
     * @param newPassword 新パスワード
     * @param confirmPassword 確認用パスワード
     * @return 変更成功ならtrue
     */
    public boolean changePassword(
            int userId,
            String currentPassword,
            String newPassword,
            String confirmPassword) {

        boolean success = false;

        if (userId <= 0) {
            return false;
        }

        if (isBlank(currentPassword)
                || isBlank(newPassword)
                || isBlank(confirmPassword)) {
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        if (!isValidPassword(newPassword)) {
            return false;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            UserDTO dbUser = userDao.findById(userId);

            if (dbUser == null || isBlank(dbUser.getPasswordHash())) {
                rollback();
                return false;
            }

            if (!checkPassword(currentPassword, dbUser.getPasswordHash())) {
                rollback();
                return false;
            }

            String newPasswordHash = hashPassword(newPassword);
            int result = userDao.updatePassword(userId, newPasswordHash);

            success = result == 1;

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return success;
    }

    /**
     * 管理者がパスワードをリセットする。
     *
     * @param userId ユーザーID
     * @param newPassword 新パスワード
     * @return 更新件数
     */
    public int resetPassword(int userId, String newPassword) {

        int result = 0;

        if (userId <= 0 || !isValidPassword(newPassword)) {
            return 0;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            result = userDao.updatePassword(userId, hashPassword(newPassword));

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    /**
     * ログインIDが存在するか確認
     *
     * @param loginId ログインID
     * @return 存在する場合true
     */
    public boolean existsLoginId(String loginId) {

        boolean exists = false;

        if (isBlank(loginId)) {
            return false;
        }

        try {
            access();

            UserDAO userDao = new UserDAO(conn);
            exists = userDao.existsLoginId(loginId);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return exists;
    }

    /**
     * 登録時の入力チェックを行う
     *
     * @param userDto ユーザーDTO
     * @return 登録可能ならtrue
     */
    private boolean isValidForRegist(UserDTO userDto) {

        if (userDto == null) {
            return false;
        }

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
     * 更新時の入力チェックを行う
     *
     * @param userDto ユーザーDTO
     * @return 更新可能ならtrue
     */
    private boolean isValidForUpdate(UserDTO userDto) {

        if (userDto == null) {
            return false;
        }

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
     * パスワードが有効か確認
     *
     * @param password パスワード
     * @return 有効ならtrue
     */
    private boolean isValidPassword(String password) {
        return !isBlank(password)
                && password.length() >= MIN_PASSWORD_LENGTH;
    }

    /**
     * パスワードをハッシュ化
     *
     * @param password 平文パスワード
     * @return ハッシュ化済みパスワード
     */
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 入力パスワードとDBハッシュを照合
     *
     * @param plainPassword 入力パスワード
     * @param passwordHash DBハッシュ
     * @return 一致する場合true
     */
    private boolean checkPassword(String plainPassword, String passwordHash) {

        if (isBlank(plainPassword) || isBlank(passwordHash)) {
            return false;
        }

        if (passwordHash.startsWith("$2b$")) {
            passwordHash = "$2a$" + passwordHash.substring(4);
        }

        return BCrypt.checkpw(plainPassword, passwordHash);
    }

    /**
     * DTOからパスワードハッシュを消す
     *
     * @param userDto ユーザーDTO
     */
    private void clearPasswordHash(UserDTO userDto) {

        if (userDto != null) {
            userDto.setPasswordHash(null);
        }
    }

    /**
     * 一覧内DTOからパスワードハッシュを消す
     *
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
     *
     * @param value 確認する文字列
     * @return 未入力ならtrue
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}