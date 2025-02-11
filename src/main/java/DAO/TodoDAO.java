package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import user.Todo;
import util.conpool;

public class TodoDAO {
	
	public boolean updateTodo() {//수정은 tag가 되어 있는 사람들은 수정 가능하게 + 태그 ,언테그 인원 수정 
        return false;
    }

	public boolean UntagTodos(String uid,int [] todo_code ,int server_code[]) { // 삭제를 누르면 Untag가 되어서 그 사람만 볼 수 없게
		return false;
	}
	
	public boolean deleteTodo() {// 삭제
		return false;
	}

	public String todoInsert(int serverCode, String todoInput, String todoWriter, String Date) throws SQLException {
			Connection conn = null;
	        PreparedStatement stmt = null;
	        try {
	        	//나중에 TODO로 바꿔야함
	        	String sql = "INSERT INTO TODOLIST (SERVER_CODE, TODO_TITLE, TODO_WRITER, TODO_CHECK, POST_DATE) VALUES(?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'))";
	        	System.out.println(sql);
	        	conn = conpool.get();
	            stmt = conn.prepareStatement(sql);
                stmt.setInt(1, serverCode);
                stmt.setString(2, todoInput);
                stmt.setString(3, todoWriter);
                stmt.setInt(4, 0);
                stmt.setString(5, Date);
                int count = stmt.executeUpdate();
                return (count == 1) ? "OK" : "ER";
		
	        } finally {
	            if (stmt != null) stmt.close(); 
	            if (conn != null) conn.close();
	        }
		}
	public String todoGetgroup(String POST_DATE, String serverSession) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	//나중에 TODO로 바꿔야함
        	String sql = "SELECT * FROM TODOLIST WHERE TO_CHAR(POST_DATE, 'YYYY-MM-DD') = '" + POST_DATE + "' AND SERVER_CODE = " + serverSession + " ORDER BY TODO_CODE DESC";
        	System.out.println(sql);
        	conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("TODO_CODE", rs.getString("TODO_CODE"));
                obj.put("SERVER_CODE", rs.getString("SERVER_CODE"));
                obj.put("TODO_TITLE", rs.getString("TODO_TITLE"));
                obj.put("TODO_WRITER", rs.getString("TODO_WRITER"));
                obj.put("TODO_CHECK", rs.getString("TODO_CHECK"));
                obj.put("TODO_CONTENT", rs.getString("TODO_CONTENT"));
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
	
	public String todoShowGetgroup(String serverSession) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
        	//나중에 TODO로 바꿔야함
        	String sql = "SELECT * FROM TODOLIST WHERE SERVER_CODE = " + serverSession + " ORDER BY POST_DATE";
        	System.out.println(sql);
        	conn = conpool.get();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("TODO_CODE", rs.getString("TODO_CODE"));
                obj.put("SERVER_CODE", rs.getString("SERVER_CODE"));
                obj.put("TODO_TITLE", rs.getString("TODO_TITLE"));
                obj.put("TODO_WRITER", rs.getString("TODO_WRITER"));
                obj.put("TODO_CHECK", rs.getString("TODO_CHECK"));
                obj.put("TODO_CONTENT", rs.getString("TODO_CONTENT"));
                obj.put("POST_DATE", rs.getString("POST_DATE"));
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
