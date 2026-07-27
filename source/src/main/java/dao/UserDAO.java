package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.UserDTO;

/**
 * Usersテーブルを操作するDAO
 * ログイン、メンバー一覧、登録、更新、パスワード更新を担当
 */
public class UserDAO {

    /** DB接続 */
    private final Connection conn;

    /**
     * DB接続を受け取る
     *
     * @param conn DB接続
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * ログインIDで有効ユーザーを1件取得
     * ログイン認証で使う
     *
     * @param loginId ログインID
     * @return ユーザーDTO
     * @throws SQLException SQLエラー
     */
    public UserDTO findByLoginId(String loginId) throws SQLException {

        UserDTO userDto = null;

        String sql = baseSelectSql()
                + " WHERE login_id = ?"
                + " AND is_valid = true";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loginId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userDto = setToUserDTO(rs);
                }
            }
        }

        return userDto;
    }

    /**
     * ユーザーIDで1件取得
     * メンバー編集やパスワード変更で使う
     *
     * @param userId ユーザーID
     * @return ユーザーDTO
     * @throws SQLException SQLエラー
     */
    public UserDTO findById(int userId) throws SQLException {

        UserDTO userDto = null;

        String sql = baseSelectSql()
                + " WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    userDto = setToUserDTO(rs);
                }
            }
        }

        return userDto;
    }

    /**
     * 全ユーザーを取得
     * メンバー一覧で使う
     *
     * @return ユーザー一覧
     * @throws SQLException SQLエラー
     */
    public List<UserDTO> selectAll() throws SQLException {

        List<UserDTO> userList = new ArrayList<>();

        String sql = baseSelectSql()
                + " ORDER BY user_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                userList.add(setToUserDTO(rs));
            }
        }

        return userList;
    }

    /**
     * 有効ユーザーだけ取得
     * PM候補や担当者候補で使う
     *
     * @return 有効ユーザー一覧
     * @throws SQLException SQLエラー
     */
    public List<UserDTO> selectValidUsers() throws SQLException {

        List<UserDTO> userList = new ArrayList<>();

        String sql = baseSelectSql()
                + " WHERE is_valid = true"
                + " ORDER BY name ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                userList.add(setToUserDTO(rs));
            }
        }

        return userList;
    }

    /**
     * ユーザーを登録する
     * user_id、c_at、u_atはDB側に任せる
     *
     * @param userDto 登録するユーザーDTO
     * @return 登録件数
     * @throws SQLException SQLエラー
     */
    public int insert(UserDTO userDto) throws SQLException {

        String sql =
                "INSERT INTO Users ("
                        + "login_id, "
                        + "password_hash, "
                        + "name, "
                        + "email, "
                        + "is_admin, "
                        + "is_valid"
                        + ") VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userDto.getLoginId());
            ps.setString(2, userDto.getPasswordHash());
            ps.setString(3, userDto.getName());
            ps.setString(4, userDto.getEmail());
            ps.setBoolean(5, userDto.getIsAdmin());
            ps.setBoolean(6, userDto.getIsValid());

            return ps.executeUpdate();
        }
    }

    /**
     * 互換用メソッド
     *
     * @param userDto 登録するユーザーDTO
     * @return 登録件数
     * @throws SQLException SQLエラー
     */
    public int userInsert(UserDTO userDto) throws SQLException {
        return insert(userDto);
    }

    /**
     * ユーザー情報を更新する
     * ログインIDとパスワードは更新しない
     *
     * @param userDto 更新するユーザーDTO
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int update(UserDTO userDto) throws SQLException {

        String sql =
                "UPDATE Users SET "
                        + "name = ?, "
                        + "email = ?, "
                        + "is_admin = ?, "
                        + "is_valid = ?, "
                        + "u_at = CURRENT_TIMESTAMP "
                        + "WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userDto.getName());
            ps.setString(2, userDto.getEmail());
            ps.setBoolean(3, userDto.getIsAdmin());
            ps.setBoolean(4, userDto.getIsValid());
            ps.setInt(5, userDto.getUserId());

            return ps.executeUpdate();
        }
    }

    /**
     * 互換用メソッド
     *
     * @param userDto 更新するユーザーDTO
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int userUpdate(UserDTO userDto) throws SQLException {
        return update(userDto);
    }

    /**
     * 有効状態を更新する
     *
     * @param userId ユーザーID
     * @param isValid 有効ならtrue
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int updateValidity(int userId, boolean isValid) throws SQLException {

        String sql =
                "UPDATE Users SET "
                        + "is_valid = ?, "
                        + "u_at = CURRENT_TIMESTAMP "
                        + "WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBoolean(1, isValid);
            ps.setInt(2, userId);

            return ps.executeUpdate();
        }
    }

    /**
     * パスワードを更新する
     *
     * @param userId ユーザーID
     * @param passwordHash ハッシュ化済みパスワード
     * @return 更新件数
     * @throws SQLException SQLエラー
     */
    public int updatePassword(int userId, String passwordHash) throws SQLException {

        String sql =
                "UPDATE Users SET "
                        + "password_hash = ?, "
                        + "u_at = CURRENT_TIMESTAMP "
                        + "WHERE user_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, passwordHash);
            ps.setInt(2, userId);

            return ps.executeUpdate();
        }
    }

    /**
     * ログインIDが存在するか確認する
     *
     * @param loginId ログインID
     * @return 存在する場合true
     * @throws SQLException SQLエラー
     */
    public boolean existsLoginId(String loginId) throws SQLException {

        String sql =
                "SELECT COUNT(*) AS user_count "
                        + "FROM Users "
                        + "WHERE login_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, loginId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_count") > 0;
                }
            }
        }

        return false;
    }

    /**
     * 互換用メソッド
     *
     * @param loginId ログインID
     * @return 存在する場合true
     * @throws SQLException SQLエラー
     */
    public boolean existLoginId(String loginId) throws SQLException {
        return existsLoginId(loginId);
    }

    /**
     * ユーザー取得用の共通SQLを作る
     *
     * @return 共通SELECT文
     */
    private String baseSelectSql() {

        return "SELECT "
                + "user_id, "
                + "login_id, "
                + "password_hash, "
                + "name, "
                + "email, "
                + "is_admin, "
                + "is_valid, "
                + "DATE_FORMAT(c_at, '%Y-%m-%d %H:%i:%s') AS c_at, "
                + "DATE_FORMAT(u_at, '%Y-%m-%d %H:%i:%s') AS u_at "
                + "FROM Users";
    }

    /**
     * ResultSetの1行をUserDTOへ変換する
     *
     * @param rs SQL取得結果
     * @return ユーザーDTO
     * @throws SQLException SQLエラー
     */
    private UserDTO setToUserDTO(ResultSet rs) throws SQLException {

        UserDTO userDto = new UserDTO();

        userDto.setUserId(rs.getInt("user_id"));
        userDto.setLoginId(rs.getString("login_id"));
        userDto.setPasswordHash(rs.getString("password_hash"));
        userDto.setName(rs.getString("name"));
        userDto.setEmail(rs.getString("email"));
        userDto.setIsAdmin(rs.getBoolean("is_admin"));
        userDto.setIsValid(rs.getBoolean("is_valid"));
        userDto.setCreatedAt(rs.getString("c_at"));
        userDto.setUpdatedAt(rs.getString("u_at"));

        return userDto;
    }
}