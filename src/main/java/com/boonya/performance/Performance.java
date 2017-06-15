package com.boonya.performance;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.boonya.jdbc.JDBCUtil;
import com.boonya.utils.UUIDUtil;
import com.mysql.jdbc.Connection;

/**
 * 性能测试方法
 * 
 * @author pengjunlin
 *
 */
public class Performance {
	
	/**
	 * 生成批量插入语句
	 * 
	 * @param count
	 * @return
	 */
	public static String getSql(int count){
		StringBuffer sql = new StringBuffer();
		// F_ID为自增字段，mycat使用需要配置table的autoIncrement属性为true
		sql.append("INSERT INTO t_location (F_ID,F_VEHICLE_ID,F_LATITUDE,F_LONGITUDE,F_HIGH,F_SPEED,"
		+"F_DIRECTION,F_ALARM_COUNT,F_DSPEED,F_OIL_LEVEL,"
		+"F_MILEAGE,F_ALARM_DATA,F_GPS_TIME,F_RECV_TIME,"
		+"F_ACC_STATUS,F_IS_REAL_LOCATION,F_TERMINAL_ID,"
		+"F_ENTERPRISE_CODE,F_GPSENCRYPT,F_GPSSTATE,F_ISPASSUP,"
		+"F_ALARM_DATA1,F_AREASN,F_GEO) VALUES");
		for (int i = 1; i <=count; i++) {
			if(i==count){
				sql.append("('"+UUIDUtil.createUUID()+"','4664901388982278', '0', '0', '0', '0', '0', '0', '0', '0', '1711081', '0', '2017-05-07 00:34:41', '2017-05-06 16:34:35', '1', '0', '4664901388993337', '41082302', '0', '1', '0', '0', '', '')");
			}else{
				sql.append("('"+UUIDUtil.createUUID()+"','4664901388982278', '0', '0', '0', '0', '0', '0', '0', '0', '1711081', '0', '2017-05-07 00:34:41', '2017-05-06 16:34:35', '1', '0', '4664901388993337', '41082302', '0', '1', '0', '0', '', ''),");
			}
		}
		//System.out.println("SQL:"+sql.toString());
		return sql.toString();
	}

	/**
	 * 批量事务插入
	 * 
	 * @param conn
	 * @param sqls
	 */
	public void insertByTransaction(Connection conn, List<String> sqls) {
		Statement stm = null;
		try {
			conn.setAutoCommit(false);// 手动提交
			long start = System.currentTimeMillis();
			stm = conn.createStatement();
			for (String sql : sqls) {
				stm.addBatch(sql);// 每次执行的语句
			}
			stm.executeBatch();
			conn.commit();// 提交事务
			conn.setAutoCommit(true);
			long end = System.currentTimeMillis();
			System.out.println(">>>>批量事务插入完成，耗时：" + (end - start) + "ms,约"
					+ (Double.valueOf(end - start))/1000 + "seconds");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量事务插入
	 * 
	 * @param conn
	 * @param sqls
	 */
	public void insertByTransaction(Connection conn, String preSql,
			List<String> sqls) {
		PreparedStatement pstm = null;
		try {
			conn.setAutoCommit(false);// 手动提交
			long start = System.currentTimeMillis();
			pstm = conn.prepareStatement(preSql);
			for (String sql : sqls) {
				pstm.addBatch(sql);// 每次执行的语句
			}
			pstm.executeBatch();
			conn.commit();// 提交事务
			conn.setAutoCommit(true);
			long end = System.currentTimeMillis();
			System.out.println(">>>>批量事务插入完成，耗时：" + (end - start) + "ms,约"
					+ (Double.valueOf(end - start))/1000 + "seconds");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SQLException {
		args = new String[4];
		args[0] = "10";
		args[1] = "insert";
		args[2] = "2000";
		args[3] = "unlimit";

		Connection conn = JDBCUtil.getConnection("testInnodb");

		boolean isConnected = (conn != null);

		System.out.println("数据库已连接:" + isConnected);
		if (!isConnected) {
			return;
		}

		int threadNum = 0;
		try {
			threadNum = Integer.valueOf(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("args[0] 线程个数类型装换错误");
			e.printStackTrace();
			return;
		}

		if ("".equals(args[1])) {
			System.out
					.println("args[1] 参数请输入：query|insert|queryandinsert|insertandquery");
			return;
		}
		String sql = "";
		String querySql = "";
		List<String> sqls=new ArrayList<String>();
		if ("query".equals(args[1])) {
			// 只读
			int limit = 0;
			if (!"".equals(args[2])) {
				try {
					limit = Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("args[2] 操作记录数类型装换错误");
					e.printStackTrace();
					return;
				}
			}
			if ("unlimit".equals(args[3])) {
				querySql = new String("SELECT * FROM t_location ;");
			} else {
				querySql = new String("SELECT * FROM t_location "
						+ (limit > 0 ? "limit " + limit : "") + ";");
			}
		} else if ("insert".equals(args[1])) {
			// 只写
			int limit = 0;
			if (!"".equals(args[2])) {
				try {
					limit = Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("args[2] 操作数记录数类型装换错误");
					e.printStackTrace();
					return;
				}
			}
			if (limit == 0) {
				limit = 1;
			}
			
			for (int i = 0; i < threadNum; i++) {
				sql = new String(Performance.getSql(limit));
				sqls.add(sql);
			}
			
			Performance p=new Performance();
			//p.insertByTransaction(conn, sqls);//T10*L2000 20000->0.879seconds
            p.insertByTransaction(conn, "select 1", sqls);//T10*L2000 20000->0.952seconds
		} else if ("insertandquery".equals(args[1])
				|| "queryandinsert".equals(args[1])) {
			// 既写又读
			int limit = 0;
			if (!"".equals(args[2])) {
				try {
					limit = Integer.valueOf(args[2]);
				} catch (NumberFormatException e) {
					System.out.println("args[2] 操作数记录数类型装换错误");
					e.printStackTrace();
					return;
				}
			}
			if ("unlimit".equals(args[3])) {
				querySql = new String("SELECT * FROM t_location;");
			} else {
				querySql = new String("SELECT * FROM t_location"
						+ (limit > 0 ? "limit " + limit : "") + ";");
			}

			if (limit == 0) {
				limit = 1;
			}
			
			sql = new String(Performance.getSql(limit));

		} else {
			System.out
					.println("args[1] 参数请输入：query|insert|queryandinsert|insertandquery");
			return;
		}
		
	}

}
