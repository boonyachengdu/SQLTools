package com.boonya.jdbc;

import java.sql.SQLException;
import com.boonya.factory.JdbcConnectionFactory;
import com.boonya.factory.impl.JdbcConnectionFactoryImpl;
import com.mysql.jdbc.Connection;

public class JDBCUtil {

	
	static Connection conn = null;
	
	/**
	 * 创建连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection(String type) throws SQLException{
		if(conn==null){
			JdbcConnectionFactory factory=new JdbcConnectionFactoryImpl();
			try {
				conn=factory.getConnection(type);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
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
