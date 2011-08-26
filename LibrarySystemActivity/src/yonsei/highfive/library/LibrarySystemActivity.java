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
	
	//NFC������ ���߿� Activity�� �±׸� Share�� ���� ���� �� ����� �˴ϴ�.
	//private Nfc mNfc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);	 
        
        /**
		 * NFC�ڵ鷯�� ���� Intent�� �޾� �� �ȿ� �ִ� URI�� ���Ͽ� �ش� service�� �´� Activity�� �����ϴ�
		 * Intent�� ����. ���⼭ �� service�� �ʿ��� �߰����� parameters�� Extra�� �̿��� ���� ������.
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
				this.finish();	// �̰�� ���ξ�Ƽ��Ƽ�� �ʿ䰡 �����Ƿ� �ٷ� finish ���־���.
			} else if (service.equals("gateway")) {
				/**
				 * ���� �ó����� Activity ȣ��
				 */

			} else if (service.equals("slideshow")) {
				/**
				 * Junction Show Activity ȣ��
				 */

			}

			/* ��Ÿ �ó����� */

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
		 * �л� ���� Ȯ��
		 */
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(!pref.getBoolean("certification", false)){
			AlertDialog.Builder builder = new Builder(this);
			builder.setCancelable(true)
				.setMessage("�л� ������ �Ǿ����� �ʽ��ϴ�. ���� �����Ͻðڽ��ϱ�?")
				.setPositiveButton("Ȯ��", new DialogInterface.OnClickListener() {
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