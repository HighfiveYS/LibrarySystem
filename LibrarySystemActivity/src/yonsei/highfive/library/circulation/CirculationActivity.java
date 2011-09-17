package yonsei.highfive.library.circulation;

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


public class CirculationActivity extends Activity implements OnClickListener {
    BookSpec book = null;
 
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
        
        // BookSpec ����
        book = new BookSpec();
        
        // Intent�� ���� bookid �������� //
        Intent intent = getIntent();
        Bundle intent_data = intent.getExtras();
        book.setBookid(intent_data.getString("bookid"));
        
        JSONObject message = new JSONObject();
        
        try {
        	message.put("service", "checkbook");
			message.put("bookid", book.getBookid());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationActivity.this, actor, "db", "å ������ �о� ���� ���Դϴ�.");
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
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationActivity.this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "�л� ������ �Ǿ����� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject message = new JSONObject();
				message.put("service", "borrowbook");
				message.put("bookid", book.getBookid());
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationActivity.this, actor, "db", "���� ��û���Դϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
			else if(v.getId() == R.id.button_return){	// �ݳ� ��û
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationActivity.this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "�л� ������ �Ǿ����� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject message = new JSONObject();
				message.put("service", "returnbook");
				message.put("bookid", book.getBookid());
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationActivity.this, actor, "db", "�ݳ� ��û���Դϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
	}
	
	public void setBooktext(BookSpec book){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationActivity.this);
        Button _borrow = (Button)findViewById(R.id.button_borrow);
        Button _return = (Button)findViewById(R.id.button_return);
        TextView _bookid = (TextView)findViewById(R.id.book_id);
        TextView _title =  (TextView)findViewById(R.id.title);
	    TextView _author = (TextView)findViewById(R.id.author);
	    TextView _publisher = (TextView)findViewById(R.id.publisher);
	    TextView _possible = (TextView)findViewById(R.id.possible);
	    TextView _starttime = (TextView)findViewById(R.id.starttime);
	    TextView _endtime = (TextView)findViewById(R.id.endtime);
	    
	    String bookid = book.getBookid();
	    String title = book.getTitle();
	    String author = book.getAuthor();
	    String publisher = book.getPublisher();
	    String borrower = book.getBorrower();
	    String StartTime = book.getStartTime();
	    String EndTime = book.getEndTime();
	    
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
        

	    _bookid.setText("å ID : " + bookid);
		_title.setText("���� : " + title);
		_author.setText("���� : " + author);
		_publisher.setText("���ǻ� : " + publisher);
		_starttime.setText("����Ⱓ : " + StartTime);
		_endtime.setText("�ݳ��Ⱓ : " + EndTime);
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
					
//===============================================���� ���� Ȯ��====================================================//
					if(service.equals("checkbook")){
						JSONObject bookspec = message.getJSONObject("book");
						book.setBookSpec(bookspec);
						
						synchronized (actor) {
							 actor.notify();
							 actor.leave();
						}
						// �ȵ���̵� ������ ������ ����ϱ� ���ؼ��� UI Thread�� Main Thread���� �۾��� �̷�������Ѵ�.
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								setBooktext(book);
							}
						});

					}

//===============================================���� �뿩 Ȯ��====================================================//					
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
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationActivity.this);
									book.setBorrower(pref.getString("id", ""));
									Toast.makeText(CirculationActivity.this, "���� ���� ����", Toast.LENGTH_LONG).show();
									finish();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(CirculationActivity.this, "���� ���� ����", Toast.LENGTH_LONG).show();
								}
							});
						}
					}

//===============================================���� �ݳ� Ȯ��====================================================//
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
									SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationActivity.this);
									Toast.makeText(CirculationActivity.this, "���� �ݳ� ����", Toast.LENGTH_LONG).show();
									finish();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(CirculationActivity.this, "���� �ݳ� ����", Toast.LENGTH_LONG).show();
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

