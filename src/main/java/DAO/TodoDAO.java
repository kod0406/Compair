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
	
	/*
	 * public boolean updateTodo() {//수정은 tag가 되어 있는 사람들은 수정 가능하게 + 태그 ,언테그 인원 수정
	 * return false; }
	 * 
	 * public boolean UntagTodos(String uid,int [] todo_code ,int server_code[]) {
	 * // 삭제를 누르면 Untag가 되어서 그 사람만 볼 수 없게 return false; }
	 * 
	 * public boolean deleteTodo() {// 삭제 return false; }
	 * 
	 * public String todoInsert(int serverCode, String todoInput, String todoWriter)
	 * throws SQLException { Connection conn = null; PreparedStatement stmt = null;
	 * try { //나중에 TODO로 바꿔야함 String sql =
	 * "INSERT INTO TODOLIST (SERVER_CODE, TODO_TITLE, TODO_WRITER, TODO_CHECK) VALUES(?, ?, ?, ?)"
	 * ; System.out.println(sql); conn = conpool.get(); stmt =
	 * conn.prepareStatement(sql); stmt.setInt(1, serverCode); stmt.setString(2,
	 * todoInput); stmt.setString(3, todoWriter); stmt.setInt(4, 0); int count =
	 * stmt.executeUpdate(); return (count == 1) ? "OK" : "ER";
	 * 
	 * } finally { if (stmt != null) stmt.close(); if (conn != null) conn.close(); }
	 * } public String todoGetgroup(String POST_DATE, String serverSession) throws
	 * SQLException { Connection conn = null; PreparedStatement stmt = null;
	 * ResultSet rs = null; try { //나중에 TODO로 바꿔야함 String sql =
	 * "SELECT * FROM TODOLIST WHERE TO_CHAR(POST_DATE, 'YYYY-MM-DD') = '" +
	 * POST_DATE + "' AND SERVER_CODE = " + serverSession +
	 * " ORDER BY TODO_CODE DESC"; System.out.println(sql); conn = conpool.get();
	 * stmt = conn.prepareStatement(sql); rs = stmt.executeQuery();
	 * 
	 * JSONArray jsonArray = new JSONArray(); while (rs.next()) { JSONObject obj =
	 * new JSONObject(); obj.put("TODO_CODE", rs.getString("TODO_CODE"));
	 * obj.put("SERVER_CODE", rs.getString("SERVER_CODE")); obj.put("TODO_TITLE",
	 * rs.getString("TODO_TITLE")); obj.put("TODO_WRITER",
	 * rs.getString("TODO_WRITER")); obj.put("TODO_CHECK",
	 * rs.getString("TODO_CHECK")); jsonArray.add(obj); }
	 * System.out.println(jsonArray.toString()); return jsonArray.toString(); }
	 * finally { if (rs != null) rs.close(); if (stmt != null) stmt.close(); if
	 * (conn != null) conn.close(); } }
	 */
	public Map<String, Object> updateTodo(int todoCode, int serverCode, String newTitle, String newContent, String newTag, String userId) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmtList = null;
	    PreparedStatement stmtContent = null;
	    PreparedStatement stmtGetTags = null;
	    ResultSet rs = null;
	    Map<String, Object> resultMap = new HashMap<>();
	    try {
	        conn = conpool.get();
	        conn.setAutoCommit(false);

	        // 1. 기존 태그 조회
	        String sqlGetTags = "SELECT TAG FROM TODOLIST WHERE TODO_CODE = ? AND SERVER_CODE = ? AND INSTR(TAG, ?) > 0";
	        stmtGetTags = conn.prepareStatement(sqlGetTags);
	        stmtGetTags.setInt(1, todoCode);
	        stmtGetTags.setInt(2, serverCode);
	        stmtGetTags.setString(3, userId);
	        rs = stmtGetTags.executeQuery();

	        String updatedTags;
	        List<String> duplicateTags = new ArrayList<>();
	        if (rs.next()) {
	            String existingTags = rs.getString("TAG");
	            Set<String> existingTagSet = new HashSet<>(Arrays.asList(existingTags.split(",")));
	            Set<String> newTagSet = new HashSet<>(Arrays.asList(newTag.split(",")));

	            // 중복 태그 확인
	            for (String tag : newTagSet) {
	                if (existingTagSet.contains(tag)) {
	                    duplicateTags.add(tag); // 중복된 태그 추가
	                } else {
	                    existingTagSet.add(tag); // 새 태그 추가
	                }
	            }

	            updatedTags = String.join(",", existingTagSet);
	        } else {
	            resultMap.put("success", false);
	            return resultMap; // 권한 없음 또는 데이터 없음
	        }

	        // 2. todoList 테이블 업데이트
	        String sqlList = "UPDATE TODOLIST SET TODO_TITLE = ?, TAG = ? " +
	                        "WHERE TODO_CODE = ? AND SERVER_CODE = ? AND INSTR(TAG, ?) > 0";
	        stmtList = conn.prepareStatement(sqlList);
	        stmtList.setString(1, newTitle);
	        stmtList.setString(2, updatedTags);
	        stmtList.setInt(3, todoCode);
	        stmtList.setInt(4, serverCode);
	        stmtList.setString(5, userId);

	        int listCount = stmtList.executeUpdate();

	        // 3. todoContent 테이블 업데이트
	        String sqlContent = "UPDATE TODOCONTENT SET TODO_CONTENT = ? " +
	                          "WHERE TODO_CODE = ? AND SERVER_CODE = ?";
	        stmtContent = conn.prepareStatement(sqlContent);
	        stmtContent.setString(1, newContent);
	        stmtContent.setInt(2, todoCode);
	        stmtContent.setInt(3, serverCode);

	        int contentCount = stmtContent.executeUpdate();

	        boolean success = (listCount > 0 && contentCount > 0);
	        if (success) {
	            conn.commit();
	            resultMap.put("success", true);
	            resultMap.put("duplicateTags", duplicateTags); // 중복 태그 반환
	            return resultMap;
	        } else {
	            conn.rollback();
	            resultMap.put("success", false);
	            return resultMap;
	        }
	        
	    } catch (SQLException e) {
	        if (conn != null) conn.rollback();
	        throw e;
	    } finally {
	        if (rs != null) rs.close();
	        if (stmtGetTags != null) stmtGetTags.close();
	        if (stmtList != null) stmtList.close();
	        if (stmtContent != null) stmtContent.close();
	        if (conn != null) conn.close();
	    }
	}

	
	public boolean UntagTodos(String currentUserId, int[] todoCodes, int[] serverCodes) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    try {
	        // SQL: 정규 표현식을 사용해 태그와 불필요한 쉼표 제거
	        String sql = "UPDATE TODOLIST " +
	                     "SET TAG = REGEXP_REPLACE(TAG, '(^|,)(' || ? || ')(,|$)', '\\1') " +
	                     "WHERE TODO_CODE = ? AND SERVER_CODE = ? AND INSTR(TAG, ?) > 0";
	        
	        conn = conpool.get();
	        stmt = conn.prepareStatement(sql);

	        for (int i = 0; i < todoCodes.length; i++) {
	            stmt.setString(1, currentUserId); // 제거할 사용자 ID
	            stmt.setInt(2, todoCodes[i]);     // Todo 코드
	            stmt.setInt(3, serverCodes[i]);   // 서버 코드
	            stmt.setString(4, currentUserId); // 권한 확인용
	            stmt.addBatch();                  // Batch로 실행 준비
	        }

	        int[] results = stmt.executeBatch();  // Batch 실행
	        for (int result : results) {
	            if (result == 0) return false; // 하나라도 실패하면 false 반환
	        }
	        return true; // 모두 성공하면 true 반환
	    } finally {
	        if (stmt != null) stmt.close();
	        if (conn != null) conn.close();
	    }
	}
	public boolean deleteTodos(int[] todoCodes, int[] serverCodes) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmtContent = null;
	    PreparedStatement stmtList = null;
	    try {
	        conn = conpool.get();
	        conn.setAutoCommit(false);

	        // 1. todoContent 삭제 (배치 처리)
	        String sqlContent = "DELETE FROM TODOCONTENT WHERE TODO_CODE = ? AND SERVER_CODE = ?";
	        stmtContent = conn.prepareStatement(sqlContent);
	        for (int i = 0; i < todoCodes.length; i++) {
	            stmtContent.setInt(1, todoCodes[i]);
	            stmtContent.setInt(2, serverCodes[i]);
	            stmtContent.addBatch();
	        }
	        int[] contentResults = stmtContent.executeBatch();

	        // 2. todoList 삭제 (배치 처리)
	        String sqlList = "DELETE FROM TODOLIST WHERE TODO_CODE = ? AND SERVER_CODE = ?";
	        stmtList = conn.prepareStatement(sqlList);
	        for (int i = 0; i < todoCodes.length; i++) {
	            stmtList.setInt(1, todoCodes[i]);
	            stmtList.setInt(2, serverCodes[i]);
	            stmtList.addBatch();
	        }
	        int[] listResults = stmtList.executeBatch();

	        // 트랜잭션 커밋
	        conn.commit();

	        // 3. 삭제 성공 여부 검증 (최소 1건 성공 시 true)
	        boolean contentSuccess = Arrays.stream(contentResults).anyMatch(res -> res > 0);
	        boolean listSuccess = Arrays.stream(listResults).anyMatch(res -> res > 0);
	        return contentSuccess && listSuccess;

	    } catch (SQLException e) {
	        if (conn != null) conn.rollback();
	        throw e;
	    } finally {
	        if (stmtContent != null) stmtContent.close();
	        if (stmtList != null) stmtList.close();
	        if (conn != null) conn.close();
	    }
	}
	

	public String todoInsert(int serverCode, String todoTitle, String todoContent, String todoWriter, String tags, String postDate) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmtList = null;
	    PreparedStatement stmtContent = null;
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
	        stmtList = conn.prepareStatement(sqlList, new String[]{"TODO_CODE"});
	        stmtList.setInt(1, serverCode);
	        stmtList.setString(2, todoTitle);
	        stmtList.setString(3, todoWriter);
	        stmtList.setString(4, tags);
	        stmtList.setString(5, postDate);

	        int listCount = stmtList.executeUpdate();
	        if (listCount != 1) return "ER";

	        // 2. 생성된 todo_code 조회
	        ResultSet rs = stmtList.getGeneratedKeys();
	        int generatedCode = rs.next() ? rs.getInt(1) : -1;
	        if (generatedCode == -1) return "ER";

	        // 3. todocontent 삽입
	        String sqlContent = "INSERT INTO TODOCONTENT (TODO_CODE, SERVER_CODE, TODO_CONTENT) VALUES(?, ?, ?)";
	        stmtContent = conn.prepareStatement(sqlContent);
	        stmtContent.setInt(1, generatedCode);
	        stmtContent.setInt(2, serverCode);
	        stmtContent.setString(3, todoContent);

	        int contentCount = stmtContent.executeUpdate();
	        conn.commit();

	        return (contentCount == 1) ? "OK" : "ER";
	    } catch (SQLException e) {
	        if (conn != null) conn.rollback();
	        throw e;
	    } finally {
	        if (stmtList != null) stmtList.close();
	        if (stmtContent != null) stmtContent.close();
	        if (conn != null) conn.close();
	    }
	}


	public List<Map<String, Object>> todoGetgroup(int serverCode, String userId) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    List<Map<String, Object>> todoList = new ArrayList<>();

	    try {
	        String sql = "SELECT l.*, c.TODO_CONTENT, c.ATTACHMENT " +
	                     "FROM TODOLIST l " +
	                     "JOIN TODOCONTENT c ON l.TODO_CODE = c.TODO_CODE AND l.SERVER_CODE = c.SERVER_CODE " +
	                     "WHERE l.SERVER_CODE = ? AND INSTR(l.TAG, ?) > 0";
	        
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
	            
	            // TODOCONTENT 정보
	            todo.put("content", rs.getString("TODO_CONTENT"));
	            todo.put("attachment", rs.getString("ATTACHMENT"));
	            
	            todoList.add(todo);
	        }
	        return todoList;
	    } finally {
	        if (rs != null) rs.close();
	        if (stmt != null) stmt.close();
	        if (conn != null) conn.close();
	    }
	}
	
	public boolean todoCheck(int todoCode, int serverCode, String userId) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    try {
	        String sql = "UPDATE TODOLIST SET TODO_CHECK = 1 - TODO_CHECK " +
	                     "WHERE TODO_CODE = ? AND SERVER_CODE = ? " +
	                     "AND INSTR(TAG, ?) > 0"; // 태그된 사용자만 체크 가능
	        
	        conn = conpool.get();
	        stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, todoCode);
	        stmt.setInt(2, serverCode);
	        stmt.setString(3, userId);
	        
	        int count = stmt.executeUpdate();
	        return count > 0;
	    } finally {
	        if (stmt != null) stmt.close();
	        if (conn != null) conn.close();
	    }
	}

	public List<Map<String, Object>> getTodosByPostDate(int serverCode, String postDate) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    List<Map<String, Object>> todoList = new ArrayList<>();

	    try {
	        // SQL: 날짜(년-월-일) 일치 항목 조회
	        String sql = "SELECT l.*, c.TODO_CONTENT, c.ATTACHMENT " +
	                     "FROM TODOLIST l " +
	                     "JOIN TODOCONTENT c ON l.TODO_CODE = c.TODO_CODE AND l.SERVER_CODE = c.SERVER_CODE " +
	                     "WHERE l.SERVER_CODE = ? AND TO_CHAR(l.POST_DATE, 'YYYY-MM-DD') = ?";
	        
	        conn = conpool.get();
	        stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, serverCode);
	        stmt.setString(2, postDate);
	        
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
	            
	            // 상세 내용
	            todo.put("content", rs.getString("TODO_CONTENT"));
	            todo.put("attachment", rs.getString("ATTACHMENT"));
	            
	            todoList.add(todo);
	        }
	        return todoList;
	    } finally {
	        if (rs != null) rs.close();
	        if (stmt != null) stmt.close();
	        if (conn != null) conn.close();
	    }
	}
}
