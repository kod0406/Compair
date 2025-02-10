package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import user.Todo;
import util.conpool;

public class TodoDAO {

	public Map<String, Object> updateTodo(int todoCode, int serverCode, String newTitle, String newTag, String userId)
			throws SQLException {
		Connection conn = null;
		PreparedStatement stmtList = null;
		ResultSet rs = null;
		Map<String, Object> resultMap = new HashMap<>();
		try {
			conn = conpool.get();
			conn.setAutoCommit(false);

			// 1. todoList 테이블 업데이트
			String sqlList = "UPDATE TODOLIST SET TODO_TITLE = ?, TAG = ? "
					+ "WHERE TODO_CODE = ? AND SERVER_CODE = ? AND INSTR(TAG, ?) > 0";
			stmtList = conn.prepareStatement(sqlList);
			stmtList.setString(1, newTitle);
			stmtList.setString(2, newTag); // 새로운 태그로 업데이트
			stmtList.setInt(3, todoCode);
			stmtList.setInt(4, serverCode);
			stmtList.setString(5, userId);

			int listCount = stmtList.executeUpdate();

			boolean success = (listCount > 0);
			if (success) {
				conn.commit();
				resultMap.put("success", true);
				return resultMap;
			} else {
				conn.rollback();
				resultMap.put("success", false);
				return resultMap;
			}

		} catch (SQLException e) {
			if (conn != null)
				conn.rollback();
			throw e;
		} finally {
			if (rs != null)
				rs.close();
			if (stmtList != null)
				stmtList.close();
			if (conn != null)
				conn.close();
		}
	}

	public boolean UntagTodos(String currentUserId, int[] todoCodes, int[] serverCodes) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			// SQL: 정규 표현식을 사용해 태그와 불필요한 쉼표 제거
			String sql = "UPDATE TODOLIST " + "SET TAG = REGEXP_REPLACE(TAG, '(^|,)(' || ? || ')(,|$)', '\\1') "
					+ "WHERE TODO_CODE = ? AND SERVER_CODE = ? AND INSTR(TAG, ?) > 0";

			conn = conpool.get();
			stmt = conn.prepareStatement(sql);

			for (int i = 0; i < todoCodes.length; i++) {
				stmt.setString(1, currentUserId); // 제거할 사용자 ID
				stmt.setInt(2, todoCodes[i]); // Todo 코드
				stmt.setInt(3, serverCodes[i]); // 서버 코드
				stmt.setString(4, currentUserId); // 권한 확인용
				stmt.addBatch(); // Batch로 실행 준비
			}

