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
            return (count == 1);
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
//<<<<<<< HEAD
//
//    public boolean insert(String uid, String upass, String email, String name) throws SQLException, ClassNotFoundException {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        
//        try {
//            Class.forName(driver);
//            conn = DriverManager.getConnection(url, "c##sahmyook", "1111");//환경에 맞게 수정
//            String sql = "INSERT INTO USERTABLE(USER_ID, PASSWORD, USER_MAIL, USER_NAME,APPROVED)"
//            		+ "VALUES(?, ?, ?, ?, ?)";
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, uid);
//            stmt.setString(2, upass);
//            stmt.setString(3, email);
//            stmt.setString(4, name);
//            stmt.setInt(5, 0); // 기본을 0으로 하여 미승인
//            
//            int count = stmt.executeUpdate();
//            return (count == 1);
//        } finally {
//            if (stmt != null) stmt.close();
//            if (conn != null) conn.close();
//        }
//    }
//    public boolean deleteUsers(String[] userIds) 
//            throws SQLException, ClassNotFoundException {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        
//        try {
//            Class.forName(driver);
//            conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
//            conn.setAutoCommit(false); // 트랜잭션 시작
//
//            // [1] IN 절을 사용한 SQL 쿼리
//            String sql = "DELETE FROM USERTABLE WHERE USER_ID IN (";
//            sql += String.join(",", Collections.nCopies(userIds.length, "?")) + ")";
//            pstmt = conn.prepareStatement(sql);
//            
//            // [2] 파라미터 바인딩
//            for (int i = 0; i < userIds.length; i++) {
//                pstmt.setString(i + 1, userIds[i]);
//            }
//            
//            // [3] 실행 및 트랜잭션 처리
//            int count = pstmt.executeUpdate();
//            if (count == userIds.length) {
//                conn.commit();
//                return true;
//            } else {
//                conn.rollback();
//                return false;
//            }
//        } finally {
//            if (pstmt != null) pstmt.close();
//            if (conn != null) conn.close();
//        }
//    }
//
//    public boolean approveUser(String uid) throws SQLException,ClassNotFoundException{
//    	Connection conn = null;
//        PreparedStatement stmt = null;
//        try {
//        	Class.forName(driver);
//        	conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
//        	String sql = "UPDATE USERTABLE SET APPROVED = 1 WHERE USER_ID = ?";
//        	stmt = conn.prepareStatement(sql);
//        	stmt.setString(1, uid);
//        	int count = stmt.executeUpdate();
//        	return (count==1);}
//        	finally {
//        		if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//        }
//    }
//    
//	public boolean rejectUser(String uid) throws SQLException, ClassNotFoundException {
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
//			String sql = "DELETE FROM USERTABLE WHERE USER_ID = ?";
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, uid);
//			int count = stmt.executeUpdate();
//			return (count == 1);
//		} finally {
//			if (stmt != null)
//				stmt.close();
//			if (conn != null)
//				conn.close();
//		}
//	}
//
//=======
//>>>>>>> branch 'lnh' of https://github.com/Project-ABase/Compair.git
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
	       return -2; // 데이터베이스 오류
	   }
	
	
	
	
	
	//수정
//	public int login(String uid, String upass) throws SQLException, ClassNotFoundException {
//	    String SQL = "SELECT password, APPROVED FROM userTable WHERE user_id = ?";
//	    Connection conn = null;
//	    PreparedStatement pstmt = null;
//	    ResultSet rs = null;
//
//	    try {
//	        pstmt = conn.prepareStatement(SQL);
//	        pstmt.setString(1, uid);
//	        rs = pstmt.executeQuery();
//
//	        if (rs.next()) {
//	            String dbPassword = rs.getString("password");
//	            int isApproved = rs.getInt("APPROVED"); // 승인 여부 체크
//
//	            if (!dbPassword.equals(upass)) {
//	                return 0; // 비밀번호 불일치
//	            } else if (isApproved == 0) {
//	                return 2; // 승인 대기 중
//	            } else {
//	                return 1; // 로그인 성공 (승인 완료)
//	            }
//	        }
//	        return -1; // 아이디 없음
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return -2; // DB 오류
//	    } finally {
//	        // 리소스 정리
//	    }
//	    
//	}
	// 관리자 여부 확인
	public boolean isAdmin(String uid) {
	    return "admin".equals(uid); // admin ID만 관리자로 간주
	}
	//수정 1
	//	public List<Map<String, Object>> getPendingUsers() throws SQLException, ClassNotFoundException {
//	    List<Map<String, Object>> list = new ArrayList<>();
//	    Connection conn = null;
//	    PreparedStatement pstmt = null;
//	    ResultSet rs = null;
//	    
//	    try {
//	       conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
//	       String sql = "SELECT USER_ID, USER_MAIL, USER_NAME, APPROVED FROM USERTABLE WHERE APPROVED = 0";
//	       pstmt = conn.prepareStatement(sql);
//	       rs = pstmt.executeQuery();
//	       
//	       while (rs.next()) {
//	    	   Map<String,Object> user = new HashMap<>();
//	    	   user.put("user_id", rs.getString("USER_ID"));
//	    	   user.put("user_mail", rs.getString("USER_MAIL"));
//	    	   user.put("user_name", rs.getString("USER_NAME"));
//	    	   user.put("approved", rs.getInt("APPROVED"));
//	    	   list.add(user);
//	       }
//	    } finally {
//	        // 리소스 정리
//	    }
//	    return list;
//	}
	
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
	    } finally {
	        if (pstmt != null) pstmt.close();
	        if (conn != null) conn.close();
	    }

	    return isUpdated;
	}
	public boolean deleteUser(String userId) throws SQLException {
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    boolean isDeleted = false;

	    try {
	        conn = conpool.get();
	        String sql = "DELETE FROM userTable WHERE user_id = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userId);

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            isDeleted = true;
	        }
	    } finally {
	        if (pstmt != null) pstmt.close();
	        if (conn != null) conn.close();
	    }
	    return isDeleted;
	}
//수정 2
//	public List<Map<String, Object>> getAllUsers() throws SQLException, ClassNotFoundException {
//		List<Map<String, Object>> list = new ArrayList<>();
//		Connection conn = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//
//		try {
//			conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
//			String sql = "SELECT USER_ID, USER_MAIL, USER_NAME, APPROVED FROM USERTABLE";
//			pstmt = conn.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				Map<String, Object> user = new HashMap<>();
//				user.put("user_id", rs.getString("USER_ID"));
//				user.put("user_mail", rs.getString("USER_MAIL"));
//				user.put("user_name", rs.getString("USER_NAME"));
//				user.put("APPROVED", rs.getInt("APPROVED"));
//				list.add(user);
//			}
//		} finally {
//			// 리소스 정리
//		}
//		return list;
//	}
}
