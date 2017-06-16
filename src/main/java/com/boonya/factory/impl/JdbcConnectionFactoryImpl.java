package com.boonya.factory.impl;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.boonya.factory.JdbcConnectionFactory;
import com.boonya.utils.PropertiesReader;
import com.mysql.jdbc.Connection;
/**
 * JDBC连接工厂实现类
 * 
 * @author pengjunlin
 *
 */
public class JdbcConnectionFactoryImpl implements JdbcConnectionFactory {
	
	private static String driver = "com.mysql.jdbc.Driver";
		
	private static  String url = "";
		
    private static String user = "";

	private static String password = "";
	
	private static String jdbcProperties="jdbc.properties";
	
	private static String type = "innodb";
	
	private Connection conn = null;
	
	static{
		try {
			driver=PropertiesReader.getValueByKey(jdbcProperties, "jdbc."+type+".driver");
			Class.forName(driver);
			url=PropertiesReader.getValueByKey(jdbcProperties, "jdbc."+type+".url");
			user=PropertiesReader.getValueByKey(jdbcProperties, "jdbc."+type+".user");
			password=PropertiesReader.getValueByKey(jdbcProperties, "jdbc."+type+".password");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重新设置JDBC参数
	 * 
	 * @param dbtype
	 */
	private void resetParams(String dbtype){
		type=dbtype;
		url=PropertiesReader.getValueByKey(jdbcProperties, "jdbc."+type+".url");
		user=PropertiesReader.getValueByKey(jdbcProperties, "jdbc."+type+".user");
		password=PropertiesReader.getValueByKey(jdbcProperties, "jdbc."+type+".password");
		System.out.println("driver="+driver);
		System.out.println("url="+url);
		System.out.println("user="+user);
		System.out.println("password="+password);
	}

	/**
	 * 获取工厂连接
	 */
	public Connection getConnection(String type) throws ClassNotFoundException, SQLException {
		resetParams(type);
		conn = (Connection) DriverManager.getConnection(url, user, password);
		return conn;
	}

}
