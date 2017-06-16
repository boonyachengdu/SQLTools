package com.boonya.performance;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.boonya.jdbc.JDBCUtil;
import com.mysql.jdbc.Connection;

public class PerformanceMain {

	public static void main(String[] args) throws SQLException {
		args = new String[5];
		args[0] = "15";
		args[1] = "insertandquery";
		args[2] = "6666";
		args[3] = "unlimit";
		args[4] = "mycat117";

		if (args == null || args.length != 5) {
			System.out
					.println("需要设置5个参数：args[0]批次数,args[1]操作类型 , args[2]批次样本大小 ,args[3]限制 ,args[4]数据源");
			return;
		}

		if ("localhost".equals(args[4]) || "innodb".equals(args[4])
				|| "mycat".equals(args[4]) || "innodb252".equals(args[4])
				|| "mycat117".equals(args[4])) {

			Connection conn = JDBCUtil.getConnection(args[4]);

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
			List<String> sqls = new ArrayList<String>();
			if ("query".equals(args[1])) {
				// 只读
				int limit = 0;
				if (!"".equals(args[2])) {
					try {
						limit = Integer.valueOf(args[2]);
					} catch (NumberFormatException e) {
						System.err.println("args[2] 操作记录数类型装换错误");
						e.printStackTrace();
						return;
					}
				}
				if (!"unlimit".equals(args[3]) && !"limit".equals(args[3])) {
					System.err.println("args[3]  参数请输入：limit|unlimit");
					return;
				} else if ("unlimit".equals(args[3])) {
					querySql = new String("SELECT * FROM t_location ;");
				} else {
					querySql = new String("SELECT * FROM t_location "
							+ (limit > 0 ? "limit " + limit : "") + ";");
				}
				// 不做查询
				System.out.println("暂不支持query");
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

				Performance p = new Performance();
				p.deleteAll(conn, "t_location");
				p.insertByTransaction(conn, sqls);// T10*L2000
													// 20000->0.879seconds
				// p.insertByTransaction(conn, "", sqls);//T10*L2000
				// 20000->0.952seconds
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
				if (!"unlimit".equals(args[3]) && !"limit".equals(args[3])) {
					System.err.println("args[3]  参数请输入：limit|unlimit");
					return;
				} else if ("unlimit".equals(args[3])) {
					querySql = new String("SELECT * FROM t_location;");
				} else {
					querySql = new String("SELECT * FROM t_location"
							+ (limit > 0 ? "limit " + limit : "") + ";");
				}

				if (limit == 0) {
					limit = 1;
				}
				for (int i = 0; i < threadNum; i++) {
					sql = new String(Performance.getSql(limit));
					sqls.add(sql);
				}

				Performance p = new Performance();
				p.deleteAll(conn, "t_location");
				p.insertAndQueryByTransaction(conn, "select 1", querySql, sqls);

			} else {
				System.out
						.println("args[1] 参数请输入：query|insert|queryandinsert|insertandquery");
				return;
			}
		} else {
			System.out
					.println("arg[4] 数据源请设置为：localhost|innodb|mycat|innodb252|mycat117");
		}

	}

}
