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
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;


public class GatewayActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// �������� Switchboard ȣ��Ʈ�� �ҷ��� config ���� 
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		switchboard = pref.getString("switchboard", "mobilesw.yonsei.ac.kr"); 
		config = new XMPPSwitchboardConfig(switchboard);


		// Intent�� ���� bookid �������� //
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
		AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(GatewayActivity.this, actor, "db", "����Ȯ�����Դϴ�.");
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
						// �ȵ���̵� ������ ������ ����ϱ� ���ؼ��� UI Thread�� Main Thread���� �۾��� �̷�������Ѵ�.
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(GatewayActivity.this, "�԰�ó�� �Ǿ����ϴ�.", Toast.LENGTH_SHORT);
								}
							});
						}
					}
					else if(service.equals("outgate")){
						String ack = message.getString("ack");

						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						// �ȵ���̵� ������ ������ ����ϱ� ���ؼ��� UI Thread�� Main Thread���� �۾��� �̷�������Ѵ�.
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(GatewayActivity.this, "���ó�� �Ǿ����ϴ�.", Toast.LENGTH_SHORT);
								}
							});
						}
					}
					else if(service.equals("errorgate")){
						String ack = message.getString("ack");

						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						// �ȵ���̵� ������ ������ ����ϱ� ���ؼ��� UI Thread�� Main Thread���� �۾��� �̷�������Ѵ�.
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(GatewayActivity.this, "��ϵ� �����Ͱ� �����ϴ�.", Toast.LENGTH_SHORT);
								}
							});
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
	 * �����޴��� Ŭ������ �� Settings ��Ƽ��Ƽ�� �����ϴ� Intent �߻�
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


