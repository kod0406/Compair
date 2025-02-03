package user;

public class User {
	private String uid;
	private String upass;
	private String email;
	private String user_id;
	private boolean APPROVED;
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUpass() {
		return upass;
	}
	public void setUpass(String upass) {
		this.upass = upass;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public boolean getAPPROVED() {
		return APPROVED;
	}
	public void setAPPROVED(boolean aPPROVED) {
		APPROVED = aPPROVED;
	}
	
	
}
