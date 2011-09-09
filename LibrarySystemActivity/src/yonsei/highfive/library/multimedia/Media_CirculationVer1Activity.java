package yonsei.highfive.library.multimedia;

import java.net.URI;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import yonsei.highfive.library.Settings;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;


public class Media_CirculationVer1Activity extends Activity implements OnClickListener {
    MediaSpec media = null;
 
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ���̾ƿ� ���� //
        setContentView(R.layout.media_circulation);
        
        // ��ư �ʱ�ȭ, ������ ���� //
        Button _borrow = (Button)findViewById(R.id.button_borrow);
        Button _return = (Button)findViewById(R.id.button_return);
        _borrow.setOnClickListener(this);
        _return.setOnClickListener(this);
        //coding by JYP
        Button _play = (Button)findViewById(R.id.button_play);
        _play.setOnClickListener(this);
        
        // MediaSpec ����
        media = new MediaSpec();
        
        // Intent�� ���� mediaid �������� //
        Intent intent = getIntent();
        Bundle intent_data = intent.getExtras();
        media.setmediaid(intent_data.getString("mediaid"));
        
        JSONObject message = new JSONObject();
        
        try {
        	message.put("service", "checkmedia");
			message.put("mediaid", media.getmediaid());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(Media_CirculationVer1Activity.this, actor, "��Ƽ�̵�� ������ �о� ���� ���Դϴ�.");
		mJunctionBindingAsyncTask.execute(message);
		
    }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try{
			if(v.getId() == R.id.button_borrow){		// �뿩 ��û
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Media_CirculationVer1Activity.this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "�л� ������ �Ǿ����� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject message = new JSONObject();
				message.put("service", "borrowmedia");
				message.put("mediaid", media.getmediaid());
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(Media_CirculationVer1Activity.this, actor, "���� ��û���Դϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
			else if(v.getId() == R.id.button_return){	// �ݳ� ��û
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Media_CirculationVer1Activity.this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "�л� ������ �Ǿ����� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject message = new JSONObject();
				message.put("service", "returnmedia");
				message.put("mediaid", media.getmediaid());
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(Media_CirculationVer1Activity.this, actor, "�ݳ� ��û���Դϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
			
			// coding by JYP
			else if(v.getId() == R.id.button_play){	// �׽�Ʈ ��� 
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Media_CirculationVer1Activity.this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "�л� ������ �Ǿ����� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=OZlekwtVfA4"));
				startActivity(intent);
				
				
				
				
			}
			
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public void setmediatext(MediaSpec media){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Media_CirculationVer1Activity.this);
        Button _borrow = (Button)findViewById(R.id.button_borrow);
        Button _return = (Button)findViewById(R.id.button_return);
        Button _play = (Button)findViewById(R.id.button_play);
        TextView _mediaid = (TextView)findViewById(R.id.media_id);
        TextView _title =  (TextView)findViewById(R.id.title);
	    TextView _director = (TextView)findViewById(R.id.director);
	    TextView _production = (TextView)findViewById(R.id.production);
	    TextView _possible = (TextView)findViewById(R.id.possible);

	    String mediaid = media.getmediaid();
	    String title = media.getTitle();
	    String director = media.getdirector();
	    String production = media.getproduction();
	    String borrower = media.getBorrower();
	    
	    if(borrower.equals("null")||borrower==null){
        	_return.setEnabled(false);
        	_borrow.setEnabled(true);
        	_play.setEnabled(false);
        }
        else if(borrower.equals(pref.getString("id",""))){
        	_return.setEnabled(true);
        	_borrow.setEnabled(false);
        	_play.setEnabled(true);
        }
        else{
        	_return.setEnabled(false);
        	_borrow.setEnabled(false);
        	_play.setEnabled(false);
        }
        

	    _mediaid.setText("å ID : " + mediaid);
		_title.setText("���� : " + title);
		_director.setText("���� : " + director);
		_production.setText("���� : " + production);
		if(borrower==null || borrower.equals("null"))
			_possible.setText("���� ���� ���� : ����");
		else if(borrower.equals(pref.getString("id", "")))
			_possible.setText("���� ���� ���� : ����뿩��");
		else
			_possible.setText("���� ���� ���� : �Ұ�");
	}


	// Junction Setup
	private UserJunction actor = new UserJunction();
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
					
//===============================================�ڷ� ���� Ȯ��====================================================//
					if(service.equals("checkmedia")){
						JSONObject mediaspec = message.getJSONObject("media");
						media.setmediaSpec(mediaspec);
						
						synchronized (actor) {
							 actor.notify();
							 actor.leave();
						}
						// �ȵ���̵� ������ ������ ����ϱ� ���ؼ��� UI Thread�� Main Thread���� �۾��� �̷�������Ѵ�.
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								setmediatext(media);
							}
						});

					}

//===============================================�ڷ� �뿩 Ȯ��====================================================//					
					else if(service.equals("borrowmedia")){
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
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Media_CirculationVer1Activity.this);
									media.setBorrower(pref.getString("id", ""));
									setmediatext(media);
									Toast.makeText(Media_CirculationVer1Activity.this, "�ڷ� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(Media_CirculationVer1Activity.this, "�ڷ� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
					}

//===============================================�ڷ� �ݳ� Ȯ��====================================================//
					else if(service.equals("returnmedia")){
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
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Media_CirculationVer1Activity.this);
									media.setBorrower("null");
									setmediatext(media);
									Toast.makeText(Media_CirculationVer1Activity.this, "�ڷ� �ݳ� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(Media_CirculationVer1Activity.this, "�ڷ� �ݳ� ����", Toast.LENGTH_LONG).show();
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

