package yonsei.highfive.library.circulation;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import yonsei.highfive.R;
import yonsei.highfive.library.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import edu.stanford.junction.JunctionException;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;


public class CirculationVer1Activity extends Activity implements OnClickListener {
    String bookid = null;
//    String title = null;
//    String author = null;
//    String publisher = null;
//    String borrower = null;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ���̾ƿ� ���� //
        setContentView(R.layout.circulation);
        
        // ��ư �ʱ�ȭ, ������ ���� //
        Button _burrow = (Button)findViewById(R.id.button_burrow);
        Button _return = (Button)findViewById(R.id.button_return);
//        _burrow.setOnClickListener(this);
//        _return.setOnClickListener(this);
// 
  
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
        
        mJunctionBindingAsyncTask = new AsyncTask<JSONObject, Void, Void>() {
			private ProgressDialog mDialog;

			protected Void doInBackground(JSONObject... params) {
				try {
					URI jxSession = URI.create("junction://"+switchboard+"/db");
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
					CirculationVer1Activity.this.finish();
				}
				return null;
			};

			@Override
			protected void onPreExecute() {
				if (mDialog == null) {
					mDialog = new ProgressDialog(CirculationVer1Activity.this);
					mDialog.setMessage("�����κ��� �����͸� �޴� ��...");
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
		mJunctionBindingAsyncTask.execute(message);
        
		
    }
	
	public void setBooktext(String title, String author, String publisher, String borrower){
	
	    TextView _bookid = (TextView)CirculationVer1Activity.this.findViewById(R.id.book_id);
        TextView _title =  (TextView)CirculationVer1Activity.this.findViewById(R.id.title);
	    TextView _author = (TextView)CirculationVer1Activity.this.findViewById(R.id.author);
	    TextView _publisher = (TextView)CirculationVer1Activity.this.findViewById(R.id.publisher);
	    TextView _possible = (TextView)CirculationVer1Activity.this.findViewById(R.id.possible);

		_title.setText("���� : " + title);
		_author.setText("���� : " + author);
		_publisher.setText("���ǻ� : " + publisher);
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(CirculationVer1Activity.this);
		if(borrower==null || borrower.equals("null"))
			_possible.setText("���� ���� ���� : ����");
		else if(borrower.equals(pref.getString("id", "")))
			_possible.setText("���� ���� ���� : ����뿩��");
		else
			_possible.setText("���� ���� ���� : �Ұ�");
	}

	/**
	 * AsynchTask ��ü�� Junction Connection, Session Join �� Director�� ���� ������ ����
	 * ������ ��ٸ� (Android������ ���Ǵ� ������ ������ Thread)
	 */
	private AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask;
	
	
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
						
						String title = message.getString("title");
						String author = message.getString("author");
						String publisher = message.getString("publisher");
						String borrower = message.getString("borrower");
						synchronized (actor) {
							 actor.notify();
							 actor.leave();
						}
						setBooktext(title, author, publisher, borrower);
							

					}
					else if(service.equals("burrowbook")){
						
					}
					else if(service.equals("returnbook")){
						
					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	private void showDialog(final CirculationVer1Activity activity, String title, String text) {
			
		AlertDialog.Builder ad = new AlertDialog.Builder(activity);
		ad.setTitle(title);
		ad.setMessage(text);
		ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichbutton) {
				activity.setResult(Activity.RESULT_OK);
			}
		});
		
		ad.create();
		ad.show();
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		/*
		if(v.getId() == R.id.button_burrow){
			;
		}
		else if(v.getId() == R.id.button_return){
			;
		}
		*/
	}
}

