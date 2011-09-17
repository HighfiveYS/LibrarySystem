package yonsei.highfive.library;

import yonsei.highfive.R;
import yonsei.highfive.library.circulation.SearchBookActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LibrarySystemActivity extends Activity {
	/** Called when the activity is first created. */

	// NFC변수는 나중에 Activity가 태그를 Share할 일이 생길 때 사용이 됩니다.
	// private Nfc mNfc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (!pref.getBoolean("certification", false)) {
			AlertDialog.Builder builder = new Builder(this);
			builder.setCancelable(true)
					.setMessage("학사인증이 되어있지 않습니다. 지금 인증하시겠습니까?")
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent setintent = new Intent(
											LibrarySystemActivity.this,
											Settings.class);
									startActivity(setintent);
								}
							}).create().show();
		}

		/**
		 * NFC핸들러에 의한 Intent를 받아 그 안에 있는 URI를 통하여 해당 service에 맞는 Activity를 시작하는
		 * Intent를 보냄. 여기서 각 service에 필요한 추가적인 parameters를 Extra를 이용해 같이 보내줌.
		 */
		if (getIntent().getData() != null
				&& getIntent().getData().toString()
						.startsWith("http://mobilesw.yonsei.ac.kr")) {
			Uri data = getIntent().getData();
			String service = data.getQueryParameter("service");

			if (service.equals("circulation")) {
				String bookid = data.getQueryParameter("bookid");
				Intent intent = new Intent(
						this,
						yonsei.highfive.library.circulation.CirculationActivity.class);
				Bundle intent_data = new Bundle();
				intent_data.putString("bookid", bookid);
				intent.putExtras(intent_data);
				startActivity(intent);
			} else if (service.equals("seat")) {
				String SeatID = data.getQueryParameter("SeatID");
				Intent intent = new Intent(this,
						yonsei.highfive.library.seat.SeatActivity.class);
				Bundle intent_data = new Bundle();
				intent_data.putString("SeatID", SeatID);
				intent.putExtras(intent_data);
				startActivity(intent);
			}else if(service.equals("gateway")){
		    	String UserID = data.getQueryParameter("UserID");
		    	Intent intent = new Intent(this, yonsei.highfive.library.gate.GatewayActivity.class);
		    	Bundle intent_data = new Bundle();
		    	intent_data.putString("UserID", UserID);
		    	intent.putExtras(intent_data);
		    	startActivity(intent);
		    } else if (service.equals("slideshow")) {
				String SessionID = data.getQueryParameter("SessionID");
				Intent intent = new Intent(this, yonsei.highfive.slideshow.SlideShowActivity.class);
				Bundle intent_data = new Bundle();
				intent_data.putString("SessionID", SessionID);
				intent.putExtras(intent_data);
				startActivity(intent);
			}
			/*
			 * 멀티미디어 자료 대출 시나리오
			 */
			else if (service.equals("media_circulation")) {
				String mediaid = data.getQueryParameter("mediaid");
				Intent intent = new Intent(
						this,
						yonsei.highfive.library.multimedia.MediaCirculationActivity.class);
				Bundle intent_data = new Bundle();
				intent_data.putString("mediaid", mediaid);
				intent.putExtras(intent_data);
				startActivity(intent);
			}

			/* 기타 시나리오 */

		}

		Button button_book = (Button) findViewById(R.id.bookservice);
		button_book.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LibrarySystemActivity.this,
						SearchBookActivity.class);
				startActivity(intent);
			}
		});
		
		Button button_slideshow = (Button)findViewById(R.id.slideshow);
		button_slideshow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageView image = (ImageView)findViewById(R.id.touchimage);
				if(!image.isShown())
					image.setVisibility(View.VISIBLE);
			}
		});

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
	 * 메뉴에서 설정을 눌렀을 때 설정창으로 이동하는 Intent 발생
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return false;
	}

}