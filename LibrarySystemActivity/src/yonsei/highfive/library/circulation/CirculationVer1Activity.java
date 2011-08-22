package yonsei.highfive.library.circulation;

import java.util.ArrayList;

import yonsei.highfive.R;
import yonsei.highfive.library.LibrarySystemActivity;
import yonsei.highfive.library.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class CirculationVer1Activity extends Activity 
	implements OnClickListener {
	
	private Button button; 
	String userid;
	String userpw;
	
	String bookid;

	
	UserSpec queryuser = new UserSpec();
	
	// 이용자 계정 // 
	UserSpec user = new UserSpec();
	
	// 도서 객체들 //
	BookSpec _123 = new BookSpec("005.123", "안드로이드 공략", "김전화", "IT출판사");    // 빌려진 책
	BookSpec _234 = new BookSpec("005.234", "자바 공략", "이자바", "이클립스출판");      // 빌릴 책
	BookSpec _345 = new BookSpec("005.345", "씨언어 공략", "최코딩", "굿소프트");       // 다른사람이 빌린책
	
	// 유저 계정과 책의 리스트들. -> 나중에 DB를 쓰면 없앨 예정 //
	ArrayList <UserSpec> UserList = new ArrayList<UserSpec>();
	ArrayList <BookSpec> BookList = new ArrayList<BookSpec>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 레이아웃 지정///
        setContentView(R.layout.circulation);
        
        // 컴포넌트 관련 //
        button = (Button) this.findViewById(R.id.button_p);
        button.setOnClickListener(this);
        
        
        // 이용자  계정 데이터 정의 //
        user.set_UserID("2000253000");
        user.set_UserName("홍길동");
        user.set_Password("123456");
        user.set_Delay_pay(0);
        user.set_Borr_num(0);
        
        // 도서 상태 정의 // 
        _123.set_LanedDate(110815);
        _123.set_DueDate(110830);
        _123.set_Borrower("2000253000");
        _345.set_LanedDate(110815);
        _345.set_DueDate(110830);
        _345.set_Borrower("2000253011");
        
        // 이용자 계정에 빌린 책 정보 넣기
        BookSpec prebook = new BookSpec();
        prebook = _123;
		user.Borrowing.add(prebook);
        // 이용자 계정 DB에 등록 // 
        UserList.add(user);

        
        // 도서들 DB에 등록 //
        BookList.add(_123);
        BookList.add(_234);
        BookList.add(_345);

        /**
         * 이정현 수정
         */
        // Intent를 통해 bookid 가져오기 //
        Intent intent = getIntent();
        Bundle intent_data = intent.getExtras();
        bookid = intent_data.getString("bookid");
        
        TextView _bookid = (TextView)findViewById(R.id.bookid);
        
        _bookid.setText("도서 ID : " + bookid);
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
		switch(item.getItemId()){
		case R.id.settings:
			startActivity(new Intent(this, Settings.class));
			return true;
		}
		return false;
	}

	
	// 버튼 클릭 이벤트 처리 
	public void onClick(View view)
	{
	if (view == button) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getBoolean("certification", false)==false){
			showDialog(this, "조회 실패", "유효하지 않은 이용자입니다.");
			 return;
		}
		
		// 책 정보 불러오기 //
		String querybookid = bookid;
		BookSpec querybook = new BookSpec();
		int findflag_book = 0;
		
		for(BookSpec tmp : BookList) {
			if(tmp.get_BookID().equals(querybookid)  ) { 
				querybook = tmp;
				findflag_book = 1;
				}
		}
		
		if( findflag_book == 0) {
			showDialog(this, "도서 조회 실패", "유효하지 않은 도서입니다.");
			 return;
		}
		
		// 책의 현재 상태 확인
		int circulation_case; // 대출반납의 케이스
		
		if(queryuser.get_UserID().equals(querybook.get_Borrower()))
			circulation_case = 1; // 이미 대출한 경우
		else if (! querybook.Borrower.equals(""))
			circulation_case = 2; // 다른 사람에게 대출된 경우
		else 
			circulation_case = 3; // 정상 대출 가능 경우
		
		// 경우에 따른 처리 //
		switch(circulation_case) {
		case 1 :
			showDialog(this, "반납 안내", "반납되었습니다");
			querybook.Borrower = ""; // 책 정보에서 대출자 삭제
			for(BookSpec tmp : queryuser.Borrowing) {
				if(tmp.get_BookID() == querybook.BookID) {
					queryuser.Borrowing.remove(tmp);
				}
			} // 이용자의 책 대여 리스트에서 삭제
			break;
			
		case 2 :
			showDialog(this, "대출 실패", "이미 다른 사람에게 대여된 책입니다.");
			break;
			
		case 3 :
			// 대여 권수 확인 //
			 if( queryuser.get_Borr_num() >= 3 ) {
				 showDialog(this, "대출 실패", "가능 대여권수를 초과하였습니다.");
				 return;
			 }
			
			 // 연체료 확인 //
			 if( queryuser.get_Delay_pay() > 0 ) {
				 showDialog(this, "대출 실패", "연체료를 내지 않았습니다.");
				 return;
			 }
			
			 // 대출 성공 처리 // 
			 showDialog(this, "대출 성공", queryuser.get_UserID() + " " 
			 + queryuser.get_UserName() + '\n' + querybook.BookName 
			 + '(' + querybook.BookID + ')'
			 + "이 대출 되었습니다");
			 
			 BookSpec lended_tmp = new BookSpec();
			 lended_tmp = querybook; 
			 queryuser.Borrowing.add(lended_tmp);      // 이용자 계정에 빌린 책 정보 삽입
			 querybook.set_Borrower(queryuser.UserID); // 책 정보에 이용자 아이디 삽입
			 
			 break;
		
		}
		
    }
	

}

private void showDialog(final CirculationVer1Activity activity, String title, String text) {
		
	AlertDialog.Builder ad = new AlertDialog.Builder(activity);
	ad.setTitle(title);
	ad.setMessage(text);
	ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichbutton) {
			activity.setResult(Activity.RESULT_OK);
		}
	});
	
	ad.create();
	ad.show();
	
	}
}

