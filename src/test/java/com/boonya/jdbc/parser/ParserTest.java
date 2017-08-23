package com.boonya.jdbc.parser;

import java.util.List;
import org.junit.Test;
import com.boonya.sql.parser.SqlSegment;
import com.boonya.sql.parser.util.SqlParserUtil;
// 参考：http://www.cnblogs.com/zcftech/archive/2013/06/10/3131286.html
public class ParserTest {

	@Test
	public void parseSelectSQL() {
		String sql = "select c1,c2,c3 from t1,t2 where condi3=3  or condi4=5 order by o1,o2";
		System.out.println(sql);
		SqlParserUtil test = new SqlParserUtil();
		String result = test.getParsedSql(sql);
		System.out.println(result);
		//保存解析结果
		List<SqlSegment> resultList=test.getParsedSqlList(sql);
		System.out.println(resultList);
	}
	
	@Test
	public void parseInsertSQL(){
		String sql = "insert into user (field_a,field_b) values ('a','b')"; 
		System.out.println(sql);
		SqlParserUtil test = new SqlParserUtil();
		String result = test.getParsedSql(sql);
		System.out.println(result);
		//保存解析结果
		List<SqlSegment> resultList=test.getParsedSqlList(sql);
		System.out.println(resultList);
	}
	
	@Test
	public void parseDeleteSQL(){
		String sql = "delete from user where id=1"; 
		System.out.println(sql);
		SqlParserUtil test = new SqlParserUtil();
		String result = test.getParsedSql(sql);
		System.out.println(result);
		//保存解析结果
		List<SqlSegment> resultList=test.getParsedSqlList(sql);
		System.out.println(resultList);
	}
	
	@Test
	public void parseUpdateSQL(){
		String sql = "update user set name='boonya' where id=1"; 
		System.out.println(sql);
		SqlParserUtil test = new SqlParserUtil();
		String result = test.getParsedSql(sql);
		System.out.println(result);
		//保存解析结果
		List<SqlSegment> resultList=test.getParsedSqlList(sql);
		System.out.println(resultList);
	}


}
