package yonsei.highfive.library.seat;

import java.util.ArrayList;

import org.apache.http.entity.SerializableEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.api.messaging.MessageHeader;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;
import yonsei.highfive.R;
import yonsei.highfive.junction.JunctionAsyncTask;

import android.app.TabActivity;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class SearchSeatActivity extends TabActivity implements OnClickListener{
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.searchseat);

		TabHost clsTabHost = getTabHost();

		clsTabHost.addTab(clsTabHost.newTabSpec("1열람실").setIndicator("1열람실").setContent(R.id.tab1));
		clsTabHost.addTab(clsTabHost.newTabSpec("2열람실").setIndicator("2열람실").setContent(R.id.tab2));
		clsTabHost.addTab(clsTabHost.newTabSpec("3열람실").setIndicator("3열람실").setContent(R.id.tab3));

		clsTabHost.setCurrentTab(0);

		setButton();
	}


	private void setButton() {
		// TODO Auto-generated method stub
		Button _1_1_1 = (Button)findViewById(R.id.Button1);
		Button _1_1_2 = (Button)findViewById(R.id.Button2);
		Button _1_1_3 = (Button)findViewById(R.id.Button3);
		Button _1_1_4 = (Button)findViewById(R.id.Button4);
		Button _1_1_5 = (Button)findViewById(R.id.Button5);
		Button _1_1_6 = (Button)findViewById(R.id.Button6);
		Button _1_1_7 = (Button)findViewById(R.id.Button7);
		Button _1_1_8 = (Button)findViewById(R.id.Button8);
		Button _1_1_9 = (Button)findViewById(R.id.Button9);
		Button _1_2_10 = (Button)findViewById(R.id.Button10);
		Button _1_2_11 = (Button)findViewById(R.id.Button11);
		Button _1_2_12 = (Button)findViewById(R.id.Button12);
		Button _1_2_13 = (Button)findViewById(R.id.Button13);
		Button _1_2_14 = (Button)findViewById(R.id.Button14);
		Button _1_2_15 = (Button)findViewById(R.id.Button15);

		Button _1_2_16 = (Button)findViewById(R.id.Button16);
		Button _1_2_17 = (Button)findViewById(R.id.Button17);
		Button _1_2_18 = (Button)findViewById(R.id.Button18);
		Button _2_3_19 = (Button)findViewById(R.id.Button19);
		Button _2_3_20 = (Button)findViewById(R.id.Button20);
		Button _2_3_21 = (Button)findViewById(R.id.Button21);
		Button _2_3_22 = (Button)findViewById(R.id.Button22);
		Button _2_3_23 = (Button)findViewById(R.id.Button23);
		Button _2_3_24 = (Button)findViewById(R.id.Button24);
		Button _2_3_25 = (Button)findViewById(R.id.Button25);
		Button _2_3_26 = (Button)findViewById(R.id.Button26);
		Button _2_3_27 = (Button)findViewById(R.id.Button27);

		_1_1_1.setOnClickListener(this);
		_1_1_2.setOnClickListener(this);
		_1_1_3.setOnClickListener(this);
		_1_1_4.setOnClickListener(this);
		_1_1_5.setOnClickListener(this);
		_1_1_6.setOnClickListener(this);
		_1_1_7.setOnClickListener(this);
		_1_1_8.setOnClickListener(this);
		_1_1_9.setOnClickListener(this);
		_1_2_10.setOnClickListener(this);
		_1_2_11.setOnClickListener(this);
		_1_2_12.setOnClickListener(this);
		_1_2_13.setOnClickListener(this);
		_1_2_14.setOnClickListener(this);
		_1_2_15.setOnClickListener(this);
		_1_2_16.setOnClickListener(this);
		_1_2_17.setOnClickListener(this);
		_1_2_18.setOnClickListener(this);
		_2_3_19.setOnClickListener(this);
		_2_3_20.setOnClickListener(this);
		_2_3_21.setOnClickListener(this);
		_2_3_22.setOnClickListener(this);
		_2_3_23.setOnClickListener(this);
		_2_3_24.setOnClickListener(this);
		_2_3_25.setOnClickListener(this);
		_2_3_26.setOnClickListener(this);
		_2_3_27.setOnClickListener(this);
		}

	public void onClick(View v) {
		String temp = null;
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(SearchSeatActivity.this);
		if(!pref.getBoolean("inlibrary", false)){
			Toast.makeText(this, "도서관에 입장한 상태가 아닙니다.", Toast.LENGTH_SHORT).show();
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