package com.boonya.mycat;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
/**
 * 批量插入JDBC操作类
 * 
 * @author pengjunlin
 *
 */
public class BatchInsert {
	
	private int limit=10;
	
	private String method="batchInsertWithTransaction";
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	

	public void deleteAll(Connection conn){
		try {
			String sql = "DELETE FROM userinfo ;";
			conn.prepareStatement(sql).execute();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	/**
	 * 记录执行的时间
	 * 
	 * @MethodName: insertResult 
	 * @Description: 
	 * @param methodName
	 * @param limit
	 * @param timeStr
	 * @throws
	 */
	public void insertResult(Connection conn,String methodName,int limit,long time) {
		PreparedStatement pstm = null;
		try {
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sql = "/*#mycat:db_type=master*/INSERT INTO processtask (pmethod,plimit,ptime,systime) VALUES('"+methodName+"',"+limit+","+time+",'"+sdf.format(new Date())+"')";
			System.out.println(sql);
			pstm = (PreparedStatement) conn.prepareStatement(sql);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void batchInsertWithTransaction(Connection conn,String sql) {
		PreparedStatement pstm = null;
		try {
			conn.setAutoCommit(false);// 即手动提交
			pstm = (PreparedStatement) conn.prepareStatement(sql);
			Long startTime = System.currentTimeMillis();
			pstm.execute();
			conn.commit();// 手动提交
			Long endTime = System.currentTimeMillis();
			String timeStr=(endTime - startTime)+""; 
			System.out.println("OK,用时：" + timeStr);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pstm != null) {
				try {
					pstm.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	public long queryProcessResult(Connection conn) throws ClassNotFoundException, SQLException{
		Statement st = conn.createStatement();
		
		ResultSet res=st.executeQuery("select * from userinfo where id in(1,100000) order by id");
		
		ResultSetMetaData meta = res.getMetaData();
		// System.out.println( "\t"+res.getRow()+"条记录");
		String str = "";
		for (int i = 1; i <= meta.getColumnCount(); i++) {
			str += meta.getColumnName(i) + "   ";
			// System.out.println( meta.getColumnName(i)+"   ");
		}
		System.out.println("\t" + str);
		str = "";
		Date start=null,end=null;
		int index=0;
		while (res.next()) {
			if(index==0){
				start=res.getTimestamp(5);
			}else{
				end=res.getTimestamp(5);
			}
			index++;
		}
		long cost=end.getTime()-start.getTime();
		return cost;
	}
	
	public int queryCount(Connection conn) throws ClassNotFoundException, SQLException{
		
		Statement st = conn.createStatement();
		
		ResultSet res=st.executeQuery("select count(*) from userinfo");
		
		res.next();
		
		return res.getInt(1);
	}


}

