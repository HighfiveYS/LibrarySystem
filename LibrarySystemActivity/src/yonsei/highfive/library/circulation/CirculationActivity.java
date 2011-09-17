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
        
        // 레이아웃 지정 //
        setContentView(R.layout.circulation);
        
        // 버튼 초기화, 리스너 지정 //
        Button _borrow = (Button)findViewById(R.id.button_borrow);
        Button _return = (Button)findViewById(R.id.button_return);
        _borrow.setOnClickListener(this);
        _return.setOnClickListener(this);
        
        // BookSpec 생성
        book = new BookSpec();
        
        // Intent를 통해 bookid 가져오기 //
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
        AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationActivity.this, actor, "db", "책 정보를 읽어 오는 중입니다.");
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
			if(v.getId() == R.id.button_borrow){		// 대여 요청
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationActivity.this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "학사 인증이 되어있지 않습니다.", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject message = new JSONObject();
				message.put("service", "borrowbook");
				message.put("bookid", book.getBookid());
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationActivity.this, actor, "db", "대출 요청중입니다.");
				mJunctionBindingAsyncTask.execute(message);
			}
			else if(v.getId() == R.id.button_return){	// 반납 요청
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationActivity.this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "학사 인증이 되어있지 않습니다.", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject message = new JSONObject();
				message.put("service", "returnbook");
				message.put("bookid", book.getBookid());
				message.put("userid", pref.getString("id", ""));
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(CirculationActivity.this, actor, "db", "반납 요청중입니다.");
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
        

	    _bookid.setText("책 ID : " + bookid);
		_title.setText("제목 : " + title);
		_author.setText("저자 : " + author);
		_publisher.setText("출판사 : " + publisher);
		_starttime.setText("대출기간 : " + StartTime);
		_endtime.setText("반납기간 : " + EndTime);
		if(borrower==null || borrower.equals("null"))
			_possible.setText("대출 가능 여부 : 가능");
		else if(borrower.equals(pref.getString("id", "")))
			_possible.setText("대출 가능 여부 : 현재대여중");
		else
			_possible.setText("대출 가능 여부 : 불가");
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
					
//===============================================도서 정보 확인====================================================//
					if(service.equals("checkbook")){
						JSONObject bookspec = message.getJSONObject("book");
						book.setBookSpec(bookspec);
						
						synchronized (actor) {
							 actor.notify();
							 actor.leave();
						}
						// 안드로이드 위젯에 접근해 사용하기 위해서는 UI Thread인 Main Thread에서 작업이 이루어져야한다.
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								setBooktext(book);
							}
						});

					}

//===============================================도서 대여 확인====================================================//					
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
									Toast.makeText(CirculationActivity.this, "도서 대출 성공", Toast.LENGTH_LONG).show();
									finish();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(CirculationActivity.this, "도서 대출 실패", Toast.LENGTH_LONG).show();
								}
							});
						}
					}

//===============================================도서 반납 확인====================================================//
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
									Toast.makeText(CirculationActivity.this, "도서 반납 성공", Toast.LENGTH_LONG).show();
									finish();
								}
							});
						}
						else if(ack.equals("false")){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Toast.makeText(CirculationActivity.this, "도서 반납 실패", Toast.LENGTH_LONG).show();
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

