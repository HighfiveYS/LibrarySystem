package yonsei.highfive.library.circulation;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import android.app.TabActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class SearchBookActivity extends TabActivity implements OnTabChangeListener{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchbook);
		
		TabHost clsTabHost = getTabHost();
		
		clsTabHost.addTab(clsTabHost.newTabSpec("도서 검색").setIndicator("도서 검색").setContent(R.id.searchbook));
		clsTabHost.addTab(clsTabHost.newTabSpec("대여 목록").setIndicator("대여 목록").setContent(R.id.checkborrowedbook));
		
		clsTabHost.setCurrentTab(0);
		clsTabHost.setOnTabChangedListener(this);
		
		final EditText keyword = (EditText)findViewById(R.id.search_keyword);
		Button button_search = (Button)findViewById(R.id.button_search);
		button_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JSONObject message = new JSONObject();
				try {
					message.put("service", "searchbook");
					message.put("keyword", keyword.getText().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SearchBookActivity.this, actor, "db", "검색 중입니다.");
				mJunctionBindingAsyncTask.execute(message);
			}
		});
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
						
	//===============================================검색 도서목록====================================================//
						if(service.equals("searchbook")){
							JSONArray books = message.getJSONArray("book");
							final ArrayList<BookSpec> booklist = new ArrayList<BookSpec>();
							for(int i=0;i<books.length();i++){
								BookSpec bs = new BookSpec(books.getJSONObject(i));
								booklist.add(bs);
							}
							runOnUiThread(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									BookAdapter adapter = new BookAdapter(SearchBookActivity.this, R.layout.book, booklist);
									ListView listview = (ListView)findViewById(R.id.searchlist);
									listview.setAdapter(adapter);
								}
							});
							synchronized (actor) {
								actor.notify();
								actor.leave();
							}
						}
	//===========================================대여도서목록================================================//					
						else if(service.equals("checkborrowed")){
							JSONArray books = message.getJSONArray("book");
							final ArrayList<BookSpec> booklist = new ArrayList<BookSpec>();
							for(int i=0;i<books.length();i++){
								BookSpec bs = new BookSpec(books.getJSONObject(i));
								booklist.add(bs);
							}
							runOnUiThread(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									BookAdapter adapter = new BookAdapter(SearchBookActivity.this, R.layout.book, booklist);
									ListView listview = (ListView)findViewById(R.id.borrowlist);
									listview.setAdapter(adapter);
								}
							});
							synchronized (actor) {
								actor.notify();
								actor.leave();
							}
						}
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		@Override
		public void onTabChanged(String tabId) {
			// TODO Auto-generated method stub
			if(tabId.equals("대여 목록")){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "학사 인증이 되어있지 않습니다.", Toast.LENGTH_SHORT).show();
					return;
				}
				JSONObject message = new JSONObject();
				try {
					message.put("service", "checkborrowed");
					message.put("userid", pref.getString("id", ""));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SearchBookActivity.this, actor, "db", "목록을 불러오고 있습니다.");
				mJunctionBindingAsyncTask.execute(message);
			}
		}
}
