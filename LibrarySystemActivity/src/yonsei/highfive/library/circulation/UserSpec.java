package yonsei.highfive.library.circulation;

import java.util.ArrayList;

public class UserSpec {

	String UserID;
	String Password;
	String UserName;
	
	public ArrayList <BookSpec> Borrowing = new ArrayList<BookSpec>();
	int Borr_num;
	int Delay_pay;
		
	UserSpec() {}

	public String get_UserID() { return UserID; }
	public String get_Password() { return Password; }
	public String get_UserName() { return UserName; }

	public int get_Borr_num() { return Borr_num; }
	public int get_Delay_pay() { return Delay_pay; }
	
	public void set_UserID( String ID ) { UserID = ID; }
	public void set_Password( String PW ) { Password = PW; }
	public void set_UserName( String UN ) { UserName = UN; }
	
	public void set_Borr_num( int num ) { Borr_num = num; }
	public void set_Delay_pay( int pay ) { Delay_pay = pay; }
}
