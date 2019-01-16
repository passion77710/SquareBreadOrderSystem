package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

	static final String driver = "oracle.jdbc.driver.OracleDriver";
	static final String url ="jdbc:oracle:thin:127.0.0.1:1521:orcl";
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url, "scott", "tiger");
		System.out.println("접속성공");
		return con;
	}
}
