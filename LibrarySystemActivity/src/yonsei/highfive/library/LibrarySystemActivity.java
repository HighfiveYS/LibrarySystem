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

			/* ��Ÿ �ó����� */

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

}