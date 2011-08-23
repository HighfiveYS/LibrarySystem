package yonsei.highfive.library;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import edu.stanford.junction.JunctionException;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;

public class Settings extends PreferenceActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		switchboard = pref.getString("switchboard", "165.132.214.212"); 
		config = new XMPPSwitchboardConfig(switchboard);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			// 환경설정을 통해 학번 가져오기 //
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			String id = pref.getString("id", "");
			String pw = pref.getString("pw", "");

			JSONObject message = new JSONObject();
			
			try {
				message.put("service", "certification");
				message.put("id",id);
				message.put("pw",pw);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			mJunctionBindingAsyncTask = new AsyncTask<JSONObject, Void, Void>() {
				private ProgressDialog mDialog;

				protected Void doInBackground(JSONObject... params) {
					try {
						URI jxSession = URI.create("junction://"+switchboard+"/db");
						AndroidJunctionMaker.getInstance(config).newJunction(jxSession,	actor);
						synchronized (actor) {
							try {
								actor.sendMessageToRole("director", params[0]);
								actor.wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
						}
					} catch (JunctionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Settings.this.finish();
					}
					return null;
				};

				@Override
				protected void onPreExecute() {
					if (mDialog == null) {
						mDialog = new ProgressDialog(Settings.this);
						mDialog.setMessage("학사 인증 중...");
						mDialog.setIndeterminate(true);
						mDialog.setCancelable(true);
						mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
							@Override
							public void onCancel(DialogInterface arg0) {
							}
						});
						mDialog.show();
					}
				}

				protected void onPostExecute(Void result) {
					mDialog.hide();
				};
			};
			mJunctionBindingAsyncTask.execute(message); // AsyncTask Thread 시작
		}
		return super.onKeyDown(keyCode, event);
	}
    
	// JunctionActor 정의

	private String switchboard;
	private UserJunction actor = new UserJunction();
	private XMPPSwitchboardConfig config = null;
	
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
						Settings.this.finish();
					} else if (message.getString("accept").equals("false")) {
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
						pref.edit().putBoolean("certification", false).commit();
						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						Settings.this.finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * AsynchTask 객체로 Junction Connection, Session Join 및 Director로 부터 응답을 받을
	 * 때까지 기다림 (Android에서만 사용되는 일종의 간단한 Thread)
	 */
	private AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask;

}
