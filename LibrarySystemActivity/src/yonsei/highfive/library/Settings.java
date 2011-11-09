package yonsei.highfive.library;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.widget.Toast;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;

public class Settings extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			// 환경설정을 통해 학번 가져오기 //
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			String id = pref.getString("id", "");
			String pw = pref.getString("pw", "");
			TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			String IMEI = tm.getDeviceId();
			JSONObject message = new JSONObject();
			
			try {
				message.put("service", "certification");
				message.put("id",id);
				message.put("pw",pw);
				message.put("IMEI", IMEI);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(Settings.this, actor, "db", "학사정보 인증중입니다.");
			
			mJunctionBindingAsyncTask.execute(message); // AsyncTask Thread 시작
		}
		return super.onKeyDown(keyCode, event);
	}
    
	// JunctionActor 정의
	private UserJunction actor = new UserJunction();
	
	/**
	 * custom Junction Actor 클래스 생성
	 * 어플리케이션에서 Actor의 Role은 user ( Java Director의 Role은 director )
	 * onMessageReceived 핸들러를 정의함.
	 * @author Lee
	 * @issue 이 클래스를 외부에 다시 정의하여 재사용이 가능하도록 만들어야함.
	 */
	private class UserJunction extends JunctionActor {
		public UserJunction() {
			super("user");
		}

		public UserJunction(String role) {
			super(role);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onMessageReceived(MessageHeader header, JSONObject message) {
			// TODO Auto-generated method stub
			if (message.has("accept")) {
				try {
					if (message.getString("accept").equals("true")) {
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
						pref.edit().putBoolean("certification", true).commit();
						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						runOnUiThread(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(Settings.this, "학사인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
							}
						});
						Settings.this.finish();
					} else if (message.getString("accept").equals("false")) {
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
						pref.edit().putBoolean("certification", false).commit();
						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						runOnUiThread(new Runnable(){
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(Settings.this, "학사인증에 실패하였습니다.", Toast.LENGTH_SHORT).show();
							}
						});
						Settings.this.finish();
					}
					if(message.getString("inlibrary").equals("true")){
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
						pref.edit().putBoolean("inlibrary", true).commit();
					} else if(message.getString("inlibrary").equals("false")){
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
						pref.edit().putBoolean("inlibrary", false).commit();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
