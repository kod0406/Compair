package DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

public class UserDAO {
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:orcl";
	public boolean isIdExist(String uid) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
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
            Class.forName(driver);
            conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
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

    // 기존 insert 메서드 유지
    public boolean insert(String uid, String upass, String email, String name) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
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

	public String[] getUserInfoByEmail(String email) throws NamingException, SQLException, ClassNotFoundException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String[] result = new String[2];
		try {
			//입력값 확인
			System.out.println("입력된 이메일(email): " + email);
			Class.forName(driver);
			conn = DriverManager.getConnection(url, "c##sahmyook", "1111");
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
	        Class.forName(driver);
	        conn = DriverManager.getConnection(url, "c##sahmyook", "1111");

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

}
