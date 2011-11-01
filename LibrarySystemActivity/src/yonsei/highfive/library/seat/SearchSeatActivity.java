package yonsei.highfive.library.seat;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.Toast;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;


public class SearchSeatActivity extends TabActivity implements OnClickListener{
	

	private static final int buttons[] = { R.id.Button1, R.id.Button2, R.id.Button3, R.id.Button4, R.id.Button5, R.id.Button6, R.id.Button7, R.id.Button8, R.id.Button9, R.id.Button10, R.id.Button11, R.id.Button12, R.id.Button13, R.id.Button14, R.id.Button15, R.id.Button16, R.id.Button17, R.id.Button18, R.id.Button19, R.id.Button20, R.id.Button21, R.id.Button22, R.id.Button23, R.id.Button24, R.id.Button25, R.id.Button26, R.id.Button27};
	private ImageButton[] seatimg = new ImageButton[27];
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.searchseat);

		TabHost clsTabHost = getTabHost();

		clsTabHost.addTab(clsTabHost.newTabSpec("1������").setIndicator("1������").setContent(R.id.tab1));
		clsTabHost.addTab(clsTabHost.newTabSpec("2������").setIndicator("2������").setContent(R.id.tab2));
		clsTabHost.addTab(clsTabHost.newTabSpec("3������").setIndicator("3������").setContent(R.id.tab3));

		clsTabHost.setCurrentTab(0);

		
		setButton();
		
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
						if(service.equals("ackcheckseatlist")){
							final JSONArray list = message.getJSONArray("list");
							
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub

									for(int i=0;i<27;i++){
										seatimg[i].setEnabled(true);
										seatimg[i].setVisibility(View.VISIBLE);
									}
									for(int i=0;i<list.length();i++){
										try {
											seatimg[list.getInt(i)-1].setEnabled(false);
											seatimg[list.getInt(i)-1].setVisibility(View.INVISIBLE);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
									
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

	private void setButton() {
		// TODO Auto-generated method stub
		
		JSONObject message = null;
		try {
			message = new JSONObject("{service:checkseatlist}");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(SearchSeatActivity.this, actor, "db", "�ε����Դϴ�.");
		mJunctionBindingAsyncTask.execute(message);
			
		for(int i=0;i<27;i++){
			seatimg[i] = (ImageButton)findViewById(buttons[i]);
			seatimg[i].setOnClickListener(this);
		}
	}

	public void onClick(View v) {
		String temp = null;
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SearchSeatActivity.this);
		if(!pref.getBoolean("inlibrary", false)){
			Toast.makeText(this, "�������� ������ ���°� �ƴմϴ�.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (v.getId() == R.id.Button1) temp = "1_1_1";
		else if (v.getId() == R.id.Button2) temp = "1_1_2";
		else if (v.getId() == R.id.Button3) temp = "1_1_3";
		else if (v.getId() == R.id.Button4) temp = "1_1_4";
		else if (v.getId() == R.id.Button5) temp = "1_1_5";
		else if (v.getId() == R.id.Button6) temp = "1_1_6";
		else if (v.getId() == R.id.Button7) temp = "1_1_7";
		else if (v.getId() == R.id.Button8) temp = "1_1_8";
		else if (v.getId() == R.id.Button9) temp = "1_1_9";
		else if (v.getId() == R.id.Button10) temp = "1_2_10";
		else if (v.getId() == R.id.Button11) temp = "1_2_11";
		else if (v.getId() == R.id.Button12) temp = "1_2_12";
		else if (v.getId() == R.id.Button13) temp = "1_2_13";
		else if (v.getId() == R.id.Button14) temp = "1_2_14";
		else if (v.getId() == R.id.Button15) temp = "1_2_15";
		else if (v.getId() == R.id.Button16) temp = "1_2_16";
		else if (v.getId() == R.id.Button17) temp = "1_2_17";
		else if (v.getId() == R.id.Button18) temp = "1_2_18";
		else if (v.getId() == R.id.Button19) temp = "2_3_19";
		else if (v.getId() == R.id.Button20) temp = "2_3_20";
		else if (v.getId() == R.id.Button21) temp = "2_3_21";
		else if (v.getId() == R.id.Button22) temp = "2_3_22";
		else if (v.getId() == R.id.Button23) temp = "2_3_23";
		else if (v.getId() == R.id.Button24) temp = "2_3_24";
		else if (v.getId() == R.id.Button25) temp = "2_3_25";
		else if (v.getId() == R.id.Button26) temp = "2_3_26";
		else if (v.getId() == R.id.Button27) temp = "2_3_27";

	
		Intent intent = new Intent(this,
				yonsei.highfive.library.seat.SeatActivity.class);
		Bundle intent_data = new Bundle();
		intent_data.putString("SeatID", temp);
		intent.putExtras(intent_data);
		startActivity(intent);

	}
}