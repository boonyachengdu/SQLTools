package com.boonya.mycat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import com.boonya.jdbc.MycatJdbc;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
/**
 * 
 * @author pengjunlin
 *
 */
public class MycatMainTest {
	
	public  static volatile int count=0;
	
	/**
	 * Mycat 获取最后一次插入的ID
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public long getMycatLastInsertId(Connection conn) throws SQLException{
        Statement st = conn.createStatement();
		ResultSet res=st.executeQuery("select last_insert_id();");
		res.next();
		return res.getLong(1);
	}
	
	/**
	 * 插入或查询
	 * 
	 * @param threadId
	 * @param conn
	 * @param sql
	 * @param type
	 */
	public void insertOrQuery(int threadId,Connection conn,String sql,String querySql,String type){
		PreparedStatement pstm = null;
		if(type.equals("insert")){
			try {
				long start=System.currentTimeMillis();
				pstm = (PreparedStatement) conn.prepareStatement(sql);
				pstm.executeUpdate();
				long end=System.currentTimeMillis();
				count++;
				System.out.println("线程"+threadId+"执行完成，耗时："+(end-start)+"ms,约"+(end-start)/1000+"s"); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(type.equals("query")){
			try {
				long start=System.currentTimeMillis();
				pstm = (PreparedStatement) conn.prepareStatement(querySql);
				pstm.executeQuery();
				long end=System.currentTimeMillis();
				count++;
				System.out.println("线程"+threadId+"执行完成，耗时："+(end-start)+"ms,约"+(end-start)/1000+"s"); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if(type.equals("queryandinsert")||type.equals("insertandquery")){
			long start=System.currentTimeMillis();
			try {
				// 写入
				pstm = (PreparedStatement) conn.prepareStatement(sql);
				pstm.executeUpdate();
				// 读取
				pstm = (PreparedStatement) conn.prepareStatement(querySql);
				pstm.executeQuery();
				count++;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			long end=System.currentTimeMillis();
			System.out.println("线程"+threadId+"执行读写完成，耗时："+(end-start)+"ms,约"+(end-start)/1000+"s"); 
		}
		
	}
	
	/**
	 * 生成批量插入语句
	 * 
	 * @param count
	 * @return
	 */
	public String getSql(Connection conn,int count){
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO t_location (F_ID,F_VEHICLE_ID,F_LATITUDE,F_LONGITUDE,F_HIGH,F_SPEED,"
		+"F_DIRECTION,F_ALARM_COUNT,F_DSPEED,F_OIL_LEVEL,"
		+"F_MILEAGE,F_ALARM_DATA,F_GPS_TIME,F_RECV_TIME,"
		+"F_ACC_STATUS,F_IS_REAL_LOCATION,F_TERMINAL_ID,"
		+"F_ENTERPRISE_CODE,F_GPSENCRYPT,F_GPSSTATE,F_ISPASSUP,"
		+"F_ALARM_DATA1,F_AREASN,F_GEO) VALUES");
		for (int i = 1; i <=count; i++) {
			if(i==count){
				sql.append("('"+UUID.randomUUID().toString()+"','4664901388982278', '0', '0', '0', '0', '0', '0', '0', '0', '1711081', '0', '2017-05-07 00:34:41', '2017-05-06 16:34:35', '1', '0', '4664901388993337', '41082302', '0', '1', '0', '0', '', '');");
			}else{
				sql.append("('"+UUID.randomUUID().toString()+"','4664901388982278', '0', '0', '0', '0', '0', '0', '0', '0', '1711081', '0', '2017-05-07 00:34:41', '2017-05-06 16:34:35', '1', '0', '4664901388993337', '41082302', '0', '1', '0', '0', '', ''),");
			}
		}
		//System.out.println("SQL:"+sql.toString());
		return sql.toString();
	}
	
	/**
	 * 函数入口
	 * 
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		MycatMainTest st=new MycatMainTest();
		
		args=new String[4];
		args[0]="1";
		args[1]="insert";
		args[2]="2000";
		args[3]="unlimit";
		
        Connection conn=MycatJdbc.getConnection();
		
		boolean isConnected=(conn!=null);
		
		System.out.println("数据库已连接:"+isConnected);
		if(!isConnected){
			return ;
		}
		
		int threadNum=0;
		try {
			threadNum=Integer.valueOf(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("args[0] 线程个数类型装换错误");
			e.printStackTrace();
			return ;
		}
		
		if("".equals(args[1])){
			System.out.println("args[1] 参数请输入：query|insert|queryandinsert|insertandquery");
			return ;
		}
		String sql="";
		String querySql="";
		if("query".equals(args[1])){
			//  只读
			int limit=0;
			if(!"".equals(args[2])){
				try {
					limit=Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("args[2] 操作记录数类型装换错误");
					e.printStackTrace();
					return ;
				}
			}
			if("unlimit".equals(args[3])){
				querySql=new String("SELECT * FROM t_location ;");
			}else{
				querySql=new String("SELECT * FROM t_location "+(limit>0?"limit "+limit:"")+";");
			}
		}else if("insert".equals(args[1])){
			// 只写
			int limit=0;
			if(!"".equals(args[2])){
				try {
					limit=Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("args[2] 操作数记录数类型装换错误");
					e.printStackTrace();
					return ;
				}
			}
			if(limit==0){
				limit=1;
			}
			sql=new String(st.getSql(conn,limit));
		}else  if("insertandquery".equals(args[1])||"queryandinsert".equals(args[1])){
			// 既写又读
			int limit=0;
			if(!"".equals(args[2])){
				try {
					limit=Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("args[2] 操作数记录数类型装换错误");
					e.printStackTrace();
					return ;
				}
			}
			if("unlimit".equals(args[3])){
				querySql=new String("SELECT * FROM t_location ;");
			}else{
				querySql=new String("SELECT * FROM t_location "+(limit>0?"limit "+limit:"")+";");
			}
			
			if(limit==0){
				limit=1;
			}
			sql=new String(st.getSql(conn,limit));
		
		}else{
			System.out.println("args[1] 参数请输入：query|insert|queryandinsert|insertandquery");
			return ;
		}
		String type=args[1];
		long start=System.currentTimeMillis();
		
		for (int i = 1; i <= threadNum; i++) {
			new Thread(new SHThread(i, conn, sql,querySql,type)).start();
		}
		// 运行线程
		boolean run=true;
		while (run) {
			if(threadNum==count){
				long end=System.currentTimeMillis();
				System.out.println("线程运行完成，共计耗时："+(end-start)+"ms");
				break;
			}
		}
		
	}

}

class SHThread implements Runnable{
	
	int threadId;
	
	Connection conn;
	
	String sql;
	
	String type;
	
	String querySql;

	public SHThread(int threadId,Connection conn,String sql,String querySql,String type) {
		super();
		this.threadId=threadId;
		this.conn=conn;
		this.sql = sql;
		this.type=type;
		this.querySql=querySql;
	}

	public void run() {
		new MycatMainTest().insertOrQuery(threadId, conn, sql,querySql,type);
	}
	
}
