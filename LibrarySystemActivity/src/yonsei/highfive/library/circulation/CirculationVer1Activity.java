package yonsei.highfive.library.circulation;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import yonsei.highfive.library.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;


public class CirculationVer1Activity extends Activity implements OnClickListener {
    String bookid = null;
    String title = null;
    String author = null;
    String publisher = null;
    String borrower = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ���̾ƿ� ���� //
        setContentView(R.layout.circulation);
        
        // ��ư �ʱ�ȭ, ������ ���� //
        Button _borrow = (Button)findViewById(R.id.button_borrow);
        Button _return = (Button)findViewById(R.id.button_return);
        _borrow.setOnClickListener(this);
        _return.setOnClickListener(this);
 
  
        // �������� Switchboard ȣ��Ʈ�� �ҷ��� config ���� 
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		switchboard = pref.getString("switchboard", "165.132.214.212"); 
		config = new XMPPSwitchboardConfig(switchboard);
		
	       
        // Intent�� ���� bookid �������� //
        Intent intent = getIntent();
        Bundle intent_data = intent.getExtras();
        bookid = intent_data.getString("bookid");
        
        JSONObject message = new JSONObject();
        
        try {
        	message.put("service", "checkbook");
			message.put("bookid", bookid);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationVer1Activity.this, switchboard, config, actor);
		mJunctionBindingAsyncTask.execute(message);

    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try{
			if(v.getId() == R.id.button_borrow){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
				JSONObject message = new JSONObject();
				message.put("service", "borrowbook");
				message.put("bookid", bookid);
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationVer1Activity.this, switchboard, config, actor);
				mJunctionBindingAsyncTask.execute(message);
			}
			else if(v.getId() == R.id.button_return){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
				JSONObject message = new JSONObject();
				message.put("service", "returnbook");
				message.put("bookid", bookid);
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationVer1Activity.this, switchboard, config, actor);
				mJunctionBindingAsyncTask.execute(message);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public void setBooktext(String title, String author, String publisher, String borrower){
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationVer1Activity.this);
        Button _borrow = (Button)findViewById(R.id.button_borrow);
        Button _return = (Button)findViewById(R.id.button_return);
        if(borrower.equals("null")||borrower==null){
        	_return.setEnabled(false);
        	_borrow.setEnabled(true);
        }
        else if(borrower.equals(pref.getString("id",""))){
        	_return.setEnabled(true);
        	_borrow.setEnabled(false);
        }
        else{
        	_return.setEnabled(false);
        	_borrow.setEnabled(false);
        }
        
	    TextView _bookid = (TextView)CirculationVer1Activity.this.findViewById(R.id.book_id);
        TextView _title =  (TextView)CirculationVer1Activity.this.findViewById(R.id.title);
	    TextView _author = (TextView)CirculationVer1Activity.this.findViewById(R.id.author);
	    TextView _publisher = (TextView)CirculationVer1Activity.this.findViewById(R.id.publisher);
	    TextView _possible = (TextView)CirculationVer1Activity.this.findViewById(R.id.possible);

	    _bookid.setText("å ID : " + bookid);
		_title.setText("���� : " + title);
		_author.setText("���� : " + author);
		_publisher.setText("���ǻ� : " + publisher);
		if(borrower==null || borrower.equals("null"))
			_possible.setText("���� ���� ���� : ����");
		else if(borrower.equals(pref.getString("id", "")))
			_possible.setText("���� ���� ���� : ����뿩��");
		else
			_possible.setText("���� ���� ���� : �Ұ�");
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
					if(service.equals("checkbook")){
						
						title = message.getString("title");
						author = message.getString("author");
						publisher = message.getString("publisher");
						borrower = message.getString("borrower");
						synchronized (actor) {
							 actor.notify();
							 actor.leave();
						}
						// �ȵ���̵� ������ ������ ����ϱ� ���ؼ��� UI Thread�� Main Thread���� �۾��� �̷�������Ѵ�.
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								setBooktext(title, author, publisher, borrower);
							}
						});

					}
					else if(service.equals("borrowbook")){
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
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationVer1Activity.this);
									borrower = pref.getString("id", "");
									setBooktext(title, author, publisher, borrower);
									Toast.makeText(CirculationVer1Activity.this, "���� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(CirculationVer1Activity.this, "���� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
					}
					else if(service.equals("returnbook")){
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
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationVer1Activity.this);
									borrower = "null";
									setBooktext(title, author, publisher, borrower);
									Toast.makeText(CirculationVer1Activity.this, "���� �ݳ� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(CirculationVer1Activity.this, "���� �ݳ� ����", Toast.LENGTH_LONG).show();
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

