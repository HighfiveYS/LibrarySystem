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
	
	

	//NFC������ ���߿� Activity�� �±׸� Share�� ���� ���� �� ����� �˴ϴ�.
	//private Nfc mNfc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		
	/**
	 * NFC�ڵ鷯�� ���� Intent�� �޾� �� �ȿ� �ִ� URI�� ���Ͽ�
	 * �ش� service�� �´� Activity�� �����ϴ� Intent�� ����.
	 * ���⼭ �� service�� �ʿ��� �߰����� parameters�� Extra�� �̿��� ���� ������. 
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
		    	 * ���� �ó����� Activity ȣ��
		    	 */
		    
		    }
		    else if(service.equals("slideshow")){
		    	/**
		    	 * Junction Show Activity ȣ��
		    	 */
		    }
		    
		    /* ��Ÿ �ó����� */
		
		}
		 
    }
    
	// JunctionActor ����

	private UserJunction actor = new UserJunction();
	private XMPPSwitchboardConfig config = new XMPPSwitchboardConfig(	"boom1492.iptime.org");
	
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
	 * AsynchTask ��ü�� Junction Connection, Session Join �� Director�� ���� ������ ����
	 * ������ ��ٸ� (Android������ ���Ǵ� ������ ������ Thread)
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
				mDialog.setMessage("�л� ���� ��...");
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
	 * MainActivity�� restart�� �� �ڵ鸵 (Settings -> LibrarySystemActivity ?)
	 */
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// ȯ�漳���� ���� �й� �������� //
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		String id = pref.getString("id", "");
		String pw = pref.getString("pw", "");

		// JSON ��ü ����
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
		 * ���⼭ director�� ���� ID, PW�� �����޾ƾ���
		 */

		mJunctionBindingAsyncTask.execute(message); // AsyncTask Thread ����

	}

	/**
	 * �޴���ư�� ������ �� �����޴��� �����
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
	 * �޴����� ������ ������ �� ����â���� �̵��ϴ� Intent �߻�
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