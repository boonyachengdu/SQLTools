package com.boonya.jdbc.parser;

import org.junit.Test;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class DruidParserTest {
	
	/**
	 * 一开始需要初始化一个 Parser，在这里 SQLStatementParser 是一个父类，真正解析 SQL 语句的 Parser 实现是 MySqlStatementParser。Parser 的解析结果是一个 SQLStatement，这是一个内部维护了树状逻辑结构的类。
	 */
	@Test
	public void parseSelectSQL(){
		// 参考:https://segmentfault.com/a/1190000008120254
		String sql = "select * from user order by id"; 
		System.out.println(sql);
		// 新建 MySQL	Parser 
		SQLStatementParser parser = new MySqlStatementParser(sql); 
		// 使用Parser解析生成AST，这里SQLStatement就是AST 
		SQLStatement statement = parser.parseStatement(); 
		// 使用visitor来访问AST 
		MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
		statement.accept(visitor); 
		System.out.println(visitor.getColumns()); 
		System.out.println(visitor.getOrderByColumns());
	}
	
	@Test
	public void parseInsertSQL(){
		String sql = "insert into user (field_a,field_b) values ('a','b')"; 
		System.out.println(sql);
		// 新建 MySQL	Parser 
		SQLStatementParser parser = new MySqlStatementParser(sql); 
		// 使用Parser解析生成AST，这里SQLStatement就是AST 
		SQLStatement statement = parser.parseStatement(); 
		// 使用visitor来访问AST 
		MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
		statement.accept(visitor); 
		System.out.println(visitor.getColumns()); 
		System.out.println(visitor.getOrderByColumns());
	}
	
	@Test
	public void parseDeleteSQL(){
		String sql = "delete from user where id=1"; 
		System.out.println(sql);
		// 新建 MySQL	Parser 
		SQLStatementParser parser = new MySqlStatementParser(sql); 
		// 使用Parser解析生成AST，这里SQLStatement就是AST 
		SQLStatement statement = parser.parseStatement(); 
		// 使用visitor来访问AST 
		MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
		statement.accept(visitor); 
		System.out.println(visitor.getColumns()); 
		System.out.println(visitor.getOrderByColumns());
	}
	
	@Test
	public void parseUpdateSQL(){
		String sql = "update user set name='boonya' where id=1"; 
		System.out.println(sql);
		// 新建 MySQL	Parser 
		SQLStatementParser parser = new MySqlStatementParser(sql); 
		// 使用Parser解析生成AST，这里SQLStatement就是AST 
		SQLStatement statement = parser.parseStatement(); 
		// 使用visitor来访问AST 
		MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
		statement.accept(visitor); 
		System.out.println(visitor.getColumns()); 
		System.out.println(visitor.getOrderByColumns());
	}

}
