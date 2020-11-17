/**
 * Copyright (c) 2019 EOVA.CN. All rights reserved.
 * 
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * For authorization, please contact: admin@eova.cn
 */
package com.oss.job;

import org.quartz.JobExecutionContext;

/**
 * 每分钟执行
 *
 * @author Jieven
 * @date 2014-7-7
 */
public class EveryMinJob extends AbsJob {

	@Override
	protected void process(JobExecutionContext context) {
		System.out.println("每分钟任务");
		// context.getJobDetail().getJobDataMap().get("xx参数");
	}
}