package yonsei.highfive.library.gate;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import yonsei.highfive.library.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;


public class GatewayActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// 설정에서 Switchboard 호스트를 불러와 config 설정 
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		switchboard = pref.getString("switchboard", "mobilesw.yonsei.ac.kr"); 
		config = new XMPPSwitchboardConfig(switchboard);


		
		// Intent를 통해 bookid 가져오기 //
		Intent intent = getIntent();
		Bundle intent_data = intent.getExtras();


		JSONObject message = new JSONObject();

		try {
			message.put("service", "managegate");
			message.put("UserID", pref.getString("id", ""));
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(GatewayActivity.this, actor, "db", "정보확인중입니다.");
		mJunctionBindingAsyncTask.execute(message);

	}


	// Junction Setup
	private String switchboard;
	private UserJunction actor = new UserJunction();
	private XMPPSwitchboardConfig config = null;


	private class UserJunction extends JunctionActor {

		public UserJunction() {
			super("user");
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onMessageReceived(MessageHeader header, JSONObject message) {
			// TODO Auto-generated method stub

			try {
				if (message.has("service")) {
					String service = message.getString("service");
					if(service.equals("ingate")){
						String ack = message.getString("ack");
						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						System.out.println("ingate");
						// 안드로이드 위젯에 접근해 사용하기 위해서는 UI Thread인 Main Thread에서 작업이 이루어져야한다.
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(GatewayActivity.this, "입관처리 되었습니다.", Toast.LENGTH_SHORT).show();
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(GatewayActivity.this);
									pref.edit().putBoolean("inlibrary", true).commit();

									// 미디어 파일 로드
									MediaPlayer.create(GatewayActivity.this, R.raw.ding).start();
									Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
									vibrator.vibrate(500);
									
								}
							});
							finish();
						}
					}
					else if(service.equals("outgate")){
						String ack = message.getString("ack");

						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						// 안드로이드 위젯에 접근해 사용하기 위해서는 UI Thread인 Main Thread에서 작업이 이루어져야한다.
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(GatewayActivity.this, "퇴관처리 되었습니다.", Toast.LENGTH_LONG).show();
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(GatewayActivity.this);
									pref.edit().putBoolean("inlibrary", false).commit();

									// 미디어 파일 로드
									MediaPlayer.create(GatewayActivity.this, R.raw.ding).start();
									Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
									vibrator.vibrate(500);
								}
							});

							finish();
						}
					}
					else if(service.equals("errorgate")){
						String ack = message.getString("ack");

						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						// 안드로이드 위젯에 접근해 사용하기 위해서는 UI Thread인 Main Thread에서 작업이 이루어져야한다.
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(GatewayActivity.this, "학사인증이 되어있지않습니다.", Toast.LENGTH_LONG).show();
								}
							});
							
							finish();
						}
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 메뉴버튼을 눌렀을 때 설정메뉴를 출력함
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return true;

	}

	/**
	 * 설정메뉴를 클릭했을 때 Settings 액티비티를 시작하는 Intent 발생
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return false;
	}
}


