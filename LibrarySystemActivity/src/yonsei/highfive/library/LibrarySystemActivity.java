package yonsei.highfive.library;

import yonsei.highfive.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class LibrarySystemActivity extends Activity {
    /** Called when the activity is first created. */
	
	//NFC변수는 나중에 Activity가 태그를 Share할 일이 생길 때 사용이 됩니다.
	//private Nfc mNfc;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		
	/**
	 * NFC핸들러에 의한 Intent를 받아 그 안에 있는 URI를 통하여
	 * 해당 service에 맞는 Activity를 시작하는 Intent를 보냄.
	 * 여기서 각 service에 필요한 추가적인 parameters를 Extra를 이용해 같이 보내줌. 
	 */
		if (getIntent().getData() != null &&
		         getIntent().getData().toString().startsWith("http://165.132.214.212")) {
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
		    	 * 출입 시나리오 Activity 호출
		    	 */
		    
		    }
		    else if(service.equals("slideshow")){
		    	/**
		    	 * Junction Show Activity 호출
		    	 */
		    }
		    
		    /* 기타 시나리오 */
		
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