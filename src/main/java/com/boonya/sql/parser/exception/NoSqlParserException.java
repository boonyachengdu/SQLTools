package com.boonya.sql.parser.exception;

public class NoSqlParserException extends Exception {
	private static final long serialVersionUID = 1L;

	NoSqlParserException() {
       super();
	}

	NoSqlParserException(String sql) {
		// 调用父类方法
		super(sql);
	}

}
