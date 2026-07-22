package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import dao.UserDAO;
import model.UserDTO;

public class UserService extends DBAccess {

    public UserDTO login(String loginId, String password) {

        // クラス図準拠のpublicメソッド
        // ログインIDとパスワードでログイン可否を確認する
        if (isBlank(loginId) || isBlank(password)) {
            return null;
        }

        UserDTO user = null;

        try {
            access();

            UserDAO dao = new UserDAO(conn);
            UserDTO dbUser = dao.findByLoginId(loginId.trim());

            // ユーザーが存在しない場合はログイン失敗にする
            if (dbUser == null) {
                rollback();
                return null;
            }

            // 無効ユーザーはログイン不可にする
            if (!dbUser.isValid()) {
                rollback();
                return null;
            }

            // BCryptで入力値とDBのハッシュ値を照合する
            if (isPasswordMatch(password, dbUser.getPasswordHash())) {
                user = dbUser;
            }

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return user;
    }

    public List<UserDTO> selectAll() {

        // クラス図準拠のpublicメソッド
        // メンバー一覧で使う全ユーザーを取得する
        List<UserDTO> list = new ArrayList<>();

        try {
            access();
            UserDAO dao = new UserDAO(conn);
            list = dao.selectAll();
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return list;
    }

    public List<UserDTO> selectValidUsers() {

        // クラス図準拠のpublicメソッド
        // 担当者やPMの選択肢で使う有効ユーザーを取得する
        List<UserDTO> list = new ArrayList<>();

        try {
            access();
            UserDAO dao = new UserDAO(conn);
            list = dao.selectValidUsers();
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return list;
    }

    public UserDTO findById(int userId) {

        // クラス図準拠のpublicメソッド
        // ユーザーIDで1件取得する
        UserDTO user = null;

        if (userId <= 0) {
            return null;
        }

        try {
            access();
            UserDAO dao = new UserDAO(conn);
            user = dao.findById(userId);
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return user;
    }

    public int regist(UserDTO userDto) {

        // クラス図準拠のpublicメソッド
        // メンバー登録で使う
        if (userDto == null || isBlank(userDto.getLoginId()) || isBlank(userDto.getPasswordHash())) {
            return 0;
        }

        int result = 0;

        try {
            access();

            UserDAO dao = new UserDAO(conn);

            // 初期パスワードをBCryptで保存する
            userDto.setPasswordHash(hashPassword(userDto.getPasswordHash()));
            result = dao.userInsert(userDto);

            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    public int update(UserDTO userDto) {

        // クラス図準拠のpublicメソッド
        // メンバー編集で使う
        if (userDto == null || userDto.getUserId() <= 0) {
            return 0;
        }

        int result = 0;

        try {
            access();
            UserDAO dao = new UserDAO(conn);
            result = dao.userUpdate(userDto);
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    public int resetPassword(int userId, String newPassword) {

        // クラス図準拠のpublicメソッド
        // 管理者のパスワードリセットとログイン者の変更保存で使う
        if (userId <= 0 || isBlank(newPassword)) {
            return 0;
        }

        int result = 0;

        try {
            access();
            UserDAO dao = new UserDAO(conn);
            result = dao.updatePassword(userId, hashPassword(newPassword));
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return result;
    }

    public boolean existsLoginId(String loginId) {

        // クラス図準拠のpublicメソッド
        // ログインID重複チェックで使う
        if (isBlank(loginId)) {
            return false;
        }

        boolean exists = false;

        try {
            access();
            UserDAO dao = new UserDAO(conn);
            exists = dao.existsLoginId(loginId.trim());
            commit();

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();

        } finally {
            close();
        }

        return exists;
    }

    private static String hashPassword(String password) {

        // private staticなのでUserService内部だけで使うハッシュ化処理
        // 同じ入力でも毎回違うハッシュ値になる
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private static boolean isPasswordMatch(String plainPassword, String passwordHash) {

        // private staticなのでUserService内部だけで使う照合処理
        // 画面入力値とDB保存値を比較する
        if (isBlank(plainPassword) || isBlank(passwordHash)) {
            return false;
        }

        return BCrypt.checkpw(plainPassword, passwordHash);
    }

    private static boolean isBlank(String value) {

        // private staticなのでUserService内部だけで使う空文字判定
        // nullと空文字を同じ扱いにする
        return value == null || value.trim().isEmpty();
    }
}