package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.conpool;

public class FeedDAO {
	public boolean insert(String jsonstr, String writer, String server) throws NamingException, SQLException, ParseException, ClassNotFoundException {
		Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        try {
            synchronized(this) {
            	System.out.println();
            	System.out.println("FeedDAO 첫번째 디버깅");
            	conn = conpool.get();
            	JSONObject obj = (JSONObject)(new JSONParser()).parse(jsonstr);
                String TITLE = obj.get("id").toString();
                String CONTENT = obj.get("content").toString();
                String IMAGE = null; 
                if(obj.get("images") != null) IMAGE = obj.get("images").toString();
                //테스트 코드
                int SERVER_CODE = Integer.parseInt(server);
                String AUTHOR = null;
                String ATTACHMENT = IMAGE;
                
                String query = "SELECT user_name FROM userTable WHERE user_id = ?";
                stmt2 = conn.prepareStatement(query);
                stmt2.setString(1, writer);
                rs = stmt2.executeQuery();
                
                if (rs.next()) {
                    AUTHOR = rs.getString("user_name");  // user_name을 가져옴
                } else {
                    System.out.println("해당 user_id의 user_name을 찾을 수 없음: " + writer);
                    return false;  // user_name이 없으면 삽입 중단
                }
                
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
    public String getList() throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT jsonstr FROM feed ORDER BY BOARD_CODE DESC";

            conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
                
            String str = "[";
            int cnt = 0;
            while(rs.next()) {
                if (cnt++ > 0) str += ", ";
                str += rs.getString("jsonstr");
            }
            return str + "]";
                
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
    
	public String getGroup(String maxNo, String sc, String good) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	if(good == null) good = "3";
        	String sql = "SELECT * FROM BOARDTABLE";
        	//변수로 변경
        	if (maxNo != null) {
        	    sql += " WHERE BOARD_CODE < " + maxNo + " AND SERVER_CODE = " + sc;
        	}
        	else if(maxNo == null) {
        		sql += " WHERE SERVER_CODE = " + sc;
        	}
        	sql += " ORDER BY BOARD_CODE DESC FETCH FIRST " + good + "ROWS ONLY";

            conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
                
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("SERVER_CODE", rs.getString("SERVER_CODE"));
                obj.put("BOARD_CODE", rs.getString("BOARD_CODE"));
                obj.put("TITLE", rs.getString("TITLE"));
                obj.put("AUTHOR", rs.getString("AUTHOR"));
                obj.put("POSTDATE", rs.getString("POST_DATE"));
                obj.put("CONTENT", rs.getString("CONTENT"));
                obj.put("ATTACHMENT", rs.getString("ATTACHMENT"));
                jsonArray.add(obj);
            }
            return jsonArray.toString();
                
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
	
	public String getOneFeed(String boardCode) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	String sql = "SELECT * FROM BOARDTABLE WHERE BOARD_CODE = " + boardCode;
        	
        	conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("SERVER_CODE", rs.getString("SERVER_CODE"));
                obj.put("BOARD_CODE", rs.getString("BOARD_CODE"));
                obj.put("TITLE", rs.getString("TITLE"));
                obj.put("AUTHOR", rs.getString("AUTHOR"));
                obj.put("POSTDATE", rs.getString("POST_DATE"));
                obj.put("CONTENT", rs.getString("CONTENT"));
                obj.put("ATTACHMENT", rs.getString("ATTACHMENT"));
                jsonArray.add(obj);
            }
            System.out.println(jsonArray.toString());
            return jsonArray.toString();
	
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
	}
	
	public String calendarGetGroup(String POST_DATE, String serverSession) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	//나중에 TODO로 바꿔야함
        	String sql = "SELECT * FROM BOARDTABLE WHERE TO_CHAR(POST_DATE, 'YYYY-MM-DD') = '" + POST_DATE + "' AND SERVER_CODE = " + serverSession + " ORDER BY BOARD_CODE DESC";
        	System.out.println(sql);
        	conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("SERVER_CODE", rs.getString("SERVER_CODE"));
                obj.put("BOARD_CODE", rs.getString("BOARD_CODE"));
                obj.put("TITLE", rs.getString("TITLE"));
                obj.put("AUTHOR", rs.getString("AUTHOR"));
                obj.put("POSTDATE", rs.getString("POST_DATE"));
                obj.put("CONTENT", rs.getString("CONTENT"));
                obj.put("ATTACHMENT", rs.getString("ATTACHMENT"));
                jsonArray.add(obj);
            }
            System.out.println(jsonArray.toString());
            return jsonArray.toString();
	
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
	}
}
