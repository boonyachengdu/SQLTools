package com.boonya.factory;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
/**
 * 定义JDBC连接工厂
 * 
 * @author pengjunlin
 *
 */
public interface JdbcConnectionFactory {
	 
	public  Connection getConnection(String type) throws ClassNotFoundException, SQLException;

}
