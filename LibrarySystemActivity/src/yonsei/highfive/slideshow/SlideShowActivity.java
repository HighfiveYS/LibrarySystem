package yonsei.highfive.slideshow;

import java.net.URI;

import mobisocial.nfc.Nfc;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.stanford.junction.JunctionException;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;

public class SlideShowActivity extends Activity implements OnClickListener{

	private Nfc mNfc = null;
	private String SessionID = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slideshow);
		
		Intent intent = getIntent();
		Bundle intent_data = intent.getExtras();
	    SessionID = intent_data.getString("SessionID");
	    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
	    String switchboard = pref.getString("switchboard", "mobilesw.yonsei.ac.kr"); 
		XMPPSwitchboardConfig config =  new XMPPSwitchboardConfig(switchboard);
	    URI jxSession = URI.create("junction://"+switchboard+"/"+SessionID);
		try {
			AndroidJunctionMaker.getInstance(config).newJunction(jxSession,	actor);
		} catch (JunctionException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "세션 연결 실패...", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		Toast.makeText(this, "세션 연결 성공!", Toast.LENGTH_LONG).show();
		JSONObject message = new JSONObject();

		try {
			message.put("service","openurl");
			message.put("url","http://mobilesw.yonsei.ac.kr/slideshow?SessionID="+SessionID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actor.sendMessageToSession(message);
		
		
		 /*
        * NFC 공유를 설정하는 부분
        * ( onPause와 onResume을 오버라이딩 해야한다. )
        */
       mNfc = new Nfc(this);
       String uri = "http://mobilesw.yonsei.ac.kr/slideshow/?service=showviewer&SessionID=";
       
       if(SessionID==null)
    	   finish();
       else
    	   uri += SessionID;
       
       NdefRecord uriRecord = new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[] {}, uri.getBytes());
       NdefMessage uriMessage = new NdefMessage(new NdefRecord[] {uriRecord});
       
       mNfc.share(uriMessage);
		
		
		Button _prev = (Button)findViewById(R.id.button_prev);
		Button _next = (Button)findViewById(R.id.button_next);
		_prev.setOnClickListener(this);
		_next.setOnClickListener(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mNfc.onPause(this);
		JSONObject message = new JSONObject();

		try {
			message.put("service","exit");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		actor.sendMessageToSession(message);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mNfc.onResume(this);
	}

	class Controller extends JunctionActor{
		public Controller(String role) {
			super(role);
			// TODO Auto-generated constructor stub
		}
		public Controller(){
			super("contoller");
		}
		@Override
		public void onMessageReceived(MessageHeader header, JSONObject message) {
			// TODO Auto-generated method stub
			
		}
	}
	Controller actor = new Controller();

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.button_prev){
			JSONObject message = new JSONObject();
			try {
				message.put("action", "prev");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			actor.sendMessageToSession(message);
		}
		else if(v.getId()==R.id.button_next){
			JSONObject message = new JSONObject();
			try {
				message.put("action", "next");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			actor.sendMessageToSession(message);
		}
	}
}