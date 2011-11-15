package yonsei.highfive.library.seat;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;
import yonsei.highfive.library.circulation.SearchBookActivity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;

public class SearchSeatActivity extends TabActivity implements OnClickListener,
		OnTabChangeListener {

	private String SeatID = null;

	private static final int buttons[] = { R.id.Button1, R.id.Button2,
			R.id.Button3, R.id.Button4, R.id.Button5, R.id.Button6,
			R.id.Button7, R.id.Button8, R.id.Button9, R.id.Button10,
			R.id.Button11, R.id.Button12, R.id.Button13, R.id.Button14,
			R.id.Button15, R.id.Button16, R.id.Button17, R.id.Button18,
			R.id.Button19, R.id.Button20, R.id.Button21, R.id.Button22,
			R.id.Button23, R.id.Button24, R.id.Button25, R.id.Button26,
			R.id.Button27 };
	private ImageButton[] seatimg = new ImageButton[27];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.searchseat);

		TabHost clsTabHost = getTabHost();

		clsTabHost.addTab(clsTabHost.newTabSpec("1열람실").setIndicator("1열람실")
				.setContent(R.id.tab1));
		clsTabHost.addTab(clsTabHost.newTabSpec("2열람실").setIndicator("2열람실")
				.setContent(R.id.tab2));
		clsTabHost.addTab(clsTabHost.newTabSpec("3열람실").setIndicator("3열람실")
				.setContent(R.id.tab3));
		clsTabHost.addTab(clsTabHost.newTabSpec("내좌석").setIndicator("내좌석")
				.setContent(R.id.tab4));
		clsTabHost.setCurrentTab(0);

		clsTabHost.setOnTabChangedListener(this);

	}

	@Override
	public void onResume() {
		super.onResume();
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
					if (service.equals("ackcheckseatlist")) {
						final JSONArray list = message.getJSONArray("list");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub

								for (int i = 0; i < 27; i++) {
									seatimg[i].setEnabled(true);
									seatimg[i].setVisibility(View.VISIBLE);
								}
								for (int i = 0; i < list.length(); i++) {
									try {
										seatimg[list.getInt(i) - 1]
												.setEnabled(false);
										seatimg[list.getInt(i) - 1]
												.setVisibility(View.INVISIBLE);
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
					} else if (service.equals("ackcheckmyseat")) {
						SeatID = message.getString("SeatID");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (SeatID == null || SeatID == "not") {
									Toast.makeText(SearchSeatActivity.this,
											"배정받은 좌석이 없습니다.", Toast.LENGTH_LONG)
											.show();
								} else {
									Intent intent = new Intent(
											SearchSeatActivity.this,
											yonsei.highfive.library.seat.SeatActivity.class);
									Bundle intent_data = new Bundle();
									intent_data.putString("SeatID", SeatID);
									intent.putExtras(intent_data);
									startActivity(intent);
								}
							}
						});
						actor.notify();
						actor.leave();
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
		AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(
				SearchSeatActivity.this, actor, "db", "로딩중입니다.");
		mJunctionBindingAsyncTask.execute(message);

		for (int i = 0; i < 27; i++) {
			seatimg[i] = (ImageButton) findViewById(buttons[i]);
			seatimg[i].setOnClickListener(this);
		}
	}

	public void onClick(View v) {

		/*
		 * String temp = null; SharedPreferences pref =
		 * PreferenceManager.getDefaultSharedPreferences
		 * (SearchSeatActivity.this); if(!pref.getBoolean("inlibrary", false)){
		 * Toast.makeText(this, "도서관에 입장한 상태가 아닙니다.",
		 * Toast.LENGTH_SHORT).show(); return; } if (v.getId() == R.id.Button1)
		 * temp = "1_1_1"; else if (v.getId() == R.id.Button2) temp = "1_1_2";
		 * else if (v.getId() == R.id.Button3) temp = "1_1_3"; else if
		 * (v.getId() == R.id.Button4) temp = "1_1_4"; else if (v.getId() ==
		 * R.id.Button5) temp = "1_1_5"; else if (v.getId() == R.id.Button6)
		 * temp = "1_1_6"; else if (v.getId() == R.id.Button7) temp = "1_1_7";
		 * else if (v.getId() == R.id.Button8) temp = "1_1_8"; else if
		 * (v.getId() == R.id.Button9) temp = "1_1_9"; else if (v.getId() ==
		 * R.id.Button10) temp = "1_2_1"; else if (v.getId() == R.id.Button11)
		 * temp = "1_2_2"; else if (v.getId() == R.id.Button12) temp = "1_2_3";
		 * else if (v.getId() == R.id.Button13) temp = "1_2_4"; else if
		 * (v.getId() == R.id.Button14) temp = "1_2_5"; else if (v.getId() ==
		 * R.id.Button15) temp = "1_2_6"; else if (v.getId() == R.id.Button16)
		 * temp = "1_2_7"; else if (v.getId() == R.id.Button17) temp = "1_2_8";
		 * else if (v.getId() == R.id.Button18) temp = "1_2_9"; else if
		 * (v.getId() == R.id.Button19) temp = "2_3_1"; else if (v.getId() ==
		 * R.id.Button20) temp = "2_3_2"; else if (v.getId() == R.id.Button21)
		 * temp = "2_3_3"; else if (v.getId() == R.id.Button22) temp = "2_3_4";
		 * else if (v.getId() == R.id.Button23) temp = "2_3_5"; else if
		 * (v.getId() == R.id.Button24) temp = "2_3_6"; else if (v.getId() ==
		 * R.id.Button25) temp = "2_3_7"; else if (v.getId() == R.id.Button26)
		 * temp = "2_3_8"; else if (v.getId() == R.id.Button27) temp = "2_3_9";
		 * 
		 * 
		 * Intent intent = new Intent(this,
		 * yonsei.highfive.library.seat.SeatActivity.class); Bundle intent_data
		 * = new Bundle(); intent_data.putString("SeatID", temp);
		 * intent.putExtras(intent_data); startActivity(intent);
		 */
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		if (tabId.equals("내좌석")) {
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(this);
			if (!pref.getBoolean("certification", false)) {
				Toast.makeText(this, "학사 인증이 되어있지 않습니다.", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			JSONObject message = new JSONObject();
			try {
				message.put("service", "checkmyseat");
				message.put("userid", pref.getString("id", ""));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AsyncTask<JSONObject, Void, Void> mJunctionBindingAsyncTask = new JunctionAsyncTask(
					SearchSeatActivity.this, actor, "db", "목록을 불러오고 있습니다.");
			mJunctionBindingAsyncTask.execute(message);

		}
	}
}