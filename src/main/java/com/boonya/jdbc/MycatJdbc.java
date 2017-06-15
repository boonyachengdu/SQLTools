package com.boonya.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

public class MycatJdbc {
	
    private static String driver = "com.mysql.jdbc.Driver";
	
	private static String url = "jdbc:mysql://10.10.11.122:8066/TESTDB?autoReconnect=true&maxReconnects=10&failOverReadOnly=false&useUnicode=true&characterEncoding=utf-8&rewriteBatchedStatements=true";
	
	private static String user = "root";

	private static String password = "123456";
	
	static Connection conn = null;

	static{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 创建连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		if(conn==null){
			conn = (Connection) DriverManager.getConnection(url, user, password);
		}
		return conn;
	}
	
	/**
	 * 关闭连接
	 * 
	 * @throws SQLException
	 */
	public static void close() throws SQLException{
		if(conn!=null){
			conn.close();
		}
	}

}
