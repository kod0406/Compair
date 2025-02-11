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


public boolean insertServer(String userId, int serverCode) throws SQLException {
    Connection conn = null;
    PreparedStatement stmtCheck = null;
    PreparedStatement stmtInsertServer = null;
    PreparedStatement stmtInsertServerTable = null;
    ResultSet rs = null;

    try {
        conn = conpool.get();
        conn.setAutoCommit(false);

        // Check if the user is already in the server
        String sqlCheckUserInServer = "SELECT COUNT(*) FROM SERVER_TABLE WHERE USER_ID = ? AND SERVER_CODE = ?";
        stmtCheck = conn.prepareStatement(sqlCheckUserInServer);
        stmtCheck.setString(1, userId);
        stmtCheck.setInt(2, serverCode);
        rs = stmtCheck.executeQuery();

        if (rs.next() && rs.getInt(1) > 0) {
            // User is already in the server, no need to insert again
            return false;
        }

        // Check if the server code exists in SERVERTABLE
        String sqlCheck = "SELECT COUNT(*) FROM SERVERTABLE WHERE SERVER_CODE = ?";
        stmtCheck = conn.prepareStatement(sqlCheck);
        stmtCheck.setInt(1, serverCode);
        rs = stmtCheck.executeQuery();

        if (rs.next() && rs.getInt(1) == 0) {
            // Insert the server code into SERVERTABLE if it does not exist
            String sqlInsertServer = "INSERT INTO SERVERTABLE (SERVER_CODE, USER_ID, SEVER_NAME) VALUES (?, ?, ?)";
            stmtInsertServer = conn.prepareStatement(sqlInsertServer);
            stmtInsertServer.setInt(1, serverCode);
            stmtInsertServer.setString(2, userId);
            stmtInsertServer.setString(3, "Default Server Name"); // Change as needed
            stmtInsertServer.executeUpdate();
        }

        // Insert into SERVER_TABLE
        String sqlInsertServerTable = "INSERT INTO SERVER_TABLE (USER_ID, SERVER_CODE) VALUES (?, ?)";
        stmtInsertServerTable = conn.prepareStatement(sqlInsertServerTable);
        stmtInsertServerTable.setString(1, userId);
        stmtInsertServerTable.setInt(2, serverCode);
        stmtInsertServerTable.executeUpdate();

        conn.commit();
        return true;

    } catch (SQLException e) {
        if (conn != null) conn.rollback();
        throw e;
    } finally {
        if (rs != null) rs.close();
        if (stmtCheck != null) stmtCheck.close();
        if (stmtInsertServer != null) stmtInsertServer.close();
        if (stmtInsertServerTable != null) stmtInsertServerTable.close();
        if (conn != null) conn.close();
    }
}
//서버 생성

	public boolean leaveServer(String uid, int serverCode) throws SQLException {
	    String sql = "DELETE FROM SERVER_TABLE WHERE USER_ID = ? AND SERVER_CODE = ?";
	    try (Connection conn = conpool.get(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, uid);
	        pstmt.setInt(2, serverCode);
	        int rowsAffected = pstmt.executeUpdate();
	        return rowsAffected > 0;
	    }
	}
	
	public String getUsersInServer(int serverCode) throws NamingException, SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	String sql = "SELECT USER_ID FROM SERVERTABLE where server_code = ?";
        	conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(0, serverCode);
            rs = stmt.executeQuery();
                
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("USER_ID", rs.getString("USER_ID"));
                jsonArray.add(obj);
            }
            return jsonArray.toString();
                
        } finally {
            if (rs != null) rs.close(); 
            if (stmt != null) stmt.close(); 
            if (conn != null) conn.close();
        }
    }
	
}
