package com.boonya.mycat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.boonya.jdbc.JDBCUtil;
/**
 * 简单的数据库批量性能测试
 * 
 * @author pengjunlin
 *
 */
public class SimpleBatchInsertTest {
	
	public List<String> getSqlList(int total,int perCount) {
		List<String> sqlList = new ArrayList<String>();
		List<Integer> list = new ArrayList<Integer>();

		for (int i = 1; i <= total; i++) {
			list.add(i);
		}

		int index = 0;

		int times = list.size() / perCount;

		do {
			List<Integer> listTemp = null;
			if (list.size() >= perCount) {
				listTemp = list.subList(0, perCount);// listTemp是分段处理逻辑的参数
			} else {
				listTemp = list.subList(0, list.size());// listTemp是分段处理逻辑的参数

			}
			// 遍历当前的值是否正确
			String result = "";
			/*for (int i = 0; i < listTemp.size(); i++) {
				result += listTemp.get(i) + ",";
			}*/
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO userinfo(name,phone,address,time) VALUES");/*#mycat:db_type=master*/
			Random rand = new Random();
			int a, b, c, d;
			for (int i = 1; i <=listTemp.size(); i++) {
				a = rand.nextInt(10);
				b = rand.nextInt(10);
				c = rand.nextInt(10);
				d = rand.nextInt(10);
				if(i==listTemp.size()){
					sql.append("('boonya',"+"'188" + a + "88" + b + c + "66" + d+"','"+"xxxxxxxxxx_" + "188" + a + "88" + b + c
							+ "66" + d+"',NOW());"); 
				}else{
					sql.append("('boonya',"+"'188" + a + "88" + b + c + "66" + d+"','"+"xxxxxxxxxx_" + "188" + a + "88" + b + c
							+ "66" + d+"',NOW()),");
				}
			}
			result=sql.toString();
			
			sqlList.add(result);
			System.out.println("第" + (index+1) + "轮:>>" + result);

			list.removeAll(listTemp);

			System.out.println("当前剩余集合长度:>>" + list.size());
			
			if(list.size()==0){
				break;
			}

			index++;
		} while (index <= times);
		
		return sqlList;
	}
	
	public void execute(int persize){
		try {
			BatchInsert bi=new BatchInsert(); 
			int total=100000;
			SimpleBatchInsertTest bit=new SimpleBatchInsertTest();
			// 删除记录
		    //bi.deleteAll(JDBCUtil.getConnection());
			List<String> sqlList=bit.getSqlList(total, persize);
			// 开始批量插入
			long start=System.currentTimeMillis();
			for (int i = 0; i < sqlList.size(); i++) {
				bi.batchInsertWithTransaction(JDBCUtil.getConnection("innodb"),sqlList.get(i));
			}
			long end=System.currentTimeMillis();
			long cost=end-start;
			bi.insertResult(JDBCUtil.getConnection("innodb"),"MYCAT",persize , cost);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SimpleBatchInsertTest  bit=new SimpleBatchInsertTest();
		int persize=100;
		try {
			for (int i = persize; i <=100000; i++) {
				try {
					bit.execute(persize);
				} catch (Exception e) {
					throw e;
				} 
				if(persize<10000){
					persize+=100;
				}else{
					persize+=1000;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}