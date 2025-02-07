package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import user.Todo;

public class TodoDAO {
		
	Connection conn = null;
    PreparedStatement stmt = null;
	
	public boolean insertTodo(Todo todo) { //tag 인원 설정 가능(오직 추가만)
		return false;
		
	}
	
	public boolean updateTodo() {//수정은 tag가 되어 있는 사람들은 수정 가능하게 + 태그 ,언테그 인원 수정 
        return false;
    }

	public boolean UntagTodos(String uid,int [] todo_code ,int server_code[]) { // 삭제를 누르면 Untag가 되어서 그 사람만 볼 수 없게
		return false;
	}
	
	public boolean deleteTodo() {// 삭제
		return false;
	}

	public String getTodo() {//Todo를 받아서 보여주기
		return null;
		
    }

	
}
