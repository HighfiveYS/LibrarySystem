package yonsei.highfive.slideshow;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slideshow);
		
		Intent intent = getIntent();
		Bundle intent_data = intent.getExtras();
	    String SessionID = intent_data.getString("SessionID");
	    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
	    String switchboard = pref.getString("switchboard", "165.132.214.212"); 
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
		
		Button _prev = (Button)findViewById(R.id.button_prev);
		Button _next = (Button)findViewById(R.id.button_next);
		_prev.setOnClickListener(this);
		_next.setOnClickListener(this);
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