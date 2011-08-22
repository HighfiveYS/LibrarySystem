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
	
	// �̿��� ���� // 
	UserSpec user = new UserSpec();
	
	// ���� ��ü�� //
	BookSpec _123 = new BookSpec("005.123", "�ȵ���̵� ����", "����ȭ", "IT���ǻ�");    // ������ å
	BookSpec _234 = new BookSpec("005.234", "�ڹ� ����", "���ڹ�", "��Ŭ��������");      // ���� å
	BookSpec _345 = new BookSpec("005.345", "����� ����", "���ڵ�", "�¼���Ʈ");       // �ٸ������ ����å
	
	// ���� ������ å�� ����Ʈ��. -> ���߿� DB�� ���� ���� ���� //
	ArrayList <UserSpec> UserList = new ArrayList<UserSpec>();
	ArrayList <BookSpec> BookList = new ArrayList<BookSpec>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // ���̾ƿ� ����///
        setContentView(R.layout.circulation);
        
        // ������Ʈ ���� //
        button = (Button) this.findViewById(R.id.button_p);
        button.setOnClickListener(this);
        
        
        // �̿���  ���� ������ ���� //
        user.set_UserID("2000253000");
        user.set_UserName("ȫ�浿");
        user.set_Password("123456");
        user.set_Delay_pay(0);
        user.set_Borr_num(0);
        
        // ���� ���� ���� // 
        _123.set_LanedDate(110815);
        _123.set_DueDate(110830);
        _123.set_Borrower("2000253000");
        _345.set_LanedDate(110815);
        _345.set_DueDate(110830);
        _345.set_Borrower("2000253011");
        
        // �̿��� ������ ���� å ���� �ֱ�
        BookSpec prebook = new BookSpec();
        prebook = _123;
		user.Borrowing.add(prebook);
        // �̿��� ���� DB�� ��� // 
        UserList.add(user);

        
        // ������ DB�� ��� //
        BookList.add(_123);
        BookList.add(_234);
        BookList.add(_345);

        /**
         * ������ ����
         */
        // Intent�� ���� bookid �������� //
        Intent intent = getIntent();
        Bundle intent_data = intent.getExtras();
        bookid = intent_data.getString("bookid");
        
        TextView _bookid = (TextView)findViewById(R.id.bookid);
        
        _bookid.setText("���� ID : " + bookid);
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

	
	// ��ư Ŭ�� �̺�Ʈ ó�� 
	public void onClick(View view)
	{
	if (view == button) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getBoolean("certification", false)==false){
			showDialog(this, "��ȸ ����", "��ȿ���� ���� �̿����Դϴ�.");
			 return;
		}
		
		// å ���� �ҷ����� //
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
			showDialog(this, "���� ��ȸ ����", "��ȿ���� ���� �����Դϴ�.");
			 return;
		}
		
		// å�� ���� ���� Ȯ��
		int circulation_case; // ����ݳ��� ���̽�
		
		if(queryuser.get_UserID().equals(querybook.get_Borrower()))
			circulation_case = 1; // �̹� ������ ���
		else if (! querybook.Borrower.equals(""))
			circulation_case = 2; // �ٸ� ������� ����� ���
		else 
			circulation_case = 3; // ���� ���� ���� ���
		
		// ��쿡 ���� ó�� //
		switch(circulation_case) {
		case 1 :
			showDialog(this, "�ݳ� �ȳ�", "�ݳ��Ǿ����ϴ�");
			querybook.Borrower = ""; // å �������� ������ ����
			for(BookSpec tmp : queryuser.Borrowing) {
				if(tmp.get_BookID() == querybook.BookID) {
					queryuser.Borrowing.remove(tmp);
				}
			} // �̿����� å �뿩 ����Ʈ���� ����
			break;
			
		case 2 :
			showDialog(this, "���� ����", "�̹� �ٸ� ������� �뿩�� å�Դϴ�.");
			break;
			
		case 3 :
			// �뿩 �Ǽ� Ȯ�� //
			 if( queryuser.get_Borr_num() >= 3 ) {
				 showDialog(this, "���� ����", "���� �뿩�Ǽ��� �ʰ��Ͽ����ϴ�.");
				 return;
			 }
			
			 // ��ü�� Ȯ�� //
			 if( queryuser.get_Delay_pay() > 0 ) {
				 showDialog(this, "���� ����", "��ü�Ḧ ���� �ʾҽ��ϴ�.");
				 return;
			 }
			
			 // ���� ���� ó�� // 
			 showDialog(this, "���� ����", queryuser.get_UserID() + " " 
			 + queryuser.get_UserName() + '\n' + querybook.BookName 
			 + '(' + querybook.BookID + ')'
			 + "�� ���� �Ǿ����ϴ�");
			 
			 BookSpec lended_tmp = new BookSpec();
			 lended_tmp = querybook; 
			 queryuser.Borrowing.add(lended_tmp);      // �̿��� ������ ���� å ���� ����
			 querybook.set_Borrower(queryuser.UserID); // å ������ �̿��� ���̵� ����
			 
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

