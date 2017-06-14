package com.boonya.sql;

import java.util.List;
/**
 * 
 * @author pengjunlin
 *
 */
public class StringJoin {
	
	/**
	 * Join 一个集合
	 * 
	 * @param splitChar
	 * @param values
	 * @return
	 */
	public static String join(String splitChar,List<String> values){
		StringBuffer sb=new StringBuffer();
		int count=0;
		for (String str : values) {
			if(count>0){
				sb.append(splitChar);
			}
			sb.append(str);
			count++;
		}
		return sb.toString();
	}
	
	/**
	 * Join 一个数组
	 * 
	 * @param splitChar
	 * @param values
	 * @return
	 */
	public static String join(String splitChar,String [] values){
		StringBuffer sb=new StringBuffer();
		int count=0;
		for (String str : values) {
			if(count>0){
				sb.append(splitChar);
			}
			sb.append(str);
			count++;
		}
		return sb.toString();
	}

}
