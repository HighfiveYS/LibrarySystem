package yonsei.highfive.library.multimedia;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.stanford.junction.JunctionException;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;

public class MediaControllerActivity extends Activity{

	private Nfc mNfc = null;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			JSONObject msg = new JSONObject();
			try {
				msg.put("action", "return");
				msg.put("service", "exit");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			controller.sendMessageToSession(msg);
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediacontroller);
        
        /*
         * NFC 공유를 설정하는 부분
         * ( onPause와 onResume을 오버라이딩 해야한다. )
         */
        mNfc = new Nfc(this);
        String uri = "http://mobilesw.yonsei.ac.kr/player/?service=player";
        NdefRecord uriRecord = new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[] {}, uri.getBytes());
        NdefMessage uriMessage = new NdefMessage(new NdefRecord[] {uriRecord});
        
        mNfc.share(uriMessage);
        
        
		
        Button _play = (Button)findViewById(R.id.play);
        Button _pause = (Button)findViewById(R.id.pause);
        Button _stop = (Button)findViewById(R.id.stop);
        Button _voldown = (Button)findViewById(R.id.voldown);
        Button _volup = (Button)findViewById(R.id.volup);
        Button _mute = (Button)findViewById(R.id.mute);
        Button _unmute = (Button)findViewById(R.id.unmute);
        
        
        
        OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject msg = new JSONObject();
				try {
					switch (v.getId()) {
					case R.id.play:
						msg.put("action", "play");
						controller.sendMessageToSession(msg);
						break;
					case R.id.pause:
						msg.put("action", "pause");
						controller.sendMessageToSession(msg);
						break;
					case R.id.stop:
						msg.put("action", "stop");
						controller.sendMessageToSession(msg);
						break;
					case R.id.mute:
						msg.put("action", "mute");
						controller.sendMessageToSession(msg);
						break;
					case R.id.unmute:
						msg.put("action", "unmute");
						controller.sendMessageToSession(msg);
						break;
					case R.id.voldown:
						msg.put("action", "voldown");
						controller.sendMessageToSession(msg);
						break;
					case R.id.volup:
						msg.put("action", "volup");
						controller.sendMessageToSession(msg);
						break;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
        _play.setOnClickListener(listener);
        _pause.setOnClickListener(listener);
        _stop.setOnClickListener(listener);
        _voldown.setOnClickListener(listener);
        _volup.setOnClickListener(listener);
        _mute.setOnClickListener(listener);
        _unmute.setOnClickListener(listener);
        
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String switchboard = pref.getString("switchboard", "mobilesw.yonsei.ac.kr"); 
		XMPPSwitchboardConfig config =  new XMPPSwitchboardConfig(switchboard);
		
		URI jxSession = URI.create("http://"+switchboard+"/"+"hf");
		try {
			AndroidJunctionMaker.getInstance(config).newJunction(jxSession,	controller);
			Toast.makeText(this, "Session Connect", Toast.LENGTH_SHORT).show();

	        JSONObject msg = new JSONObject();
			try {
				msg.put("service", "openurl");
				msg.put("url", "http://mobilesw.yonsei.ac.kr/player?jxsessionid=hf");
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			controller.sendMessageToSession(msg);
		} catch (JunctionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
		Intent intent = getIntent();
        Bundle intent_data = intent.getExtras();
        String url = intent_data.getString("url");
        
        JSONObject msg = new JSONObject();
        try {
        	msg.put("action", "load");
			msg.put("url", url);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        controller.sendMessageToSession(msg);
        
        
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mNfc.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mNfc.onResume(this);
	}

	Controller controller = new Controller();

	class Controller extends JunctionActor {

		public Controller(){
			super("controller");
		}
		
		public Controller(String role) {
			super(role);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onMessageReceived(MessageHeader header, JSONObject message) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
