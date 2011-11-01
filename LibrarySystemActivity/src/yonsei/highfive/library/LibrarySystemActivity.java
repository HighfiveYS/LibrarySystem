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

	// NFC������ ���߿� Activity�� �±׸� Share�� ���� ���� �� ����� �˴ϴ�.
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
					.setMessage("�л������� �Ǿ����� �ʽ��ϴ�. ���� �����Ͻðڽ��ϱ�?")
					.setPositiveButton("Ȯ��",
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
		 * NFC�ڵ鷯�� ���� Intent�� �޾� �� �ȿ� �ִ� URI�� ���Ͽ� �ش� service�� �´� Activity�� �����ϴ�
		 * Intent�� ����. ���⼭ �� service�� �ʿ��� �߰����� parameters�� Extra�� �̿��� ���� ������.
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
			 * ��Ƽ�̵�� �ڷ� ���� �ó�����
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
			/* ��Ÿ �ó����� */

		}


		/*
		 * �������� �̿��� �޴�
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
	 * �޴����� ������ ������ �� ����â���� �̵��ϴ� Intent �߻�
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
		 * ���� UI�� ���������� ��Ÿ�� ������ư
		 */
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		RadioButton r_inlibrary = (RadioButton)findViewById(R.id.radio_inlibrary);
		RadioButton r_certification = (RadioButton)findViewById(R.id.radio_certification);
		r_inlibrary.setChecked(pref.getBoolean("inlibrary", false));
		r_certification.setChecked(pref.getBoolean("certification", false));
		
	}

}