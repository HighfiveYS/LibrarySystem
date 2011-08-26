package yonsei.highfive.library;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.widget.Toast;
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
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			// ȯ�漳���� ���� �й� �������� //
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
			
			AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(Settings.this, actor, "�л����� �������Դϴ�.");
			
			mJunctionBindingAsyncTask.execute(message); // AsyncTask Thread ����
		}
		return super.onKeyDown(keyCode, event);
	}
    
	// JunctionActor ����
	private UserJunction actor = new UserJunction();
	
	/**
	 * custom Junction Actor Ŭ���� ����
	 * ���ø����̼ǿ��� Actor�� Role�� user ( Java Director�� Role�� director )
	 * onMessageReceived �ڵ鷯�� ������.
	 * @author Lee
	 * @issue �� Ŭ������ �ܺο� �ٽ� �����Ͽ� ������ �����ϵ��� ��������.
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
								Toast.makeText(Settings.this, "�л������� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
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
								Toast.makeText(Settings.this, "�л������� �����Ͽ����ϴ�.", Toast.LENGTH_SHORT).show();
							}
						});
						Settings.this.finish();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
