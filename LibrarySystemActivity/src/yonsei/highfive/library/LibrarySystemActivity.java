package yonsei.highfive.library;

import yonsei.highfive.R;
import yonsei.highfive.library.circulation.SearchBook;
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

public class LibrarySystemActivity extends Activity {
    /** Called when the activity is first created. */
	
	//NFC변수는 나중에 Activity가 태그를 Share할 일이 생길 때 사용이 됩니다.
	//private Nfc mNfc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);	 
        
        /**
		 * NFC핸들러에 의한 Intent를 받아 그 안에 있는 URI를 통하여 해당 service에 맞는 Activity를 시작하는
		 * Intent를 보냄. 여기서 각 service에 필요한 추가적인 parameters를 Extra를 이용해 같이 보내줌.
		 */
		if (getIntent().getData() != null
				&& getIntent().getData().toString().startsWith("http://165.132.214.212")){
			Uri data = getIntent().getData();
			String service = data.getQueryParameter("service");

			if (service.equals("circulation")) {
				String bookid = data.getQueryParameter("bookid");
				Intent intent = new Intent(this, yonsei.highfive.library.circulation.CirculationVer1Activity.class);
				Bundle intent_data = new Bundle();
				intent_data.putString("bookid", bookid);
				intent.putExtras(intent_data);
				startActivity(intent);
				this.finish();	// 이경우 메인액티비티는 필요가 없으므로 바로 finish 해주었음.
			} else if (service.equals("gateway")) {
				/**
				 * 출입 시나리오 Activity 호출
				 */

			} else if (service.equals("slideshow")) {
				/**
				 * Junction Show Activity 호출
				 */

			}

			/* 기타 시나리오 */

		}
		
		Button button_book = (Button)findViewById(R.id.bookservice);
		button_book.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LibrarySystemActivity.this, SearchBook.class);
				startActivity(intent);
			}
		});
		
		/**
		 * 학사 인증 확인
		 */
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(!pref.getBoolean("certification", false)){
			AlertDialog.Builder builder = new Builder(this);
			builder.setCancelable(true)
				.setMessage("학사 인증이 되어있지 않습니다. 지금 인증하시겠습니까?")
				.setPositiveButton("확인", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent setintent = new Intent(LibrarySystemActivity.this, Settings.class);
							startActivity(setintent);
						}
					})
				.create().show();
			
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