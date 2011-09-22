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
		Button _1_1_10 = (Button)findViewById(R.id.Button10);
		Button _1_1_11 = (Button)findViewById(R.id.Button11);
		Button _1_1_12 = (Button)findViewById(R.id.Button12);
		Button _1_1_13 = (Button)findViewById(R.id.Button13);
		Button _1_1_14 = (Button)findViewById(R.id.Button14);
		Button _1_1_15 = (Button)findViewById(R.id.Button15);

		Button _1_2_16 = (Button)findViewById(R.id.Button16);
		Button _1_2_17 = (Button)findViewById(R.id.Button17);
		Button _1_2_18 = (Button)findViewById(R.id.Button18);
		Button _1_2_19 = (Button)findViewById(R.id.Button19);
		Button _1_2_20 = (Button)findViewById(R.id.Button20);
		Button _1_2_21 = (Button)findViewById(R.id.Button21);
		Button _1_2_22 = (Button)findViewById(R.id.Button22);
		Button _1_2_23 = (Button)findViewById(R.id.Button23);
		Button _1_2_24 = (Button)findViewById(R.id.Button24);
		Button _1_2_25 = (Button)findViewById(R.id.Button25);
		Button _1_2_26 = (Button)findViewById(R.id.Button26);
		Button _1_2_27 = (Button)findViewById(R.id.Button27);
		Button _1_2_28 = (Button)findViewById(R.id.Button28);
		Button _1_2_29 = (Button)findViewById(R.id.Button29);
		Button _1_2_30 = (Button)findViewById(R.id.Button30);

		Button _2_3_31 = (Button)findViewById(R.id.Button31);
		Button _2_3_32 = (Button)findViewById(R.id.Button32);
		Button _2_3_33 = (Button)findViewById(R.id.Button33);
		Button _2_3_34 = (Button)findViewById(R.id.Button34);
		Button _2_3_35 = (Button)findViewById(R.id.Button35);
		Button _2_3_36 = (Button)findViewById(R.id.Button36);
		Button _2_3_37 = (Button)findViewById(R.id.Button37);
		Button _2_3_38 = (Button)findViewById(R.id.Button38);
		Button _2_3_39 = (Button)findViewById(R.id.Button39);
		Button _2_3_40 = (Button)findViewById(R.id.Button40);
		Button _2_3_41 = (Button)findViewById(R.id.Button41);
		Button _2_3_42 = (Button)findViewById(R.id.Button42);
		Button _2_3_43 = (Button)findViewById(R.id.Button43);
		Button _2_3_44 = (Button)findViewById(R.id.Button44);
		Button _2_3_45 = (Button)findViewById(R.id.Button45);

		_1_1_1.setOnClickListener(this);
		_1_1_2.setOnClickListener(this);
		_1_1_3.setOnClickListener(this);
		_1_1_4.setOnClickListener(this);
		_1_1_5.setOnClickListener(this);
		_1_1_6.setOnClickListener(this);
		_1_1_7.setOnClickListener(this);
		_1_1_8.setOnClickListener(this);
		_1_1_9.setOnClickListener(this);
		_1_1_10.setOnClickListener(this);
		_1_1_11.setOnClickListener(this);
		_1_1_12.setOnClickListener(this);
		_1_1_13.setOnClickListener(this);
		_1_1_14.setOnClickListener(this);
		_1_1_15.setOnClickListener(this);

		_1_2_16.setOnClickListener(this);
		_1_2_17.setOnClickListener(this);
		_1_2_18.setOnClickListener(this);
		_1_2_19.setOnClickListener(this);
		_1_2_20.setOnClickListener(this);
		_1_2_21.setOnClickListener(this);
		_1_2_22.setOnClickListener(this);
		_1_2_23.setOnClickListener(this);
		_1_2_24.setOnClickListener(this);
		_1_2_25.setOnClickListener(this);
		_1_2_26.setOnClickListener(this);
		_1_2_27.setOnClickListener(this);
		_1_2_28.setOnClickListener(this);
		_1_2_29.setOnClickListener(this);
		_1_2_30.setOnClickListener(this);

		_2_3_31.setOnClickListener(this);
		_2_3_32.setOnClickListener(this);
		_2_3_33.setOnClickListener(this);
		_2_3_34.setOnClickListener(this);
		_2_3_35.setOnClickListener(this);
		_2_3_36.setOnClickListener(this);
		_2_3_37.setOnClickListener(this);
		_2_3_38.setOnClickListener(this);
		_2_3_39.setOnClickListener(this);
		_2_3_40.setOnClickListener(this);
		_2_3_41.setOnClickListener(this);
		_2_3_42.setOnClickListener(this);
		_2_3_43.setOnClickListener(this);
		_2_3_44.setOnClickListener(this);
		_2_3_45.setOnClickListener(this);

	}

	public void onClick(View v) {
		String temp = null;

		if (v.getId() == R.id.Button1) temp = "1_1_1";
		else if (v.getId() == R.id.Button2) temp = "1_1_2";
		else if (v.getId() == R.id.Button3) temp = "1_1_3";
		else if (v.getId() == R.id.Button4) temp = "1_1_4";
		else if (v.getId() == R.id.Button5) temp = "1_1_5";
		else if (v.getId() == R.id.Button6) temp = "1_1_6";
		else if (v.getId() == R.id.Button7) temp = "1_1_7";
		else if (v.getId() == R.id.Button8) temp = "1_1_8";
		else if (v.getId() == R.id.Button9) temp = "1_1_9";
		else if (v.getId() == R.id.Button10) temp = "1_1_10";
		else if (v.getId() == R.id.Button11) temp = "1_1_11";
		else if (v.getId() == R.id.Button12) temp = "1_1_12";
		else if (v.getId() == R.id.Button13) temp = "1_1_13";
		else if (v.getId() == R.id.Button14) temp = "1_1_14";
		else if (v.getId() == R.id.Button15) temp = "1_1_15";

		else if (v.getId() == R.id.Button16) temp = "1_2_1";
		else if (v.getId() == R.id.Button17) temp = "1_2_2";
		else if (v.getId() == R.id.Button18) temp = "1_2_3";
		else if (v.getId() == R.id.Button19) temp = "1_2_4";
		else if (v.getId() == R.id.Button20) temp = "1_2_5";
		else if (v.getId() == R.id.Button21) temp = "1_2_6";
		else if (v.getId() == R.id.Button22) temp = "1_2_7";
		else if (v.getId() == R.id.Button23) temp = "1_2_8";
		else if (v.getId() == R.id.Button24) temp = "1_2_9";
		else if (v.getId() == R.id.Button25) temp = "1_2_10";
		else if (v.getId() == R.id.Button26) temp = "1_2_11";
		else if (v.getId() == R.id.Button27) temp = "1_2_12";
		else if (v.getId() == R.id.Button28) temp = "1_2_13";
		else if (v.getId() == R.id.Button29) temp = "1_2_14";
		else if (v.getId() == R.id.Button30) temp = "1_2_15";

		else if (v.getId() == R.id.Button31) temp = "2_3_1";
		else if (v.getId() == R.id.Button32) temp = "2_3_2";
		else if (v.getId() == R.id.Button33) temp = "2_3_3";
		else if (v.getId() == R.id.Button34) temp = "2_3_4";
		else if (v.getId() == R.id.Button35) temp = "2_3_5";
		else if (v.getId() == R.id.Button36) temp = "2_3_6";
		else if (v.getId() == R.id.Button37) temp = "2_3_7";
		else if (v.getId() == R.id.Button38) temp = "2_3_8";
		else if (v.getId() == R.id.Button39) temp = "2_3_9";
		else if (v.getId() == R.id.Button40) temp = "2_3_10";
		else if (v.getId() == R.id.Button41) temp = "2_3_11";
		else if (v.getId() == R.id.Button42) temp = "2_3_12";
		else if (v.getId() == R.id.Button43) temp = "2_3_13";
		else if (v.getId() == R.id.Button44) temp = "2_3_14";
		else if (v.getId() == R.id.Button45) temp = "2_3_15";

	
		Intent intent = new Intent(this,
				yonsei.highfive.library.seat.SeatActivity.class);
		Bundle intent_data = new Bundle();
		intent_data.putString("SeatID", temp);
		intent.putExtras(intent_data);
		startActivity(intent);

	}
}