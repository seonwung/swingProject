package packages.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/자신의 DB 이름 넣기?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "비밀번호 입력하세요";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
