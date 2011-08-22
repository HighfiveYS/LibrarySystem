package yonsei.highfive.library;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import yonsei.highfive.R;
import yonsei.highfive.db.DBConnector;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class LibrarySystemActivity extends Activity {
    /** Called when the activity is first created. */
	
	//NFC������ ���߿� Activity�� �±׸� Share�� ���� ���� �� ����� �˴ϴ�.
	//private Nfc mNfc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		
	/**
	 * NFC�ڵ鷯�� ���� Intent�� �޾� �� �ȿ� �ִ� URI�� ���Ͽ�
	 * �ش� service�� �´� Activity�� �����ϴ� Intent�� ����.
	 * ���⼭ �� service�� �ʿ��� �߰����� parameters�� Extra�� �̿��� ���� ������. 
	 */
		if (getIntent().getData() != null &&
		         getIntent().getData().toString().startsWith("http://boom1492.iptime.org")) {
		    Uri data = getIntent().getData();
		    String service = data.getQueryParameter("service");
		    
		    
		    if(service.equals("circulation")){
		    	String bookid = data.getQueryParameter("bookid");
		    	Intent intent = new Intent(this, yonsei.highfive.library.circulation.CirculationVer1Activity.class);
		    	Bundle intent_data = new Bundle();
		    	intent_data.putString("bookid", bookid);
		    	intent.putExtras(intent_data);
		    	startActivity(intent);
		    }
		    else if(service.equals("gateway")){
		    	/**
		    	 * ���� �ó����� Activity ȣ��
		    	 */
		    
		    }
		    else if(service.equals("slideshow")){
		    	/**
		    	 * Junction Show Activity ȣ��
		    	 */
		    }
		    
		    /* ��Ÿ �ó����� */
		
		}
		 
    }
    


	/**
	 * MainActivity�� restart�� �� �ڵ鸵 (Settings -> LibrarySystemActivity ?)
	 */
    @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		// ȯ�漳���� ���� �й� �������� //
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		String id = pref.getString("id", "");
		String pw = pref.getString("pw", "");

		//mJunctionBindingAsyncTask.execute(message); // AsyncTask Thread ����
		Statement stmt = DBConnector.getStatement();
	
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id LIKE " + id);
			
			pref.edit().putBoolean("certification", false).commit();
			while(rs.next()){
				if(rs.getString("user_pw").equals(pw)){
					pref.edit().putBoolean("certification", true).commit();
					return;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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