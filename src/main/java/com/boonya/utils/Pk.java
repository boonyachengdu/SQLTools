package com.boonya.utils;

public class Pk {

	private static long[] ls = new long[3000];

	private static int li = 0;

	/**
	 * 算法获取主键ID
	 * 
	 * getID
	 * 
	 * @return
	 */
	public synchronized static long getPK() {
		/*
		 * 2012-10-18 苏沫予： 修改主键生成算法，使主键在软件生命周期内具有时间连续性同时适应JS
		 * 前端应用，根据当前算法，至少17年内不会发生2^53溢出问题 - 关于性能问题：
		 * 新主键方案每10毫秒内有十万分之一的可能性会发生冲突主键问题，因此
		 * 当系统每秒数据生成量达到100条时生成器实际性能大约下降0.003%，此
		 * 后呈线性下降，每秒数据生成量150000条时，主键生成性能大约下降一倍， 每秒生成数据超过300000条后，该主键生成算法将不再安全 -
		 * 关于并发问题： 该算法并发运行时（例如分布式服务器系统）存在每秒千万分之一的冲突
		 * 可能性，因此建议不用于并发式系统，即便投入应用，也应当保证每秒并 发插入数据不超过1000条。
		 */
		String a = String
				.valueOf((System.currentTimeMillis() / 10L) % 100000000000L);
		String d = (String.valueOf((1 + Math.random()) * 100000)).substring(1,
				6);
		// 苏沫予：添加代码结束（同时移除韩欣宇的代码方案）
		long lo = Long.parseLong(a + d);
		for (int i = 0; i < 3000; i++) {
			long lt = ls[i];
			if (lt == lo) {
				lo = getPK();
				break;
			}
		}
		ls[li] = lo;
		li++;
		if (li == 3000) {
			li = 0;
		}
		return lo;
	}

}
