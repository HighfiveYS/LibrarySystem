package yonsei.highfive.library.multimedia;

import org.json.JSONException;
import org.json.JSONObject;

public class MediaSpec {
	private String mediaid;
	private String title;
	private String director;
	private String production;
	private String borrower;
	private String medialink;
	
	public MediaSpec(){
	}
	
	public MediaSpec(String mediaid, String title, String director, String production, String borrower){
		this.mediaid = mediaid;
		this.title = title;
		this.director = director;
		this.production = production;
		this.borrower = borrower;
		this.medialink = medialink;
	}
	
	public MediaSpec(JSONObject json){
		try {
			this.mediaid = json.getString("mediaid");
			this.title = json.getString("title");
			this.director = json.getString("director");
			this.production = json.getString("production");
			this.borrower = json.getString("borrower");
			this.medialink = json.getString("medialink");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setmediaSpec(JSONObject json){
		try {
			this.mediaid = json.getString("mediaid");
			this.title = json.getString("title");
			this.director = json.getString("director");
			this.production = json.getString("production");
			this.borrower = json.getString("borrower");
			this.medialink = json.getString("medialink");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setmediaSpec(String mediaid, String title, String director, String production, String borrower, String medialink){
		this.mediaid = mediaid;
		this.title = title;
		this.director = director;
		this.production = production;
		this.borrower = borrower;
		this.medialink = medialink;
	}
	
	public void setmediaid(String mediaid){
		this.mediaid = mediaid;
	}
	public void setBorrower(String borrower){
		this.borrower = borrower;
	}
	
	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("mediaid", mediaid);
			json.put("title", title);
			json.put("director", director);
			json.put("production", production);
			json.put("borrower", borrower);
			json.put("medialink", medialink);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	
	public String getmediaid(){
		return this.mediaid;
	}
	public String getTitle(){
		return this.title;
	}
	public String getdirector(){
		return this.director;
	}
	public String getproduction(){
		return this.production;
	}
	public String getBorrower(){
		return this.borrower;
	}
	public String getMedialink(){
		return this.medialink;
	}

}
