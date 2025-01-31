package util;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class conpool {
	private static DataSource dataSource;

    static {
        try {
            Context initContext = new InitialContext();
            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/MyDB");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database Connection Pool 초기화 실패!");
        }
    }

    // Connection 객체 반환
    public static Connection get() throws SQLException {
        return dataSource.getConnection();
    }
}
