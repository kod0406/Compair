package DAO;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import util.conpool;

public class UserDAO {
	public boolean insert(String uid, String upass, String email, String name) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = conpool.get();
            String sql = "INSERT INTO USERTABLE(USER_ID, PASSWORD, USER_MAIL, USER_NAME) VALUES(?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            stmt.setString(2, upass);
            stmt.setString(3, email);
            stmt.setString(4, name);
            
            int count = stmt.executeUpdate();
            
            if(count == 1) {
            	count = serverInsert(uid, 0);
            }
            
            return (count == 1);
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
	
	public int serverInsert(String uid, int scode) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = conpool.get();
            String sql = "INSERT INTO SERVER_TABLE(USER_ID, SERVER_CODE) VALUES(?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            //수정 바람
            stmt.setInt(2, scode);
            
            int count = stmt.executeUpdate();
            
            if(count == 1) return 1;
            else return 0;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
	}
	
	public boolean isIdExist(String uid) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = conpool.get();
            String sql = "SELECT COUNT(*) FROM USERTABLE WHERE USER_ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uid);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // 중복된 ID가 있으면 true
            }
            return false;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
	
	public boolean isEmailExist(String email) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
        	conn = conpool.get();
            String sql = "SELECT COUNT(*) FROM USERTABLE WHERE USER_MAIL = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0; // 중복된 이메일이 있으면 true
            }
            return false;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
	
	public String[] getUserInfoByEmail(String email) throws NamingException, SQLException, ClassNotFoundException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String[] result = new String[2];
		try {
			//입력값 확인
			System.out.println("입력된 이메일(email): " + email);
			conn = conpool.get();
			String sql = "SELECT user_id, password FROM userTable WHERE user_mail = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("DB에 등록된 아이디(user_id): " + rs.getString("user_id"));
				result[0] = rs.getString("user_id");
				System.out.println("DB에 등록된 비밀번호(password): " + rs.getString("password"));
				result[1] = rs.getString("password");
			}
		} finally {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		}
		return result;
	}
	
	   public int login(String uid, String upass) throws NamingException, SQLException, ClassNotFoundException {
	       String SQL = "SELECT password FROM userTable WHERE user_id = ?";
	       Connection conn = null;
	       PreparedStatement pstmt = null;
	       ResultSet rs = null;

	       try {
	           conn = conpool.get();

	           // 입력값 확인
	           System.out.println("입력된 아이디(uid): " + uid);
	           System.out.println("입력된 비밀번호(upass): " + upass);

	           pstmt = conn.prepareStatement(SQL);
	           pstmt.setString(1, uid);
	           rs = pstmt.executeQuery();

	           if (rs.next()) {
	               String dbPassword = rs.getString("password");

	               // DB에서 가져온 값 확인
	               System.out.println("DB에 등록된 비밀번호(dbPassword): " + dbPassword);

	               if (dbPassword.equals(upass)) {
	                   return 1; // 로그인 성공
	               } else {
	                   return 0; // 비밀번호 불일치
	               }
	           }
	           return -1; // 아이디 없음
	       } catch (Exception e) {
	           e.printStackTrace(); // 오류 로그
	       } finally {
	           if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
	           if (pstmt != null) try { pstmt.close(); } catch (SQLException ignore) {}
	           if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	       }
		return 0;
	   }

	   
	public boolean isAdmin(String uid) {
	    return "admin".equals(uid); // admin ID만 관리자로 간주
	}
	
	private void executeUpdate(Connection conn, String sql, String userId) throws SQLException {
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setString(1, userId);
	        pstmt.executeUpdate();
	    }
	}
	
	public String[] getUserInfoById(String uid) throws NamingException, SQLException, ClassNotFoundException {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String[] result = new String[4];  
	    try {
	        conn = conpool.get();
	        String sql = "SELECT user_id, user_name, user_mail, password FROM userTable WHERE user_id = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, uid);
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            result[0] = rs.getString("user_id");
	            result[1] = rs.getString("user_name");
	            result[2] = rs.getString("user_mail");
	            result[3] = rs.getString("password");
	        }
	    } finally {
	        if (rs != null) rs.close();
	        if (pstmt != null) pstmt.close();
	        if (conn != null) conn.close();
	    }
	    return result;
	}
	public boolean updateUserInfo(String newUserId, String newUserName, String newUserMail, String newUserPass) throws NamingException, SQLException, ClassNotFoundException {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    boolean isUpdated = false;

	    try {
	        conn = conpool.get();
	        conn.setAutoCommit(false); // 트랜잭션 시작
	        String sql = "UPDATE userTable SET user_name = ?, user_mail = ?, password = ? WHERE user_id = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, newUserName);
	        pstmt.setString(2, newUserMail);
	        pstmt.setString(3, newUserPass);
	        pstmt.setString(4, newUserId);

	        int rowsAffected = pstmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            isUpdated = true;
	        }
	        
	        if(isUpdated) {
	        	pstmt.close();
	        	String sql2 = "UPDATE boardTable SET author = ? WHERE server_code IN " + 
                        "(SELECT server_code FROM serverTable WHERE user_id = ?)";
	        	pstmt= conn.prepareStatement(sql2);
	        	pstmt.setString(1, newUserName);
	        	pstmt.setString(2, newUserId); 
	        	
	        	int boardRowsAffected = pstmt.executeUpdate();
	            System.out.println("boardTable 업데이트된 행: " + boardRowsAffected);
	        	
	        }
	        conn.commit();
	        
	    } catch(SQLException e) {
	    	if(conn != null) conn.rollback(); // 오류 발생 시 롤백
	    } finally {
	        if (pstmt != null) pstmt.close();
	        if (conn != null) {
	        	conn.setAutoCommit(true); //원래 상태로 복구
	        	conn.close();
	        }
	    }

	    return isUpdated;
	}
	
	public boolean deleteUser(String userId) throws SQLException {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    boolean isDeleted = false;

	    try {
	        conn = conpool.get();
	        conn.setAutoCommit(false); // 트랜잭션 시작	
	        
	        executeUpdate(conn, "DELETE FROM SERVER_TABLE WHERE USER_ID = ?", userId);
	        
	        String sql = "DELETE FROM userTable WHERE user_id = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userId);

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            isDeleted = true;
	        }
	        
	        conn.commit();
	    } catch(SQLException e) {
	    	if (conn != null) conn.rollback(); // 오류 발생 시 롤백
	    	throw e;
	    } finally {
	        if (pstmt != null) pstmt.close();
	        if (conn != null) {
	        	conn.setAutoCommit(true); // 원래 상태로 복구
	        	conn.close();
	        }
	    }
	    return isDeleted;
	}
}
