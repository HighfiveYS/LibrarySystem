package yonsei.highfive.library.multimedia;

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

public class SearchMediaActivity extends TabActivity implements OnTabChangeListener{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchmedia);
		
		TabHost clsTabHost = getTabHost();
		
		clsTabHost.addTab(clsTabHost.newTabSpec("�ڷ� �˻�").setIndicator("�ڷ� �˻�").setContent(R.id.searchmedia));
		clsTabHost.addTab(clsTabHost.newTabSpec("�뿩 ���").setIndicator("�뿩 ���").setContent(R.id.checkborrowed));
		
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
					message.put("service", "searchmedia");
					message.put("keyword", keyword.getText().toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SearchMediaActivity.this, actor, "�˻� ���Դϴ�.");
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
						
	//===============================================�˻� �ڷ���====================================================//
						if(service.equals("searchmedia")){
							JSONArray medias = message.getJSONArray("media");
							final ArrayList<MediaSpec> medialist = new ArrayList<MediaSpec>();
							for(int i=0;i<medias.length();i++){
								MediaSpec bs = new MediaSpec(medias.getJSONObject(i));
								medialist.add(bs);
							}
							runOnUiThread(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									MediaAdapter adapter = new MediaAdapter(SearchMediaActivity.this, R.layout.media, medialist);
									ListView listview = (ListView)findViewById(R.id.searchlist);
									listview.setAdapter(adapter);
								}
							});
							synchronized (actor) {
								actor.notify();
								actor.leave();
							}
						}
	//===========================================�ڷᵵ�����================================================//					
						else if(service.equals("checkborrowed")){
							JSONArray medias = message.getJSONArray("media");
							final ArrayList<MediaSpec> medialist = new ArrayList<MediaSpec>();
							for(int i=0;i<medias.length();i++){
								MediaSpec bs = new MediaSpec(medias.getJSONObject(i));
								medialist.add(bs);
							}
							runOnUiThread(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									MediaAdapter adapter = new MediaAdapter(SearchMediaActivity.this, R.layout.media, medialist);
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
			if(tabId.equals("�뿩 ���")){
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
				if(!pref.getBoolean("certification", false)){
					Toast.makeText(this, "�л� ������ �Ǿ����� �ʽ��ϴ�.", Toast.LENGTH_SHORT).show();
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
				AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SearchMediaActivity.this, actor, "����� �ҷ����� �ֽ��ϴ�.");
				mJunctionBindingAsyncTask.execute(message);
			}
		}
}
