package com.boonya.mycat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.boonya.jdbc.JDBCUtil;

public class BatchInsertTest {
	
	
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
			sql.append("/*#mycat:db_type=master*/INSERT INTO userinfo(name,phone,address,time) VALUES");
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
			int total=100000;
			BatchInsertTest bit=new BatchInsertTest();
			//bi.deleteAll(JDBCUtil.getConnection());
			List<String> sqlList=bit.getSqlList(total, persize);
			ExecutorService service=Executors.newSingleThreadExecutor();
			for (int i = 0; i < sqlList.size(); i++) {
				service.execute(new MtThread(sqlList.get(i)));
			}
			
			service.shutdown();//关闭线程池  
			 
			while (!service.isTerminated()) {
				//System.out.println("已插入条数："+bi.queryCount(JDBCUtil.getConnection()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		BatchInsertTest  bit=new BatchInsertTest();
		int persize=100;
		for (int i = persize; i <=100000; i++) {
			bit.execute(persize); 
			if(persize<10000){
				persize+=100;
			}else{
				persize+=1000;
			}
		}
		
	}

}

class MtThread implements Runnable{
	
	String sql;
	

	public MtThread(String sql) {
		super();
		this.sql = sql;
	}


	public void run() {
		synchronized (sql) {
			BatchInsert bi=new BatchInsert();
			try {
				bi.batchInsertWithTransaction(JDBCUtil.getConnection("innodb"),sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
