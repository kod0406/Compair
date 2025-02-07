package user;

import java.sql.Timestamp;

public class Todo {
	private int todo_code; // PK (시퀀스 사용)
	private int server_code; // 서버 식별코드 (외래키)
	private String todo_title; // 제목
	private Timestamp post_date; // 생성일
	private String tag; // 분류 태그
	private int todo_check; // 완료여부 (0:미완,1:완료)
	private String todo_writer; // 작성자 (uid)
	private String todo_content; // 내용 (CLOB)
	private String attachment; // 첨부파일 경로
	public int getTodo_code() {
		return todo_code;
	}
	public void setTodo_code(int todo_code) {
		this.todo_code = todo_code;
	}
	public int getServer_code() {
		return server_code;
	}
	public void setServer_code(int server_code) {
		this.server_code = server_code;
	}
	public String getTodo_title() {
		return todo_title;
	}
	public void setTodo_title(String todo_title) {
		this.todo_title = todo_title;
	}
	public Timestamp getPost_date() {
		return post_date;
	}
	public void setPost_date(Timestamp post_date) {
		this.post_date = post_date;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getTodo_check() {
		return todo_check;
	}
	public void setTodo_check(int todo_check) {
		this.todo_check = todo_check;
	}
	public String getTodo_writer() {
		return todo_writer;
	}
	public void setTodo_writer(String todo_writer) {
		this.todo_writer = todo_writer;
	}
	public String getTodo_content() {
		return todo_content;
	}
	public void setTodo_content(String todo_content) {
		this.todo_content = todo_content;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
