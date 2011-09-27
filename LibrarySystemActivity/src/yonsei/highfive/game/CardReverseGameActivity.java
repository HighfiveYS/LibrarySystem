package yonsei.highfive.game;

import mobisocial.nfc.Nfc;
import yonsei.highfive.R;
import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CardReverseGameActivity extends Activity {
   
	private WebView mWeb = null;
	private Nfc mNfc = null;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardgame);
        
        mNfc = new Nfc(this);
        String uri = "http://mobilesw.yonsei.ac.kr/game/?service=cardgame";
        NdefRecord uriRecord = new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI, new byte[] {}, uri.getBytes());
        NdefMessage uriMessage = new NdefMessage(new NdefRecord[] {uriRecord});
        
        mNfc.share(uriMessage);
        
        mWeb = (WebView)findViewById(R.id.viewer);
        mWeb.setWebViewClient(new MyWebClient());
        WebSettings set = mWeb.getSettings();
        set.setJavaScriptEnabled(true);//웹뷰의 설정 변경
        set.setBuiltInZoomControls(true);
        mWeb.loadUrl("http://mobilesw.yonsei.ac.kr/game");
    }
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mNfc.onPause(this);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mNfc.onResume(this);
	}
	class MyWebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
               view.loadUrl(url);
               return true;//응용프로그램이 직접 url를 처리함
        }
  }
}