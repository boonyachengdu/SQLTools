package com.boonya.factory;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public interface JdbcConnectionFactory {
	 
	public  Connection getConnection(String type) throws ClassNotFoundException, SQLException;

}
