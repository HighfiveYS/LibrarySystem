package yonsei.highfive.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
	public static Connection makeConnection(){
		String url = "jdbc:mysql://boom1492.iptime.org:3306/librarydb";
//		String url = "jdbc:sqldroid:/165.132.214.212/main.sqlite";
		String id = "root";
		String password = "apmsetup";
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, id, password);
		}catch(ClassNotFoundException e){
			System.out.println("드라이버를 찾을 수 없습니다.");
		}catch(SQLException e){
			System.out.println("연결에 실패하였습니다.");
		}
		return con;
	}
	
	public static Statement getStatement(){
		Connection con = makeConnection();
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}
}
