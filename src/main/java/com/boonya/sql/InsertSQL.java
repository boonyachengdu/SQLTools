package com.boonya.sql;

import java.util.List;
import java.util.Map;
/**
 * 
 * @author pengjunlin
 *
 */
public class InsertSQL {
	
	/**
	 * 生成SQL语句
	 * 
	 * @param table
	 * @param colums
	 * @param values
	 * @return
	 */
	public String generateSql(String table,List<String>colums,List<Map<String,Object>> values){
		
		StringBuffer sql=new StringBuffer("INSERT INTO "+table.toUpperCase()+" ("+StringJoin.join(",", colums)+") ");
		sql.append("VALUES");
		
		for (Map<String, Object> map : values) {
			sql.append("(");
			int count=0;
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : map.entrySet()) {  
				if(count>0){
					sb.append(",");
				}
			    sb.append("'"+(String) entry.getValue()+"'");
			    count++;
			}
			String str=sb.toString();
			sql.append(str);
			sql.append("),");
		}
		return sql.toString().substring(0, sql.toString().length()-1)+";";
	}
	
	/**
	 * 生成SQL语句
	 * 
	 * @param table
	 * @param colums
	 * @param values
	 * @return
	 */
	public String generateSql(String table,Map<String,Object> colums,List<Map<String,Object>> values){
		int count=0;
		StringBuffer sbf=new StringBuffer();
		for (Map.Entry<String, Object> entry : colums.entrySet()) {  
			if(count>0){
				sbf.append(",");
			}
			sbf.append((String) entry.getValue());
		    count++;
		}
		StringBuffer sql=new StringBuffer("INSERT INTO "+table.toUpperCase()+" ("+sbf.toString()+") ");
		sql.append("VALUES");
		
		for (Map<String, Object> map : values) {
			sql.append("(");
			count=0;
			StringBuffer sb=new StringBuffer();
			for (Map.Entry<String, Object> entry : map.entrySet()) {  
				if(count>0){
					sb.append(",");
				}
			    sb.append("'"+(String) entry.getValue()+"'");
			    count++;
			}
			String str=sb.toString();
			sql.append(str);
			sql.append("),");
		}
		return sql.toString().substring(0, sql.toString().length()-1)+";";
	}

}
