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
	public boolean insert(String jsonstr) throws NamingException, SQLException, ParseException, ClassNotFoundException {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            synchronized(this) {
            	System.out.println();
            	System.out.println("FeedDAO 첫번째 디버깅");
            	conn = conpool.get();
            	// phase 1. add "no" property -----------------------------
                String str = jsonstr;
                JSONObject obj = (JSONObject)(new JSONParser()).parse(jsonstr);
                String TITLE = obj.get("id").toString();
                String CONTENT = obj.get("content").toString();
                String IMAGE = null; 
                if(obj.get("images") != null) IMAGE = obj.get("images").toString();
                //테스트 코드
                int SERVER_CODE = 2;
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
	public String getGroup(String maxNo) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	String sql = "SELECT * FROM BOARDTABLE";

        	if (maxNo != null) {
        	    sql += " WHERE BOARD_CODE < " + maxNo;
        	}

        	sql += " ORDER BY BOARD_CODE DESC FETCH FIRST 3 ROWS ONLY";

            conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
                
            String str = "[";
            int cnt = 0;
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
            
            String str = "[";
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
