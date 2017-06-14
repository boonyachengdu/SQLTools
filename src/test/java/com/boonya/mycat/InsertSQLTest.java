package com.boonya.mycat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boonya.sql.InsertSQL;
/**
 * 
 * @author pengjunlin
 *
 */
public class InsertSQLTest {
	
	public static void main(String[] args) {
		InsertSQL is=new InsertSQL();
		List<String> list=new ArrayList<String>();
		list.add("id");
		list.add("name");
		
		
		List<Map<String,Object>> values=new ArrayList<Map<String,Object>>();
		
		Map<String,Object> m=null;
		for (int i = 1; i <= 100; i++) {
			m=new HashMap<String, Object>();
			m.put("id", i+"");
			m.put("name", i+"ishdiehiwfsdd");
			values.add(m);
		}
		
		// 测试方法一
		String sql=is.generateSql("test",list , values);
		System.out.println(sql);
		
		
		Map<String,Object> colums=new HashMap<String, Object>();
		colums.put("id", "id");
		colums.put("name", "name");
		
		// 测试方法二
		sql=is.generateSql("test",colums , values);
		System.out.println(sql);
		
		
	}

}
