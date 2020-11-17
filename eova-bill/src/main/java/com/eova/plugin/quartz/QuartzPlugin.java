/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.plugin.quartz;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.eova.model.Task;
import com.jfinal.kit.LogKit;
import com.jfinal.plugin.IPlugin;

public class QuartzPlugin implements IPlugin {

	private SchedulerFactory sf = null;

	public static Scheduler scheduler = null;

	/**
	 * 启动Quartz
	 */
	@Override
	
	public boolean start() {
		// 创建调度工厂
		sf = new StdSchedulerFactory();

		try {
			scheduler = sf.getScheduler();

			List<Task> tasks = Task.dao.findAll();
			for (Task task : tasks) {
				String jobClassName = task.getStr("clazz");
				String jobCronExp = task.getStr("exp");
				int state = task.getInt("state");
				// String params = task.getInt("params");

				Class clazz;
				try {
					clazz = Class.forName(jobClassName);
				} catch (ClassNotFoundException e) {
					LogKit.error("Eova Job Class Not Found:" + jobClassName);
					continue;
				}

				JobDetail job = JobBuilder.newJob(clazz).withIdentity(jobClassName, jobClassName).build();
				// job.getJobDataMap().put("type", "eova");
				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobClassName).withSchedule(CronScheduleBuilder.cronSchedule(jobCronExp)).build();

				try {
					scheduler.scheduleJob(job, trigger);
					if (state == Task.STATE_STOP) {
						// 暂停触发
						scheduler.pauseTrigger(trigger.getKey());
					}
				} catch (SchedulerException e) {
					new RuntimeException(e);
				}

				LogKit.info(job.getKey() + " loading and exp: " + trigger.getCronExpression());
			}

			scheduler.start();

		} catch (SchedulerException e) {
			new RuntimeException(e);
		}

		return true;

	}

	/**
	 * 停止Quartz
	 */
	@Override
	public boolean stop() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			LogKit.error("shutdown error", e);
			return false;
		}
		return true;
	}

}