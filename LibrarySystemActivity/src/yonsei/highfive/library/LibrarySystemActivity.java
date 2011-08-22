package yonsei.highfive.library;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.stanford.junction.JunctionException;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;

public class LibrarySystemActivity extends Activity {
    /** Called when the activity is first created. */
	
	

	//NFC변수는 나중에 Activity가 태그를 Share할 일이 생길 때 사용이 됩니다.
	//private Nfc mNfc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		
	/**
	 * NFC핸들러에 의한 Intent를 받아 그 안에 있는 URI를 통하여
	 * 해당 service에 맞는 Activity를 시작하는 Intent를 보냄.
	 * 여기서 각 service에 필요한 추가적인 parameters를 Extra를 이용해 같이 보내줌. 
	 */
		if (getIntent().getData() != null &&
		         getIntent().getData().toString().startsWith("http://boom1492.iptime.org")) {
		    Uri data = getIntent().getData();
		    String service = data.getQueryParameter("service");
		    
		    
		    if(service.equals("circulation")){
		    	String bookid = data.getQueryParameter("bookid");
		    	Intent intent = new Intent(this, yonsei.highfive.library.circulation.CirculationVer1Activity.class);
		    	Bundle intent_data = new Bundle();
		    	intent_data.putString("bookid", bookid);
		    	intent.putExtras(intent_data);
		    	startActivity(intent);
		    }
		    else if(service.equals("gateway")){
		    	/**
		    	 * 출입 시나리오 Activity 호출
		    	 */
		    
		    }
		    else if(service.equals("slideshow")){
		    	/**
		    	 * Junction Show Activity 호출
		    	 */
		    }
		    
		    /* 기타 시나리오 */
		
		}
		 
    }
    
	// JunctionActor 정의

	private UserJunction actor = new UserJunction();
	private XMPPSwitchboardConfig config = new XMPPSwitchboardConfig(	"boom1492.iptime.org");
	
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
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LibrarySystemActivity.this);
						pref.edit().putBoolean("certification", true).commit();
						actor.leave();
						synchronized (actor) {
							actor.notify();
						}
					} else if (message.getString("accept").equals("false")) {
						SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(LibrarySystemActivity.this);
						pref.edit().putBoolean("certification", false).commit();
						actor.leave();
						synchronized (actor) {
							actor.notify();
						}
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
	private AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new AsyncTask<JSONObject, Void, Void>() {
		private ProgressDialog mDialog;

		protected Void doInBackground(JSONObject... params) {
			try {
				URI jxSession = URI.create("junction://boom1492.iptime.org/db");
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
			}
			return null;
		};

		@Override
		protected void onPreExecute() {
			if (mDialog == null) {
				mDialog = new ProgressDialog(LibrarySystemActivity.this);
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

	/**
	 * MainActivity를 restart할 때 핸들링 (Settings -> LibrarySystemActivity ?)
	 */
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// 환경설정을 통해 학번 가져오기 //
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		String id = pref.getString("id", "");
		String pw = pref.getString("pw", "");

		// JSON 객체 생성
		JSONObject message = new JSONObject();
		try {

			message.put("action", "dbquery");
			message.put("service", "certification");
			message.put("id", id);
			message.put("pw", pw);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * 여기서 director를 통해 ID, PW를 인증받아야함
		 */

		mJunctionBindingAsyncTask.execute(message); // AsyncTask Thread 시작

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
	 * 메뉴에서 설정을 눌렀을 때 설정창으로 이동하는 Intent 발생
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return false;
	}

}