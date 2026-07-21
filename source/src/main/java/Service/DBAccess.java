package Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBAccess {

    // ServiceとDAOで共有するDB接続
    protected Connection conn;

    // MySQLドライバ名
    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";

    // DB接続先
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pinkElephant?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Tokyo";

    // DBユーザー名
    private static final String DB_USER = "root";

    // DBパスワード
    private static final String DB_PASSWORD = "password";

    protected void access() throws SQLException {

        try {
            // JDBCドライバを読み込む
            Class.forName(DRIVER_NAME);

            // DBへ接続する
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // 手動commitにする
            conn.setAutoCommit(false);

        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBCドライバが見つかりません", e);
        }
    }

    protected void commit() {

        if (conn == null) {
            return;
        }

        try {
            // SQLの変更を確定する
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void rollback() {

        if (conn == null) {
            return;
        }

        try {
            // エラー時に変更を取り消す
            conn.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void close() {

        if (conn == null) {
            return;
        }

        try {
            // DB接続を閉じる
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}