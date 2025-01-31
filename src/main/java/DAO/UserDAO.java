package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.NamingException;

public class UserDAO {
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	public boolean insert(String uid, String upass) throws NamingException, SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        System.out.println("DAO " + uid);
        System.out.println("DAO " + upass);
        try {
    		Class.forName(driver);
    		conn=DriverManager.getConnection(url, "C##TEST2", "1111");
            String sql = "INSERT INTO USERTABLE(USER_ID, PASSWORD) VALUES(?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, upass);

            int count = stmt.executeUpdate();
            return (count == 1) ? true : false;

        } finally {
            if (stmt != null) {
				stmt.close();
			}
            if (conn != null) {
				conn.close();
			}
        }
    }
}
