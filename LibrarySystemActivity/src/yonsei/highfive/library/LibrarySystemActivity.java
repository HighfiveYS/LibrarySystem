package yonsei.highfive.library;

import yonsei.highfive.R;
import yonsei.highfive.game.CardReverseGameActivity;
import yonsei.highfive.library.circulation.SearchBookActivity;
import yonsei.highfive.library.seat.SearchSeatActivity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

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
				/*&& getIntent().getData().toString()
						.startsWith("http://mobilesw.yonsei.ac.kr")*/) {
			Uri data = getIntent().getData();
			String service = data.getQueryParameter("service");
			if(service==null){
				finish();
			}
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
			else if (service.equals("cardgame")){
				Intent intent = new Intent(this, yonsei.highfive.game.CardReverseGameActivity.class);
				startActivity(intent);
			}
			else if (service.equals("player")){
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mobilesw.yonsei.ac.kr/player"));
				startActivity(intent);
			}
			else if (service.equals("showviewer")){
				String SessionID = data.getQueryParameter("SessionID");
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mobilesw.yonsei.ac.kr/slideshow?jxsessionid="+SessionID));
				startActivity(intent);
			}
			/* 기타 시나리오 */

		}


		/*
		 * 갤러리를 이용한 메뉴
		 */
		int[] image = {R.drawable.book, R.drawable.seat,  R.drawable.game, R.drawable.setting, R.drawable.help};
		
		GalleryForOneFling gallery = (GalleryForOneFling)findViewById(R.id.gallerymenu);
		GalleryMenuAdapter gm = new GalleryMenuAdapter(this, R.layout.gallerymenu, image);
		gallery.setAdapter(gm);
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView parent, View v, int pos, long id) {
				// TODO Auto-generated method stub
				switch(pos){
				case 0:
					Intent intent = new Intent(LibrarySystemActivity.this, SearchBookActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(LibrarySystemActivity.this, SearchSeatActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(LibrarySystemActivity.this, CardReverseGameActivity.class);
					startActivity(intent);
					break;
				case 3:
					startActivity(new Intent(LibrarySystemActivity.this, Settings.class));
					break;
				case 4:
					startActivity(new Intent(LibrarySystemActivity.this, HelpActivity.class));
					break;
				case 5:
					break;
				}
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*
		 * 메인 UI의 상태정보를 나타낼 라디오버튼
		 */
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		RadioButton r_inlibrary = (RadioButton)findViewById(R.id.radio_inlibrary);
		RadioButton r_certification = (RadioButton)findViewById(R.id.radio_certification);
		r_inlibrary.setChecked(pref.getBoolean("inlibrary", false));
		r_certification.setChecked(pref.getBoolean("certification", false));
		
	}

}