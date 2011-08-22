package yonsei.highfive.library.circulation;

import java.util.ArrayList;

public class BookSpec {
	
	String BookID;
	String BookName;
	String Author;
	String Publishing;
	int LanedDate;
	int DueDate;
	String Borrower;
	ArrayList<UserSpec> R_Borrowers = new ArrayList<UserSpec>();
	boolean extention;
	
	BookSpec(){}
	BookSpec(String id, String name, String aut, String pub) {
		BookID = id;
		BookName = name;
		Author = aut; 
		Publishing = pub;
		Borrower = "";// 대여자가 없는 상태
		extention = false; // 연장 하지 않은 상태
		
	}
	
	
	
	
	public String get_BookID() { return BookID; }
	public String get_BookName() { return BookName; }
	public String get_Author() { return Author; }
	public String get_Publishing() { return Publishing; }
	public int get_LanedDate() { return LanedDate; }
	public int get_DueDate() { return DueDate; }
	public String get_Borrower() { return Borrower; }
	
	public void set_BookID( String ID ) { BookID = ID; }
	public void set_BookName( String Name ) {  BookName = Name; }
	public void set_Author( String At ) {  Author = At; }
	public void set_Publishing( String Pub) {  Publishing = Pub; }
	public void set_LanedDate( int ld) {  LanedDate = ld; }
	public void set_DueDate( int dd) {  DueDate = dd; }
	public void set_Borrower( String borr) {  Borrower = borr; }

}
