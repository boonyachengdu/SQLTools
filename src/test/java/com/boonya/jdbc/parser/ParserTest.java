package com.boonya.jdbc.parser;

import java.util.List;
import org.junit.Test;
import com.boonya.sql.parser.SqlSegment;
import com.boonya.sql.parser.util.SqlParserUtil;
// 参考：http://www.cnblogs.com/zcftech/archive/2013/06/10/3131286.html
public class ParserTest {

	@Test
	public void parseSelect() {
		// TODO Auto-generated method stub
		// String test="select a from b " +
		// "\n"+"where a=b";
		// test=test.replaceAll("\\s{1,}", " ");
		// System.out.println(test);
		// 程序的入口
		String testSql = "select c1,c2,c3 from t1,t2 where condi3=3  or condi4=5 order by o1,o2";
		SqlParserUtil test = new SqlParserUtil();
		String result = test.getParsedSql(testSql);
		System.out.println(result);
		//保存解析结果
		List<SqlSegment> resultList=test.getParsedSqlList(testSql);
		System.out.println(resultList);
	}

}
