package DAO;

public class FeedObj {
	private String images;
	private String board_code;
	private String title;
	private String author;
	
	public FeedObj(String board_code, String title, String author, String images) {
		this.board_code = board_code;
		this.title = title;
		this.author = author;
		this.images = images;
	}
	public String getId(){return this.board_code;}
	public String getContent(){return this.title;}
	public String getTs(){return this.author;}
	public String getImages(){return this.images;}
}
