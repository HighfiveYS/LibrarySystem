package yonsei.highfive.library.seat;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import yonsei.highfive.library.Settings;
import android.app.Activity;
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


public class SeatVer1Activity extends Activity implements OnClickListener {
    String SeatID = null;
    String UserID = null;
    String StartTime = null;
    String EndTime = null;
    int Hour = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ���̾ƿ� ���� //
        setContentView(R.layout.seat);
        
        // ��ư �ʱ�ȭ, ������ ���� //
        Spinner _hour = (Spinner)findViewById(R.id.spinner_hour);
      
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _hour.setAdapter(adapter);
        
        Button _occupy = (Button)findViewById(R.id.button_occupy);
        Button _return2 = (Button)findViewById(R.id.button_return2);
        Button _extent = (Button)findViewById(R.id.button_extent);
        
        _occupy.setOnClickListener(this);
        _return2.setOnClickListener(this);
        _extent.setOnClickListener(this);
 
  
        // �������� Switchboard ȣ��Ʈ�� �ҷ��� config ���� 
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		switchboard = pref.getString("switchboard", "165.132.214.212"); 
		config = new XMPPSwitchboardConfig(switchboard);
		
	       
        // Intent�� ���� bookid �������� //
        Intent intent = getIntent();
        Bundle intent_data = intent.getExtras();
        
        SeatID = intent_data.getString("SeatID");
        
        JSONObject message = new JSONObject();
        
        try {
        	message.put("service", "checkseat");
			message.put("SeatID", SeatID);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SeatVer1Activity.this, actor,"�ε����Դϴ�.");
		mJunctionBindingAsyncTask.execute(message);

    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try{
			if(v.getId() == R.id.button_occupy){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
				JSONObject message = new JSONObject();
				message.put("service", "occupyseat");
				message.put("SeatID", SeatID);
				message.put("UserID", pref.getString("id", ""));
				message.put("Hour", Hour);
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SeatVer1Activity.this, actor,"�ε����Դϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
			else if(v.getId() == R.id.button_return2){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
				JSONObject message = new JSONObject();
				message.put("service", "returnseat");
				message.put("SeatID", SeatID);
				message.put("UserID", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SeatVer1Activity.this, actor,"�ε����Դϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
			else if(v.getId() == R.id.button_extent){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
				JSONObject message = new JSONObject();
				message.put("service", "extentseat");
				message.put("SeatID", SeatID);
				message.put("UserID", pref.getString("id", ""));
				message.put("Hour", Hour);
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SeatVer1Activity.this, actor,"�ε����Դϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public void setSeattext(String SeatID, String UserID, String StartTime, String EndTime){
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SeatVer1Activity.this);
        Button _occupy = (Button)findViewById(R.id.button_occupy);
        Button _return2 = (Button)findViewById(R.id.button_return2);
        Button _extent = (Button)findViewById(R.id.button_extent);
  
        if(UserID.equals("null")||UserID==null){
        	_occupy.setEnabled(true);
        	_return2.setEnabled(false);
        	_extent.setEnabled(false);
        }
        else if(UserID.equals(pref.getString("id",""))){
        	_occupy.setEnabled(false);
        	_return2.setEnabled(true);
        	_extent.setEnabled(true);
        }
        else{
        	_occupy.setEnabled(false);
        	_return2.setEnabled(false);
        	_extent.setEnabled(false);
        }
        
	    TextView _SeatID= (TextView)SeatVer1Activity.this.findViewById(R.id.SeatID);
        TextView _UserID =  (TextView)SeatVer1Activity.this.findViewById(R.id.UserID);
	    TextView _StartTime = (TextView)SeatVer1Activity.this.findViewById(R.id.StartTime);
	    TextView _EndTime = (TextView)SeatVer1Activity.this.findViewById(R.id.EndTime);
	    TextView _Possible = (TextView)SeatVer1Activity.this.findViewById(R.id.Possible);

	    _SeatID.setText("�¼� ��ȣ : " + SeatID);
		_UserID.setText("�̿��� : " + UserID);
		_StartTime.setText("���� �ð� : " + StartTime);
		_EndTime.setText("���� �ð� : " + EndTime);
		if(UserID==null || UserID.equals("null"))
			_Possible.setText("�̿� ���� ���� : ����");
		else if(UserID.equals(pref.getString("id", "")))
			_Possible.setText("�̿� ���� ���� : ���� ����");
		else
			_Possible.setText("�̿� ���� ���� : �Ұ�");
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
					if(service.equals("checkseat")){
						
						SeatID = message.getString("SeatID");
						UserID = message.getString("UserID");
						StartTime = message.getString("StartTime");
						EndTime = message.getString("EndTime");
						synchronized (actor) {
							 actor.notify();
							 actor.leave();
						}
						// �ȵ���̵� ������ ������ ����ϱ� ���ؼ��� UI Thread�� Main Thread���� �۾��� �̷�������Ѵ�.
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								setSeattext(SeatID, UserID, StartTime, EndTime);
							}
						});

					}
					else if(service.equals("occupyseat")){
						String ack = message.getString("ack");
						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SeatVer1Activity.this);
									UserID = pref.getString("id", "");
									setSeattext(SeatID, UserID, StartTime, EndTime);
									Toast.makeText(SeatVer1Activity.this, "�¼� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(SeatVer1Activity.this, "�¼� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
					}
					else if(service.equals("returnseat")){
						String ack = message.getString("ack");
						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									UserID = "null";
									setSeattext(SeatID, UserID, StartTime, EndTime);
									Toast.makeText(SeatVer1Activity.this, "�¼� �ݳ� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(SeatVer1Activity.this, "�¼� �ݳ� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
					}
					else if(service.equals("extentseat")){
						String ack = message.getString("ack");
						synchronized (actor) {
							actor.notify();
							actor.leave();
						}
						if(ack.equals("true")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SeatVer1Activity.this);
									UserID = pref.getString("id", "");
									setSeattext(SeatID, UserID, StartTime, EndTime);
									Toast.makeText(SeatVer1Activity.this, "�¼� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(SeatVer1Activity.this, "�¼� ���� ����", Toast.LENGTH_LONG).show();
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


