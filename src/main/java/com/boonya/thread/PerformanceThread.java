package com.boonya.thread;

import com.boonya.performance.Performance;
import com.boonya.performance.PerformanceThreadMain;
import com.mysql.jdbc.Connection;

public class PerformanceThread implements Runnable{
	
	int threadId;
	
	Connection conn=null;
	
	String sql="";
	

	public PerformanceThread(int threadId,Connection conn,String sql) {
		super();
		this.threadId=threadId;
		this.conn=conn;
		this.sql = sql;
	}

	public void run() {
		
		Performance p = new Performance();
		p.insertBatchByTransaction(conn, sql);
		PerformanceThreadMain.count++;
		System.out.println(PerformanceThreadMain.count+"===线程"+threadId+"执行完成==");
	}

}
