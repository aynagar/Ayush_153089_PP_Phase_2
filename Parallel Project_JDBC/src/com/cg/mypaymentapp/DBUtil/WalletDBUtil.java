package com.cg.mypaymentapp.DBUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class WalletDBUtil 
{

	 public static Connection getConnection() throws SQLException, ClassNotFoundException 
	   {
		   
		Connection con=null;
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","hr" , "hr");
		
		return con;
	}
}
