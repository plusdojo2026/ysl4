package Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBAccess {

    protected Connection conn;

    private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/task_manager?characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";

    protected void access() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);
        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        // Service単位でcommitするため自動commitを止める
        conn.setAutoCommit(false);
    }

    protected void commit() {
        try {
            if (conn != null) {
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void rollback() {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}