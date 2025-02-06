package user;

import java.sql.Timestamp;

public class Mail {
	private int mail_code;
	private int server_code;
	private String writer;
	private String receiver;
	private Timestamp post_date;
	private String mail_title;
	private String todoContent;
	private String attachment;
	public int getMail_code() {
		return mail_code;
	}
	public void setMail_code(int mail_code) {
		this.mail_code = mail_code;
	}
	public int getServer_code() {
		return server_code;
	}
	public void setServer_code(int server_code) {
		this.server_code = server_code;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public Timestamp getPost_date() {
		return post_date;
	}
	public void setPost_date(Timestamp post_date) {
		this.post_date = post_date;
	}
	public String getMail_title() {
		return mail_title;
	}
	public void setMail_title(String mail_title) {
		this.mail_title = mail_title;
	}
	public String getTodoContent() {
		return todoContent;
	}
	public void setTodoContent(String todoContent) {
		this.todoContent = todoContent;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
