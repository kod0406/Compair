package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class FeedDAO {
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	public boolean insert(String jsonstr) throws NamingException, SQLException, ParseException, ClassNotFoundException {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            synchronized(this) {
            	System.out.println();
            	System.out.println("FeedDAO 첫번째 디버깅");
        		Class.forName(driver);
        		conn=DriverManager.getConnection(url, "C##TEST3", "1111");
                // phase 1. add "no" property -----------------------------
                String str = jsonstr;
                JSONObject obj = (JSONObject)(new JSONParser()).parse(jsonstr);
                String TITLE = obj.get("id").toString();
                String CONTENT = obj.get("content").toString();
                String IMAGE = null; 
                if(obj.get("images") != null) IMAGE = obj.get("images").toString();
                //테스트 코드
                int SERVER_CODE = 1;
                String AUTHOR = "류재열";
                String ATTACHMENT = IMAGE;
            	System.out.println(TITLE);
            	System.out.println(CONTENT);
            	System.out.println(IMAGE);
                // phase 3. insert jsonobj to the table ------------------------
                
                String sql = "INSERT INTO boardTable(SERVER_CODE, TITLE, AUTHOR, CONTENT, ATTACHMENT) VALUES(?, ?, ?, ?, ?)";
                stmt = conn.prepareStatement(sql);
                System.out.println("여기까지는 됨");
                stmt.setInt(1, SERVER_CODE);
                stmt.setString(2, TITLE);
                stmt.setString(3, AUTHOR);
                stmt.setString(4, CONTENT);
                stmt.setString(5, ATTACHMENT);
                int count = stmt.executeUpdate();
                System.out.println("count는 ?  " + count);
                return (count == 1) ? true : false;
            }
        }
        finally {
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
	public ArrayList<FeedObj> getList() throws NamingException, SQLException, ClassNotFoundException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM BOARDTABLE ORDER BY BOARD_CODE DESC";
    		Class.forName(driver);
    		conn=DriverManager.getConnection(url, "C##TEST3", "1111");
    		stmt = conn.prepareStatement(sql);
    		rs = stmt.executeQuery();

    		
    		ArrayList<FeedObj> feeds = new ArrayList<FeedObj>();
    		while(rs.next()) {
    			feeds.add(new FeedObj(rs.getString("TITLE"), rs.getString("AUTHOR"), rs.getString("CONTENT"), rs.getString("ATTACHMENT")));
    		}
    		return feeds;
    		
		} finally {
	            if (stmt != null) {
					stmt.close();
				}
	            if (conn != null) {
					conn.close();
				}
	            if(rs != null) rs.close();
	        
		}
	}
	
}
