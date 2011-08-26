package yonsei.highfive.library.circulation;

import org.json.JSONException;
import org.json.JSONObject;

public class BookSpec {
	private String bookid;
	private String title;
	private String author;
	private String publisher;
	private String borrower;
	
	public BookSpec(){
	}
	
	public BookSpec(String bookid, String title, String author, String publisher, String borrower){
		this.bookid = bookid;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.borrower = borrower;
	}
	
	public BookSpec(JSONObject json){
		try {
			this.bookid = json.getString("bookid");
			this.title = json.getString("title");
			this.author = json.getString("author");
			this.publisher = json.getString("publisher");
			this.borrower = json.getString("borrower");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setBookSpec(JSONObject json){
		try {
			this.bookid = json.getString("bookid");
			this.title = json.getString("title");
			this.author = json.getString("author");
			this.publisher = json.getString("publisher");
			this.borrower = json.getString("borrower");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setBookSpec(String bookid, String title, String author, String publisher, String borrower){
		this.bookid = bookid;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.borrower = borrower;
	}
	
	public void setBookid(String bookid){
		this.bookid = bookid;
	}
	public void setBorrower(String borrower){
		this.borrower = borrower;
	}
	
	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("bookid", bookid);
			json.put("title", title);
			json.put("author", author);
			json.put("publisher", publisher);
			json.put("borrower", borrower);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	
	public String getBookid(){
		return this.bookid;
	}
	public String getTitle(){
		return this.title;
	}
	public String getAuthor(){
		return this.author;
	}
	public String getPublisher(){
		return this.publisher;
	}
	public String getBorrower(){
		return this.borrower;
	}

}