			int[] results = stmt.executeBatch(); // Batch 실행
			for (int result : results) {
				if (result == 0)
					return false; // 하나라도 실패하면 false 반환
			}
			return true; // 모두 성공하면 true 반환
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
	}

	public boolean deleteTodo(int todoCode, int serverCode, String currentUserId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmtCheck = null;
		PreparedStatement stmtList = null;
		try {
			conn = conpool.get();
			conn.setAutoCommit(false);

			// 1. Check if the current user is the author
			String sqlCheck = "SELECT TODO_WRITER FROM TODOLIST WHERE TODO_CODE = ? AND SERVER_CODE = ?";
			stmtCheck = conn.prepareStatement(sqlCheck);
			stmtCheck.setInt(1, todoCode);
			stmtCheck.setInt(2, serverCode);
			ResultSet rs = stmtCheck.executeQuery();
			if (rs.next()) {
				String writer = rs.getString("TODO_WRITER");
				if (!writer.equals(currentUserId)) {
					conn.rollback();
					return false; 
				}
			} else {
				conn.rollback();
				return false; 
			}

			
			String sqlList = "DELETE FROM TODOLIST WHERE TODO_CODE = ? AND SERVER_CODE = ?";
			stmtList = conn.prepareStatement(sqlList);
			stmtList.setInt(1, todoCode);
			stmtList.setInt(2, serverCode);
			int listResult = stmtList.executeUpdate();

			
			conn.commit();

			return listResult > 0;

		} catch (SQLException e) {
			if (conn != null)
				conn.rollback();
			throw e;
		} finally {
			if (stmtCheck != null)
				stmtCheck.close();
			if (stmtList != null)
				stmtList.close();
			if (conn != null)
				conn.close();
		}
	}

	public String todoInsert(int serverCode, String todoTitle, String todoWriter, String tags, String postDate)
			throws SQLException {
		Connection conn = null;
		PreparedStatement stmtList = null;
		try {
			conn = conpool.get();
			conn.setAutoCommit(false);

			// Add the writer to the tags
			if (tags == null || tags.isEmpty()) {
				tags = todoWriter;
			} else {
				tags = tags + "," + todoWriter;
			}

			// 1. todolist 삽입
			String sqlList = "INSERT INTO TODOLIST (SERVER_CODE, TODO_TITLE, TODO_WRITER, TAG, TODO_CHECK, POST_DATE) VALUES(?, ?, ?, ?, 0, TO_DATE(?, 'YYYY-MM-DD'))";
			stmtList = conn.prepareStatement(sqlList, new String[] { "TODO_CODE" });
			stmtList.setInt(1, serverCode);
			stmtList.setString(2, todoTitle);
			stmtList.setString(3, todoWriter);
			stmtList.setString(4, tags);
			stmtList.setString(5, postDate);

			int listCount = stmtList.executeUpdate();
			if (listCount != 1)
				return "ER";

			conn.commit();
			return "OK";
		} catch (SQLException e) {
			if (conn != null)
				conn.rollback();
			throw e;
		} finally {
			if (stmtList != null)
				stmtList.close();
			if (conn != null)
				conn.close();
		}
	}

	public List<Map<String, Object>> todoGetgroup(int serverCode, String userId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> todoList = new ArrayList<>();

		try {
			String sql = "SELECT * FROM TODOLIST " + "WHERE SERVER_CODE = ? AND INSTR(TAG, ?) > 0";

			conn = conpool.get();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, serverCode);
			stmt.setString(2, userId);

			rs = stmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> todo = new LinkedHashMap<>();
				// TODOLIST 정보
				todo.put("todo_code", rs.getInt("TODO_CODE"));
				todo.put("title", rs.getString("TODO_TITLE"));
				todo.put("writer", rs.getString("TODO_WRITER"));
				todo.put("check", rs.getInt("TODO_CHECK") == 1);
				todo.put("tag", rs.getString("TAG").split(",")); // 태그 배열 변환
				todo.put("post_date", rs.getDate("POST_DATE"));

				todoList.add(todo);
			}
			return todoList;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
	}

	public boolean todoCheck(int todoCode, int serverCode, String userId) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			String sql = "UPDATE TODOLIST SET TODO_CHECK = 1 - TODO_CHECK " + "WHERE TODO_CODE = ? AND SERVER_CODE = ? "
					+ "AND INSTR(TAG, ?) > 0"; // 태그된 사용자만 체크 가능

			conn = conpool.get();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, todoCode);
			stmt.setInt(2, serverCode);
			stmt.setString(3, userId);

			int count = stmt.executeUpdate();
			return count > 0;
		} finally {
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
	}

	public List<Map<String, Object>> getTodosByPostDateAndUser(int serverCode, String postDate, String userId)
			throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> todoList = new ArrayList<>();

		try {
			// SQL: 날짜(년-월-일) 일치 항목 조회 및 태그에 사용자 ID 포함
			String sql = "SELECT * FROM TODOLIST " + "WHERE SERVER_CODE = ? AND TO_CHAR(POST_DATE, 'YYYY-MM-DD') = ? "
					+ "AND INSTR(TAG, ?) > 0";

			conn = conpool.get();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, serverCode);
			stmt.setString(2, postDate);
			stmt.setString(3, userId);

			rs = stmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> todo = new LinkedHashMap<>();
				// 기본 정보
				todo.put("todo_code", rs.getInt("TODO_CODE"));
				todo.put("title", rs.getString("TODO_TITLE"));
				todo.put("writer", rs.getString("TODO_WRITER"));
				todo.put("check", rs.getInt("TODO_CHECK") == 1);
				todo.put("tag", rs.getString("TAG").split(","));

				// 날짜 정보 (시간 제외)
				java.sql.Date postDateDb = rs.getDate("POST_DATE");
				todo.put("post_date", new java.text.SimpleDateFormat("yyyy-MM-dd").format(postDateDb));

				todoList.add(todo);
			}
			return todoList;
		} finally {
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		}
	}
}
