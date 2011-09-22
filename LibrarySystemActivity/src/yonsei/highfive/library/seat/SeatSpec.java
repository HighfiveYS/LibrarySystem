package yonsei.highfive.library.seat;

import org.json.JSONException;
import org.json.JSONObject;

public class SeatSpec {
	private String SeatID;
	private String UserID;
	private String StartTime;
	private String EndTime;
	private String DoubleSeat;
	
	public SeatSpec(){
		DoubleSeat = "No";
	}
	
	public SeatSpec(String SeatID, String UserID, String StartTime, String EndTime, String DoubleSeat){
		this.SeatID = SeatID;
		this.UserID = UserID;
		this.StartTime = StartTime;
		this.EndTime = EndTime;
		this.DoubleSeat = DoubleSeat;
	}
	
	public SeatSpec(JSONObject json){
		try {
			this.SeatID = json.getString("SeatID");
			this.UserID = json.getString("UserID");
			this.StartTime = json.getString("StartTime");
			this.EndTime = json.getString("EndTime");
			this.DoubleSeat = json.getString("DoubleSeat");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSeatSpec(JSONObject json){
		try {
			this.SeatID = json.getString("SeatID");
			this.UserID = json.getString("UserID");
			this.StartTime = json.getString("StartTime");
			this.EndTime = json.getString("EndTime");
			this.DoubleSeat = json.getString("DoubleSeat");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setSeatSpec(String SeatID, String UserID, String StartTime, String EndTime, String DoubleSeat){
		this.SeatID = SeatID;
		this.UserID = UserID;
		this.StartTime = StartTime;
		this.EndTime = EndTime;
		this.DoubleSeat = DoubleSeat;
	}
	
	public void setSeatID(String SeatID){
		this.SeatID = SeatID;
	}
	public void setUserID(String UserID){
		this.UserID = UserID;
	}
	public void setStartTime(String StartTime){
		this.StartTime = StartTime;
	}
	public void setEndTime(String EndTime){
		this.EndTime = EndTime;
	}
	public void setDoubleSeat(String DoubleSeat){
		this.DoubleSeat = DoubleSeat;
	}
	
	public JSONObject getJSON(){
		JSONObject json = new JSONObject();
		try {
			json.put("SeatID", SeatID);
			json.put("UserID", UserID);
			json.put("StartTime", StartTime);
			json.put("EndTime", EndTime);
			json.put("DoubleSeat", DoubleSeat);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	
	public String getSeatID(){
		return this.SeatID;
	}
	public String getUserID(){
		return this.UserID;
	}
	public String getStartTime(){
		return this.StartTime;
	}
	public String getEndTime(){
		return this.EndTime;
	}
	public String getDoubleSeat(){
		return this.DoubleSeat;
	}

}
