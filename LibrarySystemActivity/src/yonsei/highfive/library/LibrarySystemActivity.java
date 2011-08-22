package yonsei.highfive.library;

import mobisocial.nfc.Nfc;
import yonsei.highfive.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;

public class LibrarySystemActivity extends Activity {
    /** Called when the activity is first created. */
	
	
	/**
	 * NFC������ ���߿� Activity�� �±׸� Share�� ���� ���� �� ����� �˴ϴ�.
	 */
	private Nfc mNfc;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
   
    /**
     * prpl.stanford.edu�� Host���ϴ� XMPP SwitchboardConfig�� ������ �� Junction URI�� ����
     * �Ŀ� Session�� �̿��� ����� �ʿ��� �� ����� �� ����.
     */
		XMPPSwitchboardConfig xmppConfig = new XMPPSwitchboardConfig("prpl.stanford.edu");
		Uri jxUri = Uri.parse(AndroidJunctionMaker.getInstance(xmppConfig).generateSessionUri().toString());

		
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
		    else if(service.equals("gate")){
		    	/**
		    	 * ���� �ó����� Activity ȣ��
		    	 */
		    
		    }
		    else if(service.equals("show")){
		    	/**
		    	 * Junction Show Activity ȣ��
		    	 */
		    }
		    
		    /* ��Ÿ �ó����� */
		
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
		switch(item.getItemId()){
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return false;
	}
  
}