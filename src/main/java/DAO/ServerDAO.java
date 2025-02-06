package DAO;

import java.sql.*;

import util.conpool;

public class ServerDAO {
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
}
