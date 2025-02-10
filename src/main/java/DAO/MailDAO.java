package DAO;

import java.sql.*;
import util.conpool;
import java.util.*;
import user.Mail;

public class MailDAO {
	// 메일 저장
	public boolean insertMail(Mail mail) {
		String mailSQL = "INSERT INTO mail (server_code, writer, mail_title, receiver) VALUES (?, ?, ?, ?)";
		//mail DB에 한 번에 저장
		String contentSQL = "INSERT INTO mailContent (mail_code, server_code, mail_title, todo_content, attachment) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = conpool.get();
				PreparedStatement pstmt1 = conn.prepareStatement(mailSQL, new String[]{"mail_code"});
				PreparedStatement pstmt2 = conn.prepareStatement(contentSQL)) {
			conn.setAutoCommit(false);
			int mailCode = 0;
			pstmt1.setInt(1, mail.getServer_code());
			pstmt1.setString(2, mail.getWriter());
			pstmt1.setString(3, mail.getMail_title());
			pstmt1.setString(4, mail.getReceiver());
			pstmt1.executeUpdate();
			try (ResultSet generatedKeys = pstmt1.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					mailCode = generatedKeys.getInt(1);
				} else {
					throw new SQLException("메일코드 생성 실패");
				}
			}
			pstmt2.setInt(1, mailCode);
			pstmt2.setInt(2, mail.getServer_code());
			pstmt2.setString(3, mail.getMail_title());
			pstmt2.setString(4, mail.getTodoContent());
			pstmt2.setString(5, mail.getAttachment());
			pstmt2.executeUpdate();
			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 같은 서버 내 나에게 온 모든 메일 조회

public List<Mail> getMailList(String userID, String receiver, int serverCode) throws SQLException {
    List<Mail> mailList = new ArrayList<>();
    String sql = "SELECT m.*, mc.todo_content, mc.attachment "
               + "FROM mail m "
               + "LEFT JOIN mailContent mc "
               + "    ON m.mail_code = mc.mail_code "
               + "    AND m.server_code = mc.server_code "
               + "LEFT JOIN mail_deletion md "
               + "    ON m.mail_code = md.mail_code "
               + "    AND m.server_code = md.server_code "
               + "    AND md.user_id = ? "  // 1번 파라미터 (userID)
               + "WHERE m.receiver = ? "   // 2번 파라미터 (receiver)
               + "    AND m.server_code = ? " // 3번 파라미터 (serverCode)
               + "    AND md.mail_code IS NULL "
               + "ORDER BY m.post_date DESC";

    try (Connection conn = conpool.get();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // 파라미터 바인딩 (순서 주의!)
        pstmt.setString(1, userID);       // md.user_id
        pstmt.setString(2, receiver);     // m.receiver
        pstmt.setInt(3, serverCode);      // m.server_code

        System.out.println("Executing query with parameters: userID=" + userID + ", receiver=" + receiver + ", serverCode=" + serverCode);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Mail mail = new Mail();
                mail.setMail_code(rs.getInt("mail_code"));
                mail.setServer_code(rs.getInt("server_code"));
                mail.setWriter(rs.getString("writer"));
                mail.setReceiver(rs.getString("receiver"));
                mail.setPost_date(rs.getTimestamp("post_date"));
                mail.setMail_title(rs.getString("mail_title"));
                mail.setTodoContent(rs.getString("todo_content"));
                mail.setAttachment(rs.getString("attachment"));

                System.out.println("Retrieved mail - mail_code: " + mail.getMail_code() + ", server_code: " + mail.getServer_code());

                mailList.add(mail);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println("Number of retrieved mails: " + mailList.size());
    return mailList;
}

//보낸 메일
public List<Mail> getSentMailList(String writer, int serverCode) throws SQLException {
    List<Mail> mailList = new ArrayList<>();
    String sql = "SELECT m.*, mc.todo_content, mc.attachment "
               + "FROM mail m "
               + "LEFT JOIN mailContent mc "
               + "    ON m.mail_code = mc.mail_code "
               + "    AND m.server_code = mc.server_code "
               + "LEFT JOIN mail_deletion md "
               + "    ON m.mail_code = md.mail_code "
               + "    AND m.server_code = md.server_code "
               + "    AND md.user_id = ? "  // 1번 파라미터 (writer)
               + "WHERE m.writer = ? "     // 2번 파라미터 (writer)
               + "    AND m.server_code = ? " // 3번 파라미터 (serverCode)
               + "    AND md.mail_code IS NULL "
               + "ORDER BY m.post_date DESC";

    try (Connection conn = conpool.get();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        // 파라미터 바인딩 (순서 주의!)
        pstmt.setString(1, writer);       // md.user_id
        pstmt.setString(2, writer);       // m.writer
        pstmt.setInt(3, serverCode);      // m.server_code

        System.out.println("Executing query with parameters: writer=" + writer + ", serverCode=" + serverCode);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Mail mail = new Mail();
                mail.setMail_code(rs.getInt("mail_code"));
                mail.setServer_code(rs.getInt("server_code"));
                mail.setWriter(rs.getString("writer"));
                mail.setReceiver(rs.getString("receiver"));
                mail.setPost_date(rs.getTimestamp("post_date"));
                mail.setMail_title(rs.getString("mail_title"));
                mail.setTodoContent(rs.getString("todo_content"));
                mail.setAttachment(rs.getString("attachment"));

                System.out.println("Retrieved mail - mail_code: " + mail.getMail_code() + ", server_code: " + mail.getServer_code());

                mailList.add(mail);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    System.out.println("Number of retrieved mails: " + mailList.size());
    return mailList;
}


	

	// 특정 메일 조회 -> 메일검색
	public Mail getMail(int mailCode, int serverCode) throws SQLException {
		String sql = "SELECT m.*, mc.todo_content, mc.attachment " + "FROM mail m LEFT JOIN mailContent mc "
				+ "ON m.mail_code = mc.mail_code AND m.server_code = mc.server_code "
				+ "WHERE m.mail_code = ? AND m.server_code = ?";
		try (Connection conn = conpool.get(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, mailCode);
			pstmt.setInt(2, serverCode);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Mail mail = new Mail();
					mail.setMail_code(rs.getInt("mail_code"));
					mail.setServer_code(rs.getInt("server_code"));
					mail.setWriter(rs.getString("writer"));
					mail.setReceiver(rs.getString("receiver"));
					mail.setPost_date(rs.getTimestamp("post_date"));
					mail.setMail_title(rs.getString("mail_title"));
					mail.setTodoContent(rs.getString("todo_content"));
					mail.setAttachment(rs.getString("attachment"));
					return mail;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// 체크박스로 선택한 메일 삭제
	public boolean deleteMails(String userId, int[] mailCodes, int[] serverCodes) {
	    String sql = "INSERT INTO mail_deletion (mail_code, server_code, user_id) VALUES (?, ?, ?)";
	    
	    try (Connection conn = conpool.get();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        
	        conn.setAutoCommit(false);
	        
	        for(int i=0; i<mailCodes.length; i++){
	            pstmt.setInt(1, mailCodes[i]);
	            pstmt.setInt(2, serverCodes[i]);
	            pstmt.setString(3, userId);
	            pstmt.addBatch();
	        }
	        
	        pstmt.executeBatch();
	        conn.commit();
	        return true;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
}