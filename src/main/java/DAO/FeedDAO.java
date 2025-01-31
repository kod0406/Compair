package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.json.simple.parser.ParseException;

public class FeedDAO {
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	public boolean insert(String uid, String ucon, String ufile) throws NamingException, SQLException, ParseException, ClassNotFoundException {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            synchronized(this) {
        		Class.forName(driver);
        		conn=DriverManager.getConnection(url, "C##TEST2", "1111");
                // phase 1. add "no" property -----------------------------
                String sql = "SELECT no FROM feed ORDER BY no DESC LIMIT 1";

                stmt = conn.prepareStatement(sql);
                rs = stmt.executeQuery();

                int max = (!rs.next()) ? 0 : rs.getInt("BOARD_CODE");


                stmt.close(); rs.close();

                // phase 2. add "user" property ------------------------------
                String DAOuid = uid;
                String DAOucon = ucon;
                String DAOufile = ufile;

                sql = "SELECT jsonstr FROM user WHERE id = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, uid);
                rs = stmt.executeQuery();
                /*
                if (rs.next()) {
                    String usrstr = rs.getString("jsonstr");
                    JSONObject usrobj = (JSONObject) parser.parse(usrstr);
                    usrobj.remove("password");
                    usrobj.remove("ts");
                    jsonobj.put("user", usrobj);
                }
                stmt.close(); rs.close();
                */

                // phase 3. insert jsonobj to the table ------------------------
                sql = "INSERT INTO feed(SERVER_CODE, BOARD_CODE, TTTLE, AUTHOR, CONTENT, ATTACHMENT) VALUES(?, ?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, max + 1);
                stmt.setInt(2, max + 1);
                stmt.setString(3, "test");
                stmt.setString(4, DAOuid);
                stmt.setString(5, DAOucon);
                stmt.setString(6, DAOufile);

                int count = stmt.executeUpdate();
                return (count == 1) ? true : false;
            }
        } finally {
            if (rs != null) {
				rs.close();
			}
            if (stmt != null) {
				stmt.close();
			}
            if (conn != null) {
				conn.close();
			}
        }
    }
}
