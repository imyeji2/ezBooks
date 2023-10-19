package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//드라이버 로딩과 db연결을 하는 클래스
public class DbUtil {
	//static 초기화 블럭 - 해당 클래스가 메모리에 로딩될때 static변수가 초기화되고, static초기화 블럭이
	//딱 한번 호출된다
	static {
		//1. 드라이버 로딩
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("드라이버 로딩 성공");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패!");
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(String url, String user, String upwd) 
			throws SQLException {
		//2. db연결
		Connection con=DriverManager.getConnection(url, user, upwd);
		System.out.println("db연결, con="+con);
		return con;
	}
	
	public static Connection getConnection(String user, String upwd) 
			throws SQLException {
		String url="jdbc:oracle:thin:@www.mingi.ze.am:1521:xe";
		Connection con=getConnection(url, user, upwd);
		return con;
	}
	
	public static Connection getConnection() throws SQLException {
		String url="jdbc:oracle:thin:@www.mingi.ze.am:1521:xe";
		String user="teamproject", upwd="192837";
		Connection con=getConnection(url, user, upwd);
		return con;
	}
	
	public static void dbClose(ResultSet rs, PreparedStatement ps, Connection con) 
			throws SQLException {
		if(rs!=null) rs.close();
		if(ps!=null) ps.close();
		if(con!=null) con.close();		
	}
	
	public static void dbClose(PreparedStatement ps, Connection con) 
			throws SQLException {
		if(ps!=null) ps.close();
		if(con!=null) con.close();		
	}
	
	
	
}






