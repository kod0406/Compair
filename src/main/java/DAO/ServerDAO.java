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

public class ServerDAO {
	public String checkServer(String jsonstr) throws NamingException, SQLException, ParseException, ClassNotFoundException {
		Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
		try {
            synchronized(this) {
            	conn = conpool.get();
            	String sql = "SELECT SERVER_CODE FROM SERVER_TABLE WHERE USER_ID = ? ORDER BY SERVER_CODE";
            	stmt = conn.prepareStatement(sql);
            	stmt.setString(1, jsonstr);
            	rs = stmt.executeQuery(); 
            	String str = "";
            	while(rs.next()) {
            		str += rs.getString("SERVER_CODE");
            		str += " ";
            	}
            	
            	return str;
            }
		}finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
	}
	// 사용자 서버 번호 조회 메서드 ->이거는 ServerDAO에 있어야 됨
	public int getUserServerCode(String userID) throws SQLException {
	    String sql = "SELECT server_code FROM SERVERTABLE WHERE user_id = ?";
	    try (Connection conn = conpool.get(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, userID);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("server_code");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // 서버 코드가 없으면 -1 반환
	}
	public boolean isUserInServer(String uid, int serverCode) throws SQLException {
	       Connection conn = null;
	       PreparedStatement stmt = null;
	       ResultSet rs = null;

	       try {
	           conn = conpool.get();
	           String sql = "SELECT COUNT(*) FROM SERVER_TABLE WHERE USER_ID = ? AND SERVER_CODE = ?";
	           stmt = conn.prepareStatement(sql);
	           stmt.setString(1, uid);
	           stmt.setInt(2, serverCode);
	           rs = stmt.executeQuery();

	           if (rs.next()) {
	               return rs.getInt(1) > 0; // 서버에 사용자가 있으면 true
	           }
	           return false;
	       } finally {
	           if (rs != null) rs.close();
	           if (stmt != null) stmt.close();
	           if (conn != null) conn.close();
	       }
	   }
}
